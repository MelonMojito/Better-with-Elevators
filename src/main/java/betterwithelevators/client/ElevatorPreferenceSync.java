package betterwithelevators.client;

import betterwithelevators.BetterWithElevators;
import betterwithelevators.net.ElevatorPreferenceMessage;
import net.minecraft.core.entity.player.Player;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.lang.ref.WeakReference;

/**
 * Pushes the client's elevator option to the server, so opting out works in
 * multiplayer too. Sends once after joining and again whenever the option
 * changes. In singleplayer halplibe loops the message straight back.
 */
public final class ElevatorPreferenceSync {

	//give the server's login packets (which sync network message ids) time to land first
	private static final int JOIN_DELAY_TICKS = 40;

	//weak: never keep a dead player (and its world) alive after leaving a server
	private static WeakReference<Player> lastPlayer = new WeakReference<>(null);
	private static Boolean lastSent;
	private static int ticksSinceJoin;
	private static boolean unsupported;

	private ElevatorPreferenceSync() {
	}

	public static void tick(Player player) {
		if (BetterWithElevatorsClient.elevatorsEnabled == null) {
			return;
		}

		if (player != lastPlayer.get()) {
			//joined a world, or changed dimension/server: start over
			lastPlayer = new WeakReference<>(player);
			lastSent = null;
			ticksSinceJoin = 0;
			unsupported = false;
		}

		if (ticksSinceJoin < JOIN_DELAY_TICKS) {
			ticksSinceJoin++;
			return;
		}

		if (unsupported) {
			return;
		}

		boolean current = BetterWithElevatorsClient.elevatorsEnabled.value;
		if (lastSent != null && lastSent == current) {
			return;
		}

		try {
			//compatibility send: a server without the mod ignores the custom payload instead of kicking us
			NetworkHandler.sendCompatibilityToServer(new ElevatorPreferenceMessage(current));
			lastSent = current;
		} catch (Exception e) {
			//server does not know this message, so it has no elevators to opt out of
			unsupported = true;
			BetterWithElevators.LOGGER.info("Server did not accept the elevator preference, skipping further syncs.");
		}
	}
}

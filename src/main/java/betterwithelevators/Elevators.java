package betterwithelevators;

import betterwithelevators.config.ElevatorConfig;
import net.minecraft.core.entity.player.Player;

/**
 * Shared elevator gating. Loaded on both client and server, so nothing here may
 * reference client-only classes.
 */
public final class Elevators {

	private Elevators() {
	}

	/**
	 * Whether this player may use elevators right now.
	 * <p>
	 * The world setting wins: if elevators are off here, nobody uses them, no
	 * matter what a client has chosen. If they are on, each player may still opt
	 * out for themselves through the client option.
	 */
	public static boolean isEnabled(Player player) {
		if (!ElevatorConfig.get().enabled) {
			return false;
		}
		return ((ElevatorPreference) player).betterwithelevators$elevatorsEnabled();
	}
}

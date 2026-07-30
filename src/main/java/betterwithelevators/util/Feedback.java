package betterwithelevators.util;

import betterwithelevators.BetterWithElevators;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketPlaySoundDirect;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.sound.SoundTypes;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;

/**
 * Command feedback using base game text formatting.
 * Works for both a dedicated server player and a singleplayer client player.
 * Arguments (%s) are wrapped in gray brackets: [arg]
 */
public class Feedback {

	public static void success(@NotNull Player player, @NotNull String message, Object... args) {
		send(player, TextFormatting.LIME, "note.harp", message, args);
	}

	public static void error(@NotNull Player player, @NotNull String message, Object... args) {
		send(player, TextFormatting.RED, "note.bd", message, args);
	}

	public static void destructive(@NotNull Player player, @NotNull String message, Object... args) {
		send(player, TextFormatting.ORANGE, "note.snare", message, args);
	}

	/** Sends a line with no sound, for multi-line output like /elevator info. */
	public static void message(@NotNull Player player, @NotNull String message, Object... args) {
		send(player, TextFormatting.LIGHT_GRAY, null, message, args);
	}

	private static void send(@NotNull Player player, @NotNull TextFormatting color, String soundPath, @NotNull String message, Object... args) {
		player.sendMessage(format(color, message, args));
		BetterWithElevators.LOGGER.info(String.format("Sent command feedback: [%s] to player: [username: %s, uuid: %s]", formatRaw(message, args), player.username, player.uuid));
		if (soundPath != null) {
			playSound(player, soundPath);
		}
	}

	public static @NotNull String format(@NotNull TextFormatting color, @NotNull String message, Object... args) {
		Object[] argStrings = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			argStrings[i] = "" + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + args[i] + TextFormatting.RESET + TextFormatting.GRAY + "]" + color;
		}
		return color + String.format(message, argStrings);
	}

	private static @NotNull String formatRaw(@NotNull String message, Object... args) {
		Object[] argStrings = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			argStrings[i] = "[" + args[i] + "]";
		}
		return String.format(message, argStrings);
	}

	public static void playSound(@NotNull Player player, @NotNull String soundPath) {
		if (player instanceof PlayerServer) {
			((PlayerServer) player).playerNetServerHandler.sendPacket(
				new PacketPlaySoundDirect(
					SoundTypes.getSoundId(soundPath),
					SoundCategory.GUI_SOUNDS,
					player.x,
					player.y,
					player.z,
					1f,
					1f
				)
			);
		} else if (player.world != null) {
			player.world.playSoundAtEntity(player, player, soundPath, 1f, 1f);
		}
	}
}

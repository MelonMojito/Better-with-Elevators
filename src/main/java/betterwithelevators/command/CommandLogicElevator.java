package betterwithelevators.command;

import betterwithelevators.config.ElevatorConfig;
import betterwithelevators.util.Feedback;
import com.mojang.brigadier.Command;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;

public class CommandLogicElevator {

	public static int enabled(Player sender){
		ElevatorConfig config = ElevatorConfig.get();
		if(config.enabled){
			config.enabled = false;
			ElevatorConfig.save();
			Feedback.destructive(sender, "Elevators Disabled (for everyone)");
		} else {
			config.enabled = true;
			ElevatorConfig.save();
			Feedback.success(sender, "Elevators Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int allowObstructions(Player sender){
		ElevatorConfig config = ElevatorConfig.get();
		if(config.allowObstructions){
			config.allowObstructions = false;
			ElevatorConfig.save();
			Feedback.destructive(sender, "Allow Obstructions Disabled");
		} else {
			config.allowObstructions = true;
			ElevatorConfig.save();
			Feedback.success(sender, "Allow Obstructions Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int cooldown(Player sender, int cooldownValue) {
		ElevatorConfig.get().elevatorCooldown = cooldownValue;
		ElevatorConfig.save();
		Feedback.success(sender, "Elevator Cooldown Set to %s", cooldownValue);
		return Command.SINGLE_SUCCESS;
	}

	public static int block(Player sender, String blockName) {
		Block<?> block = ElevatorConfig.findBlock(blockName);
		if (block == null) {
			Feedback.error(sender, "Failed to Set Elevator Block! (Unknown Block %s)", blockName);
			return 0;
		}

		ElevatorConfig.get().elevatorBlock = block.namespaceId().toString();
		ElevatorConfig.invalidateBlock();
		ElevatorConfig.save();
		Feedback.success(sender, "Elevator Block Set to %s", block.namespaceId());
		return Command.SINGLE_SUCCESS;
	}

	public static int blockReset(Player sender) {
		ElevatorConfig.get().elevatorBlock = ElevatorConfig.DEFAULT_BLOCK;
		ElevatorConfig.invalidateBlock();
		ElevatorConfig.save();
		Feedback.destructive(sender, "Elevator Block Reset to %s", ElevatorConfig.DEFAULT_BLOCK);
		return Command.SINGLE_SUCCESS;
	}

	public static int info(Player sender) {
		ElevatorConfig config = ElevatorConfig.get();
		Feedback.message(sender, "Elevators: %s", config.enabled ? "Enabled" : "Disabled");
		Feedback.message(sender, "Elevator Block: %s", config.elevatorBlock);
		Feedback.message(sender, "Cooldown: %s", config.elevatorCooldown);
		Feedback.message(sender, "Allow Obstructions: %s", config.allowObstructions);
		Feedback.playSound(sender, "note.harp");
		return Command.SINGLE_SUCCESS;
	}
}

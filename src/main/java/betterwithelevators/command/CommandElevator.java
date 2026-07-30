package betterwithelevators.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

@SuppressWarnings("UnusedReturnValue")
public class CommandElevator implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorToggle(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		ArgumentBuilderLiteral<CommandSource> toggle = ArgumentBuilderLiteral.<CommandSource>literal("toggle");

		toggle.then(ArgumentBuilderLiteral.<CommandSource>literal("enabled")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicElevator.enabled(sender);
				}
			)
		);

		toggle.then(ArgumentBuilderLiteral.<CommandSource>literal("allowobstructions")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicElevator.allowObstructions(sender);
				}
			)
		);

		builder.then(toggle);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorCooldown(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("cooldown")
			.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("cooldownvalue", ArgumentTypeInteger.integer(0, 256))
				.executes(context ->
					{
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						int cooldownValue = context.getArgument("cooldownvalue", Integer.class);
						return CommandLogicElevator.cooldown(sender, cooldownValue);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorBlock(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("block")
			.then(ArgumentBuilderLiteral.<CommandSource>literal("reset")
				.executes(context ->
					{
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						return CommandLogicElevator.blockReset(sender);
					}
				)
			)
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("block", ArgumentTypeBlock.block())
				.executes(context ->
					{
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						String blockName = context.getArgument("block", String.class);
						return CommandLogicElevator.block(sender, blockName);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorInfo(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("info")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicElevator.info(sender);
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("elevator").requires(CommandSource::hasAdmin);

		elevatorToggle(builder);
		elevatorCooldown(builder);
		elevatorBlock(builder);
		elevatorInfo(builder);

		dispatcher.register(builder);
	}
}

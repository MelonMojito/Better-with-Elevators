package betterwithelevators.client;

import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.OptionsInitEntrypoint;

/**
 * Client-only half of the mod: the in-game elevator toggle. Registered under
 * client-only entrypoints so a dedicated server never loads this class.
 */
public class BetterWithElevatorsClient implements OptionsInitEntrypoint, ClientStartEntrypoint {

	public static OptionBoolean elevatorsEnabled;

	@Override
	public void initOptions() {
		elevatorsEnabled = GameSettings.register(new OptionBoolean("betterwithelevators.elevators.enabled", true));
	}

	@Override
	public void beforeClientStart() {
	}

	@Override
	public void afterClientStart() {
		OptionsPages.register(
			new OptionsPage("options.betterwithelevators.title", new ItemStack(Blocks.BLOCK_STEEL))
				.withComponent(new OptionsCategory("options.betterwithelevators.category.elevators")
					.withComponent(new BooleanOptionComponent(elevatorsEnabled))
				)
		);
	}
}

package betterwithelevators;

import betterwithelevators.command.CommandElevator;
import betterwithelevators.config.ElevatorConfig;
import betterwithelevators.net.ElevatorPreferenceMessage;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class BetterWithElevators implements ModInitializer {

	public static final String MOD_ID = HalpLibe.registerMod("betterwithelevators", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Better with Elevators initializing!");
		ElevatorConfig.load();
		//registerCommand (not registerServerCommand) so /elevator also exists in singleplayer
		CommandManager.registerCommand(new CommandElevator());
		//both sides register it: ids are assigned at game start and synced to clients at login
		NetworkHandler.registerNetworkMessage(ElevatorPreferenceMessage::new);
		LOGGER.info("Better with Elevators initialized!");
	}
}

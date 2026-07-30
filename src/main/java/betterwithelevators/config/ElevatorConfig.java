package betterwithelevators.config;

import betterwithelevators.BetterWithElevators;
import com.b100.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElevatorConfig {

	public static final String DEFAULT_BLOCK = "minecraft:block/block_steel";

	public boolean enabled = true;
	public boolean allowObstructions = true;
	public int elevatorCooldown = 6;
	public String elevatorBlock = DEFAULT_BLOCK;

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final File configFile =
		new File(FabricLoader.getInstance().getConfigDir() + "/" + BetterWithElevators.MOD_ID, "config.json");

	private static ElevatorConfig config = new ElevatorConfig();

	//resolved lazily: blocks from other mods may not be registered yet when the config loads
	private static Block<?> resolvedBlock;
	private static String resolvedFor;

	public static ElevatorConfig get() {
		return config;
	}

	public static void load() {
		if (!configFile.exists()) {
			save();
			return;
		}
		try (JsonReader reader = new JsonReader(new FileReader(configFile))) {
			ElevatorConfig loaded = gson.fromJson(reader, ElevatorConfig.class);
			if (loaded != null) {
				config = loaded;
			}
		} catch (FileNotFoundException e) {
			BetterWithElevators.LOGGER.error("Could not find config, using defaults!");
		} catch (IOException e) {
			BetterWithElevators.LOGGER.error("Could not read config, using defaults!");
		}
		invalidateBlock();
	}

	public static void save() {
		File file = FileUtils.createNewFile(configFile);
		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(config, ElevatorConfig.class, writer);
		} catch (IOException e) {
			BetterWithElevators.LOGGER.error("Config failed to save!");
		}
	}

	public static void invalidateBlock() {
		resolvedBlock = null;
		resolvedFor = null;
	}

	/**
	 * The block elevators are made of. Falls back to steel if the configured block cannot be found.
	 */
	public static Block<?> getElevatorBlock() {
		if (resolvedBlock != null && config.elevatorBlock.equals(resolvedFor)) {
			return resolvedBlock;
		}
		Block<?> block = findBlock(config.elevatorBlock);
		if (block == null) {
			BetterWithElevators.LOGGER.warn("Unknown elevator block '{}', falling back to {}", config.elevatorBlock, DEFAULT_BLOCK);
			block = Blocks.BLOCK_STEEL;
		}
		resolvedBlock = block;
		resolvedFor = config.elevatorBlock;
		return block;
	}

	/**
	 * Accepts a namespace id ("minecraft:block/block_steel") or a translation key ("block.steel").
	 */
	public static @Nullable Block<?> findBlock(String name) {
		if (name == null || name.isEmpty()) return null;
		for (Map.Entry<net.minecraft.core.util.collection.NamespaceID, Block<?>> entry : Blocks.blockMap.entrySet()) {
			if (entry.getKey().toString().equals(name)) {
				return entry.getValue();
			}
		}
		for (Block<?> block : Blocks.blockMap.values()) {
			if (block.getKey().equals(name)) {
				return block;
			}
		}
		return null;
	}

	public static List<String> blockNames() {
		List<String> names = new ArrayList<>();
		for (net.minecraft.core.util.collection.NamespaceID id : Blocks.blockMap.keySet()) {
			names.add(id.toString());
		}
		return names;
	}
}

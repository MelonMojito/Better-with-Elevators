package betterwithelevators.util;

import betterwithelevators.config.ElevatorConfig;
import melib.util.Teleport;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;

public class ElevatorUtil {

	// returns true if we teleported
	public static boolean jumpOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) == ElevatorConfig.getElevatorBlock() && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				Teleport.teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !ElevatorConfig.get().allowObstructions) {
				break;
			}
		}
		return false;
	}

	// returns true if we teleported
	public static boolean sneakOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y-1; y2 > 0; y2--){
			if(world.getBlock(x, y2, z) == ElevatorConfig.getElevatorBlock() && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				Teleport.teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !ElevatorConfig.get().allowObstructions) {
				break;
			}
		}
		return false;
	}

}

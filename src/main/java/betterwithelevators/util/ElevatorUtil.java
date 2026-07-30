package betterwithelevators.util;

import betterwithelevators.config.ElevatorConfig;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;

public class ElevatorUtil {

	// returns true if we teleported
	public static boolean jumpOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) == ElevatorConfig.getElevatorBlock() && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				teleport(x+0.5, y2+1, z+0.5, player);
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
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !ElevatorConfig.get().allowObstructions) {
				break;
			}
		}
		return false;
	}

	public static void teleport(double x, double y, double z, @NotNull Player player){
		assert player.world != null;
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
		//singleplayer moves the local player directly; a dedicated server has to send a teleport packet
		if (player instanceof PlayerServer){
			((PlayerServer) player).playerNetServerHandler.teleport(x, y + 0.2, z);
		} else {
			player.setPos(x, y + player.bbHeight + 0.2, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
	}
}

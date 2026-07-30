package betterwithelevators.mixin.client;

import betterwithelevators.client.ElevatorPreferenceSync;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixin {

	@Inject(method = "onLivingUpdate", at = @At("TAIL"))
	private void syncElevatorPreference(CallbackInfo ci) {
		ElevatorPreferenceSync.tick((Player) (Object) this);
	}
}

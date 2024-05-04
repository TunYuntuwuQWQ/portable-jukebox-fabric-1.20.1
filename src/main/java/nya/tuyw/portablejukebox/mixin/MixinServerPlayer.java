package nya.tuyw.portablejukebox.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import nya.tuyw.portablejukebox.jukebox.PortableJukeboxItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer {
    @Inject(method = "dropItem", at = @At("HEAD"))
    private void pj$inject$dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (!(stack.getItem() instanceof PortableJukeboxItem jukebox)) return;
        jukebox.onDroppedByPlayer(stack, (ServerPlayerEntity) (Object) this);
    }
}


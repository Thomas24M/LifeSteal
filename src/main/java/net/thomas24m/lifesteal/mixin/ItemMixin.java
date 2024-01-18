package net.thomas24m.lifesteal.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.thomas24m.lifesteal.util.LifeStealUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(at = @At("HEAD"), method = "use")
    private void injectUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient) {
            ItemStack item = user.getStackInHand(hand);
            if (item != null && LifeStealUtils.isHeartItem(item)) {
                double changed = LifeStealUtils.addHealth((ServerPlayerEntity) user, 2);
                if (changed > 0) {
                    item.decrement(1);
                    cir.setReturnValue(TypedActionResult.consume(item));
                }
            }
        }
    }
}

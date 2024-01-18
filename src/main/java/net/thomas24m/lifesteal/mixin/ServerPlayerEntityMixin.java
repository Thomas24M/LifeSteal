package net.thomas24m.lifesteal.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.thomas24m.lifesteal.LifeSteal;
import net.thomas24m.lifesteal.util.LifeStealUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void preserveMaxHealth(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        EntityAttributeInstance oldHealth = oldPlayer.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert oldHealth != null;
        EntityAttributeInstance health = ((ServerPlayerEntity) (Object) this).getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        health.setBaseValue(oldHealth.getBaseValue());
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeathLowerMaxHealth(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld world = (ServerWorld) player.getWorld();
        Entity entity = source.getAttacker();
        int stealAmount = world.getGameRules().getInt(LifeSteal.STEALAMOUNT);
        if (entity instanceof ServerPlayerEntity) {
            double change = LifeStealUtils.subtractHealth(player, stealAmount);
            LifeStealUtils.addHealth((ServerPlayerEntity) entity, change);
            if (world.getGameRules().getBoolean(LifeSteal.BANWHENMINHEALTH) && change == 0) {
                LifeStealUtils.banPlayer(player);
                LifeStealUtils.addHealth((ServerPlayerEntity) entity, 2);
            }
        } else if (!world.getGameRules().getBoolean(LifeSteal.PLAYERRELATEDONLY)) {
            double change = LifeStealUtils.subtractHealth(player, stealAmount);
            if (world.getGameRules().getBoolean(LifeSteal.BANWHENMINHEALTH) && change == 0) {
                LifeStealUtils.banPlayer(player);
            }
        }
    }
}

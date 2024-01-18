package net.thomas24m.lifesteal.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thomas24m.lifesteal.LifeSteal;

import java.util.Objects;
import java.util.UUID;

public class LifeStealUtils {
    private LifeStealUtils() {
    }

    public static final String HEART_ITEM_NBT_TAG = LifeSteal.MOD_ID + ".heart";

    public static double setHealth(ServerPlayerEntity player, double value) {
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        health.setBaseValue(value);
        return health.getBaseValue();
    }

    public static double addHealth(ServerPlayerEntity player, double amount) {
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        double current = health.getBaseValue();
        double newValue = Math.min(current + amount, player.getWorld().getGameRules().getInt(LifeSteal.MAXPLAYERHEALTH));
        health.setBaseValue(newValue);
        return newValue - current;
    }

    public static double subtractHealth(ServerPlayerEntity player, double amount) {
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        double current = health.getBaseValue();
        double newValue = Math.max(current - amount, Math.max(1, player.getWorld().getGameRules().getInt(LifeSteal.MINPLAYERHEALTH)));
        health.setBaseValue(newValue);
        return current - newValue;
    }

    public static ItemStack heartItem(int count) {
        ItemStack item = new ItemStack(Items.NETHER_STAR, count);

        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean(HEART_ITEM_NBT_TAG, true);
        item.setNbt(nbt);

        item.setCustomName(Text.literal("Heart").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.RED)));

        return item;
    }

    public static boolean isHeartItem(ItemStack item) {
        if (item.hasNbt()) {
            NbtCompound nbt = item.getNbt();
            if (nbt.contains(HEART_ITEM_NBT_TAG)) {
                return nbt.getBoolean(HEART_ITEM_NBT_TAG);
            }
        }
        return false;
    }

    public static int giveItem(ServerPlayerEntity to, ItemStack item) {
        int count = item.getCount();
        int i = item.getItem().getMaxCount();
        int j = i * 100;
        if (count > j) {
            return 0;
        }
        int k = count;
        while (k > 0) {
            ItemEntity itemEntity;
            int l = Math.min(i, k);
            k -= l;
            ItemStack itemStack2 = item.copy();
            itemStack2.setCount(l);
            boolean bl = to.getInventory().insertStack(itemStack2);
            if (!bl || !itemStack2.isEmpty()) {
                itemEntity = to.dropItem(itemStack2, false);
                if (itemEntity == null) continue;
                itemEntity.resetPickupDelay();
                itemEntity.setOwner(to.getUuid());
                continue;
            }
            itemStack2.setCount(1);
            itemEntity = to.dropItem(itemStack2, false);
            if (itemEntity != null) {
                itemEntity.setDespawnImmediately();
            }
            to.getWorld().playSound(null, to.getX(), to.getY(), to.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((to.getRandom().nextFloat() - to.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            to.currentScreenHandler.sendContentUpdates();
        }
        return 1;
    }

    public static void banPlayer(ServerPlayerEntity player) {
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        health.setBaseValue(20);
        MinecraftServer server = player.getServer();
        assert server != null;
        String text = "You lost your last life.";
        player.networkHandler.disconnect(Text.literal(text));
        server.getPlayerManager().getUserBanList().add(new BannedPlayerEntry(player.getGameProfile(), null, null, null, text));
    }
}

package net.thomas24m.lifesteal.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.NetherStarItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.thomas24m.lifesteal.LifeSteal;

public class WithdrawCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("withdraw")
                .then(CommandManager.argument("count", IntegerArgumentType.integer(1))
                .executes(context -> WithdrawCommand.run(context, IntegerArgumentType.getInteger(context, "count")))));
    }

    private static int run(CommandContext<ServerCommandSource> context, int count) throws CommandSyntaxException {
        LifeSteal.LOGGER.info("Works");

        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;

        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert health != null;
        health.setBaseValue(health.getValue() - 2d * count);

        ItemStack item = new ItemStack(Items.NETHER_STAR, count);

        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("lifesteal.heart", true);
        item.setNbt(nbt);

        item.setCustomName(Text.literal("Heart").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.RED)));

        player.giveItemStack(item);

        context.getSource().sendFeedback(() -> Text.literal("Withdrew %d %s".formatted(count, count > 1 ? "hearts" : "heart")), false);
        return 1;
    }
}

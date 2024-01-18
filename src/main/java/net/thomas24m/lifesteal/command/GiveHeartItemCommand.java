package net.thomas24m.lifesteal.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thomas24m.lifesteal.util.LifeStealUtils;

public class GiveHeartItemCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("giveheartitem").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("count", IntegerArgumentType.integer(1))
                                .executes(context -> run(context, EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "count"))))));
    }

    private static int run(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, int count) {
        int success = LifeStealUtils.giveItem(player, LifeStealUtils.heartItem(count));
        if (success > 0) {
            context.getSource().sendFeedback(() -> Text.literal("Gave %d %s to ".formatted(count, count > 1 ? "heart items" : "heart item")).append(player.getDisplayName().copy()), true);
            return success;
        } else {
            context.getSource().sendFeedback(() -> Text.literal("Could not give %d heart items to ".formatted(count)).append(player.getDisplayName().copy()).append(Text.literal(", too many items").setStyle(Style.EMPTY.withFormatting(Formatting.WHITE))), true);
            return success;
        }

    }
}

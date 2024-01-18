package net.thomas24m.lifesteal.command.health;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thomas24m.lifesteal.util.LifeStealUtils;

import java.util.Objects;

public class HealthSetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("health").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(1))
                                        .executes(context -> run(context, EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "value")))))));
    }

    private static int run(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, int value) {
        double newValue = LifeStealUtils.setHealth(player, (value));
        context.getSource().sendFeedback(() -> Text.literal("Set max health of ").append(player.getDisplayName().copy()).append(Text.literal(" to %d".formatted((int) newValue)).setStyle(Style.EMPTY.withFormatting(Formatting.WHITE))), true);
        return 1;
    }
}

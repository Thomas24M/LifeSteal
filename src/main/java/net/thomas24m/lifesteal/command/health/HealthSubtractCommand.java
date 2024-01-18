package net.thomas24m.lifesteal.command.health;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.thomas24m.lifesteal.util.LifeStealUtils;

public class HealthSubtractCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("health").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("subtract")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(context -> run(context, EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount")))))));
    }

    private static int run(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, int amount) {
        double change = LifeStealUtils.subtractHealth(player, (amount));
        context.getSource().sendFeedback(() -> Text.literal("Subtracted %d from the max health of ".formatted((int) change)).setStyle(Style.EMPTY.withFormatting(Formatting.WHITE)).append(player.getDisplayName().copy()), true);
        return 1;
    }
}

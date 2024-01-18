package net.thomas24m.lifesteal.command.health;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class HealthGetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("health").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> run(context, EntityArgumentType.getPlayer(context, "player"))))));
    }

    private static int run(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
        double health = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).getBaseValue();
        context.getSource().sendFeedback(() -> player.getDisplayName().copy().append(Text.literal(" has a max health of %d".formatted((int) (health))).setStyle(Style.EMPTY.withFormatting(Formatting.WHITE))), true);
        return 1;
    }
}

package net.thomas24m.lifesteal.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.thomas24m.lifesteal.command.GiveHeartItemCommand;
import net.thomas24m.lifesteal.command.health.HealthAddCommand;
import net.thomas24m.lifesteal.command.health.HealthGetCommand;
import net.thomas24m.lifesteal.command.health.HealthSetCommand;
import net.thomas24m.lifesteal.command.WithdrawCommand;
import net.thomas24m.lifesteal.command.health.HealthSubtractCommand;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(WithdrawCommand::register);
        CommandRegistrationCallback.EVENT.register(HealthGetCommand::register);
        CommandRegistrationCallback.EVENT.register(HealthSetCommand::register);
        CommandRegistrationCallback.EVENT.register(HealthAddCommand::register);
        CommandRegistrationCallback.EVENT.register(HealthSubtractCommand::register);
        CommandRegistrationCallback.EVENT.register(GiveHeartItemCommand::register);
    }
}

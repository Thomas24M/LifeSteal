package net.thomas24m.lifesteal.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.thomas24m.lifesteal.command.WithdrawCommand;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(WithdrawCommand::register);
    }
}

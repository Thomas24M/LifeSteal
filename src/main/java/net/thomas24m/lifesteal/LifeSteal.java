package net.thomas24m.lifesteal;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.thomas24m.lifesteal.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifeSteal implements ModInitializer {
	public static final String MOD_ID = "lifesteal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Registering LifeSteal Mod!");

		ModRegistries.registerModStuff();
	}

	public static final GameRules.Key<GameRules.BooleanRule> PLAYERRELATEDONLY =
			GameRuleRegistry.register(MOD_ID + ":playerKillOnly", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

	public static final GameRules.Key<GameRules.BooleanRule> BANWHENMINHEALTH =
			GameRuleRegistry.register(MOD_ID + ":banWhenMinHealth", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

	public static final GameRules.Key<GameRules.IntRule> STEALAMOUNT =
			GameRuleRegistry.register(MOD_ID + ":stealAmount", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(2));

	public static final GameRules.Key<GameRules.IntRule> MINPLAYERHEALTH =
			GameRuleRegistry.register(MOD_ID + ":minPlayerHealth", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(2));

	public static final GameRules.Key<GameRules.IntRule> MAXPLAYERHEALTH =
			GameRuleRegistry.register(MOD_ID + ":maxPlayerHealth", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(40));
}
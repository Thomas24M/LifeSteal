package net.thomas24m.lifesteal;

import net.fabricmc.api.ModInitializer;

import net.thomas24m.lifesteal.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifeSteal implements ModInitializer {
	public static final String MOD_ID = "lifesteal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModRegistries.registerModStuff();
	}
}
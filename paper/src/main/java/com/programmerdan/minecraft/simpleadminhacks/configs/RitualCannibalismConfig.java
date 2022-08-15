package com.programmerdan.minecraft.simpleadminhacks.configs;

import com.programmerdan.minecraft.simpleadminhacks.SimpleAdminHacks;
import com.programmerdan.minecraft.simpleadminhacks.framework.SimpleHackConfig;
import org.bukkit.configuration.ConfigurationSection;

public class RitualCannibalismConfig extends SimpleHackConfig { {
	public RitualCannibalismConfig(SimpleAdminHacks plugin, ConfigurationSection base) {
		super(plugin, base);
	}

	public RitualCannibalismConfig(ConfigurationSection base) {
		super(SimpleAdminHacks.instance(), base);
	}
	
	@Override
	protected void wireup(ConfigurationSection config) {
	}
}

package me.pikod.eulacraft;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	
	private static Plugin plugin;
	private ConfigurationManager config;
	
	@Override
	public void onEnable() {
		plugin = this;
		config = new ConfigurationManager(this);
		new Event(this);
		new Command(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public ConfigurationManager getConfigurationManager() {
		return config;
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
}

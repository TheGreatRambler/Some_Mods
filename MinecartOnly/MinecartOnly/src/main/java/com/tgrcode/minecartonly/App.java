package com.tgrcode.minecartonly;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Start MinecartOnly");
		getServer().getPluginManager().registerEvents(new Listeners(getLogger()), this);
	}
	@Override
	public void onDisable() {
		getLogger().info("MinecartOnly closed");
	}
}
package com.tgrcode.minecartonly;

import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.Server.Spigot;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.material.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.*;
import org.bukkit.util.io.BukkitObjectInputStream;

public class App extends JavaPlugin implements Listener {
	private Block lastRailBlock     = null;
	private Block lastLastRailBlock = null;

	@Override
	public void onEnable() {
		getLogger().info("Start MinecartOnly");
		getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		getLogger().info("MinecartOnly closed");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// setPlayersHand(event.getPlayer().getInventory());

		org.bukkit.inventory.ItemStack minecarts = new org.bukkit.inventory.ItemStack(Material.MINECART, 64);
		event.getPlayer().getInventory().setItem(8, minecarts);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Block thisBlock = event.getTo().getBlock();

		if(lastRailBlock != null && lastLastRailBlock != null) {
			// TODO, automatically place rails underneath
		}

		lastLastRailBlock = lastRailBlock;
		lastRailBlock     = thisBlock;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//
	}
}
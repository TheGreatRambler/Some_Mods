package com.tgrcode.minecartonly;

import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Listeners implements Listener {
	private Logger logger;

	public Listeners(Logger log) {
		logger = log;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		setPlayersHand(event.getPlayer().getInventory());
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		setPlayersHand(event.getPlayer().getInventory());
	}

	private void setPlayersHand(PlayerInventory inventory) {
		logger.info("Fix inventory");
		inventory.setItem​(9, new ItemStack​(Material.MINECART, 64));
		inventory.setItem​(8, new ItemStack​(Material.POWERED_RAIL, 64));
		inventory.setItem​(7, new ItemStack​(Material.RAILS));
		inventory.setItem​(6, new ItemStack​(Material.REDSTONE_TORCH_ON, 64));
	}
}
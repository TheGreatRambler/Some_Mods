package com.tgrcode.minecartonly;

import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server.Spigot;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
	private Location lastLocation   = null;

	enum DirectionFacing {
		North,
		Northeast,
		East,
		Southeast,
		South,
		Southwest,
		West,
		Northwest,
	}

	enum PitchFacing {
		Up,
		Level,
		Down,
	}

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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		Location thisLocation = event.getPlayer().getLocation().subtract(0, 1, 0);
		Block thisBlock       = thisLocation.getBlock();

		// Determine if player has moved off block
		if(thisLocation != lastLocation) {
			Location playerLoc = event.getPlayer().getLocation();

			DirectionFacing directionFacing;
			int degrees = (Math.round(playerLoc.getYaw()) + 270) % 360;
			if(degrees <= 22)
				directionFacing = DirectionFacing.North;
			else if(degrees <= 67)
				directionFacing = DirectionFacing.Northeast;
			else if(degrees <= 112)
				directionFacing = DirectionFacing.East;
			else if(degrees <= 157)
				directionFacing = DirectionFacing.Southeast;
			else if(degrees <= 202)
				directionFacing = DirectionFacing.South;
			else if(degrees <= 247)
				directionFacing = DirectionFacing.Southwest;
			else if(degrees <= 292)
				directionFacing = DirectionFacing.West;
			else if(degrees <= 337)
				directionFacing = DirectionFacing.Northwest;
			else if(degrees <= 359)
				directionFacing = DirectionFacing.North;

			PitchFacing pitchFacing;
			float pitch = playerLoc.getPitch();
			if(pitch < -30.0)
				pitchFacing = PitchFacing.Up;
			else if(pitch < 30.0)
				pitchFacing = PitchFacing.Level;
			else if(pitch <= 90.0)
				pitchFacing = PitchFacing.Down;

			Location startingLoc         = null;
			Location additionalDirection = null;
			switch(directionFacing) {
			case North:
				startingLoc = playerLoc.add(0, 0, -1);
				break;
			case Northeast:
				startingLoc         = playerLoc.add(1, 0, -1);
				additionalDirection = playerLoc.add(0, 0, -1);
				break;
			case East:
				startingLoc = playerLoc.add(1, 0, 0);
				break;
			case Southeast:
				startingLoc = playerLoc.add(x, y, z);
				break;
			case South:
				startingLoc = playerLoc.add(x, y, z);
				break;
			case Southwest:
				startingLoc = playerLoc.add(x, y, z);
				break;
			case West:
				startingLoc = playerLoc.add(x, y, z);
				break;
			case Northwest:
				startingLoc = playerLoc.add(x, y, z);
				break;
			}
		}

		if(lastRailBlock != null && lastLastRailBlock != null) {
			// TODO, automatically place rails underneath
		}

		lastLastRailBlock = lastRailBlock;
		lastRailBlock     = thisBlock;
		lastLocation      = thisLocation;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//
	}
}
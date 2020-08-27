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
	private Location lastLocation = null;
	private boolean railTurnDir   = false;

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
		org.bukkit.inventory.ItemStack rails     = new org.bukkit.inventory.ItemStack(Material.RAIL, 64);
		event.getPlayer().getInventory().setItem(8, minecarts);
		event.getPlayer().getInventory().setItem(7, rails);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
			if(event.getPlayer().isInsideVehicle()) {
				Location thisLocation = event.getTo().getBlock().getLocation();
				Location playerLoc    = event.getPlayer().getLocation();
				boolean isShiftHeld   = event.getPlayer().isSneaking();

				if(isShiftHeld) {
					DirectionFacing directionFacing = null;
					int degrees                     = (Math.round(playerLoc.getYaw()) + 270) % 360;
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

					PitchFacing pitchFacing = null;
					float pitch             = playerLoc.getPitch();
					if(pitch < -30.0)
						pitchFacing = PitchFacing.Up;
					else if(pitch < 30.0)
						pitchFacing = PitchFacing.Level;
					else if(pitch <= 90.0)
						pitchFacing = PitchFacing.Down;

					Location startingLoc   = null;
					Location additionalLoc = null;
					boolean isTurn         = false;
					switch(directionFacing) {
					case North:
						startingLoc = thisLocation.add(0, 0, -1);
						break;
					case Northeast:
						startingLoc = thisLocation.add(1, 0, -1);
						if(railTurnDir) {
							additionalLoc = thisLocation.add(0, 0, -1);
						} else {
							additionalLoc = thisLocation.add(1, 0, 0);
						}
						isTurn = true;
						break;
					case East:
						startingLoc = thisLocation.add(1, 0, 0);
						break;
					case Southeast:
						startingLoc = thisLocation.add(1, 0, 1);
						if(railTurnDir) {
							additionalLoc = thisLocation.add(1, 0, 0);
						} else {
							additionalLoc = thisLocation.add(0, 0, 1);
						}
						isTurn = true;
						break;
					case South:
						startingLoc = thisLocation.add(0, 0, 1);
						break;
					case Southwest:
						startingLoc = thisLocation.add(-1, 0, 1);
						if(railTurnDir) {
							additionalLoc = thisLocation.add(0, 0, 1);
						} else {
							additionalLoc = thisLocation.add(-1, 0, 0);
						}
						isTurn = true;
						break;
					case West:
						startingLoc = thisLocation.add(-1, 0, 0);
						break;
					case Northwest:
						startingLoc = thisLocation.add(-1, 0, -1);
						if(railTurnDir) {
							additionalLoc = thisLocation.add(-1, 0, 0);
						} else {
							additionalLoc = thisLocation.add(0, 0, -1);
						}
						isTurn = true;
						break;
					default:
						break;
					}

					if(startingLoc != null) {
						switch(pitchFacing) {
						case Up:
							startingLoc = startingLoc.add(0, 1, 0);
							break;
						case Level:
							break;
						case Down:
							startingLoc = startingLoc.add(0, -1, 0);
							break;
						default:
							break;
						}
					}

					if(additionalLoc != null) {
						switch(pitchFacing) {
						case Up:
							additionalLoc = additionalLoc.add(0, 1, 0);
							break;
						case Level:
							break;
						case Down:
							additionalLoc = additionalLoc.add(0, -1, 0);
							break;
						default:
							break;
						}
					}

					if(isTurn) {
						additionalLoc.getBlock().setType(Material.RAIL);
					} else {
						additionalLoc.getBlock().setType(Material.REDSTONE_BLOCK);
						additionalLoc.add(0, 1, 0).getBlock().setType(Material.POWERED_RAIL);
					}
					startingLoc.add(0, 2, 0).getBlock().setType(Material.AIR);
					startingLoc.add(0, 3, 0).getBlock().setType(Material.AIR);

					additionalLoc.getBlock().setType(Material.REDSTONE_BLOCK);
					additionalLoc.add(0, 1, 0).getBlock().setType(Material.POWERED_RAIL);
					additionalLoc.add(0, 2, 0).getBlock().setType(Material.AIR);
					additionalLoc.add(0, 3, 0).getBlock().setType(Material.AIR);

					railTurnDir = !railTurnDir;
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//
	}
}
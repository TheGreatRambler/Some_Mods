package com.tgrcode.accelerateinfinitely;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.*;

public class App extends JavaPlugin implements Listener {
	private enum AccelType {
		FORWARDS,
		BACKWARDS,
		DISABLED,
	}

	private float initialSpeed    = 0.2f;
	private float speedMultiplier = 1.01f;

	private float currentSpeed   = initialSpeed;
	private AccelType type       = AccelType.FORWARDS;
	private int tickDelayAllowed = 5;
	private int currentTickDelay = 0;

	@Override
	public void onEnable() {
		getLogger().info("Accelerate Infinitely enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// getCommand("setaccel").setExecutor(this);
		// getCommand("setmultiplieraccel").setExecutor(this);

		Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				if(type != AccelType.DISABLED) {
					for(Player player : Bukkit.getServer().getOnlinePlayers()) {
						if(player.isOnGround() || !player.isSprinting()) {
							if(currentTickDelay < tickDelayAllowed) {
								currentTickDelay++;
							} else {
								currentSpeed = initialSpeed;
							}
						} else {
							if(type == AccelType.FORWARDS) {
								Vector targetVelocity = player.getLocation().getDirection().multiply(currentSpeed);
								// Don't modify the Y velocity
								targetVelocity.setY(player.getVelocity().getY());
								player.setVelocity(targetVelocity);
								currentSpeed *= speedMultiplier;
								currentTickDelay = 0;
							}
						}
					}
				}
			}
		}, 20L, 1L);
	}
	@Override
	public void onDisable() {
		getLogger().info("Accelerate Infinitely disabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("setaccel")) {
			if(label.equals("forwards")) {
				type = AccelType.FORWARDS;
			} else if(label.equals("backwards")) {
				type = AccelType.BACKWARDS;
			} else if(label.equals("disabled")) {
				type = AccelType.DISABLED;
			}
		} else if(command.getName().equals("setmultiplieraccel")) {
			speedMultiplier = Float.parseFloat(label);
		}

		return true;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		/*
		if(event.getPlayer().isSprinting()) {
			if(type == AccelType.FORWARDS) {
				Vector movementVec = event.getPlayer().getVelocity();
				event.getPlayer().setVelocity(movementVec.multiply(multiplier));
			}
		}
		*/
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setFlying(false);
	}

	private Vector yawToVector(float yaw) {
		// Pitch is always 0
		double pitchRadians = Math.toRadians(0);
		double yawRadians   = Math.toRadians(yaw);

		double sinPitch = Math.sin(pitchRadians);
		double cosPitch = Math.cos(pitchRadians);
		double sinYaw   = Math.sin(yawRadians);
		double cosYaw   = Math.cos(yawRadians);

		return new Vector(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
	}
}
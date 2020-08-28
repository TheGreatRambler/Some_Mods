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

	private float multiplier     = 1.1f;
	private AccelType type       = AccelType.FORWARDS;
	private Vector lastVelocity  = null;
	private boolean oneTickDelay = false;

	@Override
	public void onEnable() {
		getLogger().info("Accelerate Infinitely enabled");
		getServer().getPluginManager().registerEvents(this, this);

		// getCommand("setaccel").setExecutor(this);
		// getCommand("setmultiplieraccel").setExecutor(this);

		Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					if(player.isSprinting()) {
						if(player.isOnGround()) {
							if(oneTickDelay) {
								oneTickDelay = false;
							} else {
								lastVelocity = null;
							}
						} else {
							if(lastVelocity != null) {
								player.setVelocity(lastVelocity.multiply(multiplier));
							}
							lastVelocity = player.getVelocity();
							oneTickDelay = true;
						}
					} else {
						lastVelocity = null;
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
			multiplier = Float.parseFloat(label);
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
}
package io.github.flailofthelord.wss;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		JavaPlugin.getPlugin(WSS.class).setPlayers();
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		JavaPlugin.getPlugin(WSS.class).setPlayers();
	}

}

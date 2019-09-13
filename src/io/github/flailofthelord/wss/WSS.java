package io.github.flailofthelord.wss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class WSS extends JavaPlugin {

	public static Set<UUID> wssPlayers = new HashSet<>();
	BukkitScheduler scheduler;
	private boolean isPaused = true;

	@Override
	public void onEnable() {
		scheduler = getServer().getScheduler();
		saveDefaultConfig();

		getCommand("wss").setExecutor(this);
		getCommand("wss").setTabCompleter(this);

		getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		setPlayers();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		switch (args.length) {
		case 0:
			sender.sendMessage(" ");
			sender.sendMessage(chat("&a&lWackySkySurvival &7&nversion 1.0.0&r &2&oby FlailoftheLord"));
			sender.sendMessage(" ");
			sender.sendMessage(chat("&aCommand&8: &7/wss &8[&7start &8| &7stop &8| &7reload&8]"));
			sender.sendMessage(
					chat("&3Server Info&8: &7" + getServer().getName() + " &8|&7 " + getServer().getVersion()
							+ " &8|&7 " + getServer().getBukkitVersion()));
			sender.sendMessage(" ");

			break;
		default:
			switch (args[0].toLowerCase()) {
			case "stop":
				if (isPaused) {
					sender.sendMessage(chat("&6&lSkySurvival ItemGiver is already stopped! &8(&7/wss start&8)"));

					break;
				}

				stopTask();
				sender.sendMessage(chat("&a&lSkySurvival ItemGiver was Stopped successfully! &8(&7/wss start&8)"));
				break;
			case "start":
				if (!isPaused) {
					sender.sendMessage(chat("&e&lSkySurvival ItemGiver is already going, Silly! &8(&7/wss stop&8)"));

					break;
				}

				startTask(getConfig().getInt("item-give-interval", 30));
				sender.sendMessage(chat("&a&lSkySurvival ItemGiver has Started successfully. &8(&7/wss stop&8)"));
				break;
			case "reload":

				if (sender instanceof Player) {
					boolean senderIsAdmin = false;

					for (String admin : getConfig().getStringList("admins")) {

						if (((Player) sender).getName().equalsIgnoreCase(admin)) {
							senderIsAdmin = true;
							break;
						}
					}

					if (!senderIsAdmin) {
						sender.sendMessage(chat("&c&lYou don't have permission to reload WackySkySurvial!"));
						sender.sendMessage(chat("&7Contact an admin and ask for permission."));

						break;
					}
				}

				stopTask();
				saveDefaultConfig();
				reloadConfig();

				this.setPlayers();

				sender.sendMessage(chat("&a&lRELOADED SkySurvival settings!"));
				sender.sendMessage(chat("&c&lSkySurvival ItemGiver has been stopped for the reload. &8(&7/wss start&8)"));
				break;
			}

		}


		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length < 2) {

			list.add("reload");
			list.add("stop");
			list.add("start");
		}

		for (String s : list.toArray(new String[] {})) {
			if (!s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {

				list.remove(s);
			}
		}

		return list;
	}

	int startTask(int interval) {
		isPaused = false;

		return scheduler.scheduleSyncRepeatingTask(this, new ItemGiver(), 64, interval * 20);
	}

	void stopTask() {

		scheduler.getPendingTasks().clear();
		scheduler.cancelTasks(this);
		isPaused = true;
	}

	void setPlayers() {
		wssPlayers.clear();
		List<String> disabledPlayers = getConfig().getStringList("disabled-players");

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (disabledPlayers.contains(p.getName().toLowerCase())) {
				continue;
			}

			wssPlayers.add(p.getUniqueId());
		}

	}

	String chat(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}

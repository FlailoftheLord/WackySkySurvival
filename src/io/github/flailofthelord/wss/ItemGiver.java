package io.github.flailofthelord.wss;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemGiver implements Runnable {

	@Override
	public void run() {

		Material[] materials = Material.values();
		Random random = new Random();

		if (WSS.wssPlayers.isEmpty()) {
			return;
		}

		for (UUID uuid : WSS.wssPlayers) {
			int rnd = random.nextInt(materials.length);

			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				continue;
			}
			PlayerInventory inv = player.getInventory();

			ItemStack item = new ItemStack(materials[rnd]);

			if (inv.firstEmpty() < 0) {

				player.getWorld().dropItem(player.getLocation(), item);
				continue;
			}

			inv.addItem(item);
		}

	}

}

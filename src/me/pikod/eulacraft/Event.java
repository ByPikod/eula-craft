package me.pikod.eulacraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Event implements Listener {
	
	Plugin pl;
	ConfigurationManager cm;
	YamlConfiguration config;
	
	public Event(Plugin plugin) {
		pl = plugin;
		cm = pl.getConfigurationManager();
		config = cm.getSettings();
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(event.getTo().getX() == event.getPlayer().getLocation().getX() && event.getTo().getY() == event.getPlayer().getLocation().getY() && event.getTo().getZ() == event.getPlayer().getLocation().getZ()) return;
		if(event.isCancelled()) return;
		if(event.getPlayer().getOpenInventory() != null)
		if(event.getPlayer().getOpenInventory().getTitle().equals(Lang.color(cm.getSettings().getString("title")))) return;
		if(!pl.getConfigurationManager().getUserData().isSet(event.getPlayer().getName())) {
			if(!cm.getSettings().getBoolean("enabled")) return;
			openInventory(event.getPlayer());
		}
	}
	
	public void openInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, Lang.color(cm.getSettings().getString("title")));
		ItemStack item;
		ItemMeta meta;

		item = new ItemStack(Material.getMaterial(config.getString("eula.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(config.getString("eula.text")));
		item.setItemMeta(meta);
		inv.setItem(config.getInt("eula.slot"), item);
		
		item = new ItemStack(Material.getMaterial(config.getString("accept.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(config.getString("accept.text")));
		item.setItemMeta(meta);
		inv.setItem(config.getInt("accept.slot"), item);
		
		item = new ItemStack(Material.getMaterial(config.getString("reject.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(config.getString("reject.text")));
		item.setItemMeta(meta);
		inv.setItem(config.getInt("reject.slot"), item);
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onGui(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			if(event.getClickedInventory() == null) return;
			if(event.getClickedInventory().getTitle() == null) return;
			if(event.getClickedInventory().getTitle().equals(Lang.color(cm.getSettings().getString("title")))) {
				event.setCancelled(true);
				if(event.getSlotType() != SlotType.CONTAINER) return;
				if(event.getCurrentItem().getType() == Material.AIR) return;
				if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.color(cm.getSettings().getString("accept.text")))) {
					if(!cm.getSettings().getString("accepted-message").equals("none")) event.getWhoClicked().sendMessage(Lang.color(cm.getSettings().getString("accepted-message")));
					cm.getUserData().set(event.getWhoClicked().getName(), true);
					cm.saveUserData();
					event.getWhoClicked().closeInventory();
				}
				if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.color(cm.getSettings().getString("reject.text")))) {
					event.getWhoClicked().closeInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getInventory().getTitle().equals(Lang.color(cm.getSettings().getString("title")))) {
			if(!cm.getUserData().isSet(event.getPlayer().getName())) {
				if(cm.getSettings().getString("on-reject").equals("kick")) {
					Player player = (Player) event.getPlayer();
					player.kickPlayer(Lang.color(cm.getSettings().getString("kick-message")));
				}
			}
		}
	}
}

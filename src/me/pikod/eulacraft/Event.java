package me.pikod.eulacraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
	
	public Event(Plugin plugin) {
		pl = plugin;
		cm = pl.getConfigurationManager();
		
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

		item = new ItemStack(Material.getMaterial(cm.getSettings().getString("eula.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(cm.getSettings().getString("eula.text")));
		List<String> list = new ArrayList<String>();
		for(String key : cm.getSettings().getStringList("eula.lore")) {
			list.add(Lang.color(key));
		}
		meta.setLore(list);
		item.setItemMeta(meta);
		if(cm.getSettings().getBoolean("eula.enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		inv.setItem(cm.getSettings().getInt("eula.slot"), item);
		
		item = new ItemStack(Material.getMaterial(cm.getSettings().getString("accept.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(cm.getSettings().getString("accept.text")));
		item.setItemMeta(meta);
		if(cm.getSettings().getBoolean("accept.enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		inv.setItem(cm.getSettings().getInt("accept.slot"), item);
		
		item = new ItemStack(Material.getMaterial(cm.getSettings().getString("reject.item")));
		meta = item.getItemMeta();
		meta.setDisplayName(Lang.color(cm.getSettings().getString("reject.text")));
		item.setItemMeta(meta);
		if(cm.getSettings().getBoolean("reject.enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		inv.setItem(cm.getSettings().getInt("reject.slot"), item);
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onGui(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			if(event.getClickedInventory() == null) return;
			if(event.getView().getTitle() == null) return;
			if(event.getView().getTitle().equals(Lang.color(cm.getSettings().getString("title")))) {
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
		if(event.getView().getTitle().equals(Lang.color(cm.getSettings().getString("title")))) {
			if(!cm.getUserData().isSet(event.getPlayer().getName())) {
				if(cm.getSettings().getString("on-reject").equals("kick")) {
					Player player = (Player) event.getPlayer();
					player.kickPlayer(Lang.color(cm.getSettings().getString("kick-message")));
				}
			}
		}
	}
}

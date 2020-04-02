package me.pikod.eulacraft;

import net.md_5.bungee.api.ChatColor;

public class Lang {
	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}

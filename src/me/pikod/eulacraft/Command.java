package me.pikod.eulacraft;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

	public Command(Plugin plugin) {
		plugin.getCommand("eulacraft").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if(args.length < 1) {
			sender.sendMessage(Lang.color("&aEulaCraft v0.1 installed! Use &e/ec reload &ato load configuration"));
			return true;
		}
		if(!args[0].equals("reload")) {
			sender.sendMessage(Lang.color("&aEulaCraft v0.1 installed! Use &e/ec reload &ato load configuration"));
			return true;
		}
		
		Plugin.getInstance().getConfigurationManager().reloadConfig();
		sender.sendMessage(Lang.color("&aSuccess, reloaded &e&lEulaCraft v0.1 &aconfiguration!"));
		
		return true;
	}

}

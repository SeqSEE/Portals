package me.petterim1.portals;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.utils.TextFormat;

public class CommandHandler {

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName()) {
			case "createportal":
			case "newportal":
			case "addportal":
				if (sender instanceof ConsoleCommandSender) {
	                sender.sendMessage(TextFormat.DARK_RED + "This command is only for players");
	                break;
	            }
				if(!sender.hasPermission("portals.create")){
					sender.sendMessage(TextFormat.DARK_RED + "This command is only for players");
					break;
				}
				break;
			case "removeportal":
			case "remportal":
			case "deleteportal":
			case "delportal":
				if (sender instanceof ConsoleCommandSender) {
	                sender.sendMessage(TextFormat.DARK_RED + "This command is only for players");
	                break;
	            }
				if(!sender.hasPermission("portals.remove")){
					sender.sendMessage(TextFormat.DARK_RED + "This command is only for players");
					break;
				}
				break;
			default:
				break;
		}
		return false;
	}

	

}

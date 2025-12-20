package org.eu.mawtalk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    private final MawTalk plugin;

    public HelpCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "MawTalk Commands:");
        sender.sendMessage(ChatColor.GRAY + "/msg <player> <message>  " + ChatColor.WHITE + "Send private message (alias /w)");
        sender.sendMessage(ChatColor.GRAY + "/reply <message>  " + ChatColor.WHITE + "Reply to last private message (alias /r)");
        sender.sendMessage(ChatColor.GRAY + "/ignore <player>  " + ChatColor.WHITE + "Ignore a player (private & public chat)");
        sender.sendMessage(ChatColor.GRAY + "/unignore <player>  " + ChatColor.WHITE + "Stop ignoring a player");
        sender.sendMessage(ChatColor.GRAY + "/ignorelist  " + ChatColor.WHITE + "List players you are ignoring");
        return true;
    }
}

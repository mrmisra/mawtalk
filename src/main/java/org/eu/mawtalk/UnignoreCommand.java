package org.eu.mawtalk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

public class UnignoreCommand implements CommandExecutor {
    private final MawTalk plugin;

    public UnignoreCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unignore <player>");
            return true;
        }

        Player p = (Player) sender;
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Player not found or not online.");
            return true;
        }

        Set<java.util.UUID> set = plugin.getIgnored().get(p.getUniqueId());
        if (set == null || !set.remove(target.getUniqueId())) {
            p.sendMessage(ChatColor.YELLOW + "You are not ignoring " + target.getName());
            return true;
        }

        p.sendMessage(ChatColor.GREEN + "You have stopped ignoring " + target.getName());
        return true;
    }
}

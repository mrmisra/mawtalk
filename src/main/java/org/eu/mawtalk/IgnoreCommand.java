package org.eu.mawtalk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IgnoreCommand implements CommandExecutor {
    private final MawTalk plugin;

    public IgnoreCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /ignore <player>");
            return true;
        }

        Player p = (Player) sender;
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Player not found or not online.");
            return true;
        }

        Set<java.util.UUID> set = plugin.getIgnored().computeIfAbsent(p.getUniqueId(), k -> Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>()));
        if (set.contains(target.getUniqueId())) {
            p.sendMessage(ChatColor.YELLOW + "You are already ignoring " + target.getName());
            return true;
        }
        set.add(target.getUniqueId());
        p.sendMessage(ChatColor.GREEN + "You are now ignoring " + target.getName());
        return true;
    }
}

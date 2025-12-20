package org.eu.mawtalk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class ReplyCommand implements CommandExecutor {
    private final MawTalk plugin;

    public ReplyCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /reply <message>");
            return true;
        }

        Player pSender = (Player) sender;
        UUID last = plugin.getLastMessager().get(pSender.getUniqueId());
        if (last == null) {
            pSender.sendMessage(ChatColor.RED + "No one has messaged you recently.");
            return true;
        }

        Player target = plugin.getServer().getPlayer(last);
        if (target == null || !target.isOnline()) {
            pSender.sendMessage(ChatColor.RED + "Player not found or not online.");
            return true;
        }

        String message = String.join(" ", args);
        // If the recipient is ignoring the sender, don't deliver
        Set<UUID> recipientIgnored = plugin.getIgnored().get(target.getUniqueId());
        if (recipientIgnored != null && recipientIgnored.contains(pSender.getUniqueId())) {
            pSender.sendMessage(ChatColor.RED + "That player is ignoring you.");
            return true;
        }

        pSender.sendMessage(ChatColor.GRAY + "To " + ChatColor.WHITE + target.getName() + ChatColor.GRAY + ": " + ChatColor.LIGHT_PURPLE + message);
        target.sendMessage(ChatColor.GRAY + "From " + ChatColor.WHITE + pSender.getName() + ChatColor.GRAY + ": " + ChatColor.LIGHT_PURPLE + message);

        plugin.getLastMessager().put(target.getUniqueId(), pSender.getUniqueId());
        return true;
    }
}

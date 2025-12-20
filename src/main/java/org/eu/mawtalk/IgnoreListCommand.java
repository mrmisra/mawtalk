package org.eu.mawtalk;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class IgnoreListCommand implements CommandExecutor {
    private final MawTalk plugin;

    public IgnoreListCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        Player p = (Player) sender;
        Set<UUID> set = plugin.getIgnored().get(p.getUniqueId());
        if (set == null || set.isEmpty()) {
            p.sendMessage(ChatColor.YELLOW + "You are not ignoring anyone.");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (UUID u : set) {
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(u);
            String name = (op != null && op.getName() != null) ? op.getName() : u.toString();
            if (sb.length() > 0) sb.append(", ");
            sb.append(name);
        }

        p.sendMessage(ChatColor.GRAY + "Ignored players: " + ChatColor.WHITE + sb.toString());
        return true;
    }
}

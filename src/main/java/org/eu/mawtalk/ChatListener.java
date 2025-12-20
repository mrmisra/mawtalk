package org.eu.mawtalk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatListener implements Listener {
    private final MawTalk plugin;

    public ChatListener(MawTalk plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final java.util.UUID senderId = event.getPlayer().getUniqueId();
        // If player has party chat toggled, route message only to party members
        PartyManager pm = plugin.getPartyManager();
        if (pm != null && pm.isChatEnabled(senderId)) {
            event.setCancelled(true);
            Party party = pm.getPartyByPlayer(senderId);
            if (party == null) return;
            String msg = event.getMessage();
            org.bukkit.entity.Player sender = event.getPlayer();
            String formatted = org.bukkit.ChatColor.GREEN + "[Party] " + org.bukkit.ChatColor.GRAY + sender.getName() + org.bukkit.ChatColor.GRAY + ": " + org.bukkit.ChatColor.LIGHT_PURPLE + msg;
            for (java.util.UUID memberId : party.getMembers()) {
                org.bukkit.entity.Player member = plugin.getServer().getPlayer(memberId);
                if (member == null || !member.isOnline()) continue;
                Set<java.util.UUID> ignored = plugin.getIgnored().get(member.getUniqueId());
                if (ignored != null && ignored.contains(senderId)) continue;
                member.sendMessage(formatted);
            }
            return;
        }
        // remove recipients who have ignored the sender
        event.getRecipients().removeIf(recipient -> {
            Set<java.util.UUID> ignored = plugin.getIgnored().get(recipient.getUniqueId());
            return ignored != null && ignored.contains(senderId);
        });
    }
}

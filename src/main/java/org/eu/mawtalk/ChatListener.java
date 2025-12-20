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
        // remove recipients who have ignored the sender
        event.getRecipients().removeIf(recipient -> {
            Set<java.util.UUID> ignored = plugin.getIgnored().get(recipient.getUniqueId());
            return ignored != null && ignored.contains(senderId);
        });
    }
}

package org.eu.mawtalk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyCommand implements CommandExecutor {
    private final MawTalk plugin;

    public PartyCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if (args.length == 0) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Party commands:");
            p.sendMessage(ChatColor.GRAY + "/party create");
            p.sendMessage(ChatColor.GRAY + "/party invite <player>");
            p.sendMessage(ChatColor.GRAY + "/party accept");
            p.sendMessage(ChatColor.GRAY + "/party leave");
            p.sendMessage(ChatColor.GRAY + "/party chat  (toggle party chat)");
            return true;
        }

        String sub = args[0].toLowerCase();
        PartyManager pm = plugin.getPartyManager();

        switch (sub) {
            case "create":
                if (pm.getPartyByPlayer(uuid) != null) {
                    p.sendMessage(ChatColor.RED + "You are already in a party.");
                    return true;
                }
                Party created = pm.createParty(uuid);
                if (created != null) {
                    p.sendMessage(ChatColor.GREEN + "Party created. Use /party invite <player> to invite.");
                } else {
                    p.sendMessage(ChatColor.RED + "Could not create party.");
                }
                return true;
            case "invite":
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Usage: /party invite <player>");
                    return true;
                }
                Party party = pm.getPartyByPlayer(uuid);
                if (party == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party. Create one with /party create.");
                    return true;
                }
                if (!party.getLeader().equals(uuid)) {
                    p.sendMessage(ChatColor.RED + "Only the party leader may invite.");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    p.sendMessage(ChatColor.RED + "Player not found or not online.");
                    return true;
                }
                if (pm.invite(party.getId(), target.getUniqueId())) {
                    p.sendMessage(ChatColor.GREEN + "Invitation sent to " + target.getName());
                    target.sendMessage(ChatColor.GRAY + "You have been invited to a party by " + ChatColor.WHITE + p.getName());
                    target.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/party accept" + ChatColor.GRAY + " to join.");
                } else {
                    p.sendMessage(ChatColor.RED + "Could not invite that player (maybe already in a party).");
                }
                return true;
            case "accept":
                UUID invited = pm.getInvitedParty(uuid);
                if (invited == null) {
                    p.sendMessage(ChatColor.RED + "You have no pending party invites.");
                    return true;
                }
                Party joined = pm.acceptInvite(uuid);
                if (joined == null) {
                    p.sendMessage(ChatColor.RED + "Could not join party.");
                    return true;
                }
                for (UUID memberId : joined.getMembers()) {
                    Player member = Bukkit.getPlayer(memberId);
                    if (member != null && member.isOnline()) {
                        member.sendMessage(ChatColor.GREEN + p.getName() + " has joined the party.");
                    }
                }
                return true;
            case "leave":
                Party cur = pm.getPartyByPlayer(uuid);
                if (cur == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party.");
                    return true;
                }
                boolean wasLeader = cur.getLeader().equals(uuid);
                pm.leave(uuid);
                if (wasLeader) {
                    // notify remaining members that leader left or party promoted
                    Party now = pm.getPartyByPlayer(uuid); // should be null
                    // notify all members of the old party id by iterating members of cur
                    for (UUID memberId : cur.getMembers()) {
                        Player member = Bukkit.getPlayer(memberId);
                        if (member != null && member.isOnline()) {
                            member.sendMessage(ChatColor.YELLOW + "Leader has left the party.");
                        }
                    }
                }
                p.sendMessage(ChatColor.GRAY + "You left the party.");
                return true;
            case "chat":
                Party my = pm.getPartyByPlayer(uuid);
                if (my == null) {
                    p.sendMessage(ChatColor.RED + "You are not in a party.");
                    return true;
                }
                boolean now = pm.toggleChat(uuid);
                if (now) p.sendMessage(ChatColor.GREEN + "Party chat enabled. Your messages will go to the party.");
                else p.sendMessage(ChatColor.YELLOW + "Party chat disabled. Your messages will be public.");
                return true;
            default:
                p.sendMessage(ChatColor.RED + "Unknown subcommand. Use /party for help.");
                return true;
        }
    }
}

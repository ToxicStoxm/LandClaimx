package com.x_tornado10.landclaimx.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommandTabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {

            String word = args[0];

            List<String> claimargs = new ArrayList<>();

            if (player.hasPermission("landclaimx.claim.owner")) {

                claimargs.add("owner");

            }

            if (player.hasPermission("landclaimx.claim.overwrite")) {

                claimargs.add("overwrite");

            }

            if (player.hasPermission("landclaimx.claim.remove")) {

                claimargs.add("remove");

            }

            if (player.hasPermission("landclaimx.claim.clearall")) {

                claimargs.add("clear");

            }

            claimargs.add("info");
            claimargs.add("help");

            return claimargs;

        }

        if (args.length == 2) {

            String word = args[0];
            String word2 = args[1];

            List<String> claimargs = new ArrayList<>();

            if (player.hasPermission("landclaimx.claim.clear")) {

                if (word.equals("clear")) {

                    claimargs.add("confirm");

                }

            }

            return claimargs;

        }

        return null;
    }
}

package com.x_tornado10.landclaimx.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommandTabCompletion implements TabCompleter {
    //Tabcomplete for the '/claim' command
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        //defining the player
        Player player = (Player) sender;

        //if the cursor is at the position for the first argument
        if (args.length == 1) {

            String word = args[0];

            //Defining a new list to hold all the arguments to display
            List<String> claimargs = new ArrayList<>();

            //Permission check
            if (player.hasPermission("landclaimx.claim.owner")) {

                //add 'owner' to list
                claimargs.add("owner");

            }

            //Permission check
            if (player.hasPermission("landclaimx.claim.overwrite")) {

                //add 'overwrite' to list
                claimargs.add("overwrite");

            }

            //Permission check
            if (player.hasPermission("landclaimx.claim.remove")) {

                //add 'remove' to list
                claimargs.add("remove");

            }

            //Permission check
            if (player.hasPermission("landclaimx.claim.clearall")) {

                //add 'clear' to list
                claimargs.add("clear");

            }

            //add 'info' to list
            claimargs.add("info");
            //add 'help' to list
            claimargs.add("help");

            //return the list holding all possible arguments for the first 'slot'
            return claimargs;

        }

        //if the cursor is at the position for the second argument
        if (args.length == 2) {

            String word = args[0];
            String word2 = args[1];

            //Defining a new list to hold all the arguments to display
            List<String> claimargs = new ArrayList<>();

            //Permission check
            if (player.hasPermission("landclaimx.claim.clear")) {

                //if the first argument equals 'clear'
                if (word.equals("clear")) {

                    //add 'confirm' to list
                    claimargs.add("confirm");

                }

            }

            //return the list holding all possible arguments for the second 'slot'
            return claimargs;

        }

        return null;
    }
}

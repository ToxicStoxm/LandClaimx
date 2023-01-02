package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommandTabCompletion implements TabCompleter {
    private final LandClaimX plugin;

    public ClaimCommandTabCompletion(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

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
            if (player.hasPermission(plugin.perms_owner)) {

                //add 'owner' to list
                claimargs.add("owner");

            }

            //Permission check
            if (player.hasPermission(plugin.perms_overwrite)) {

                //add 'overwrite' to list
                claimargs.add("overwrite");

            }

            //Permission check
            if (player.hasPermission(plugin.perms_remove)) {

                //add 'remove' to list
                claimargs.add("remove");

            }

            //Permission check
            if (player.hasPermission(plugin.perms_clear)) {

                //add 'clear' to list
                claimargs.add("clear");

            }

            if (player.hasPermission(plugin.perms_radius)) {

                claimargs.add("radius");

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
            if (player.hasPermission(plugin.perms_clear)) {

                //if the first argument equals 'clear'
                if (word.equals("clear")) {

                    //add 'confirm' to list
                    claimargs.add("confirm");

                }

            }

            if (player.hasPermission(plugin.perms_radius)) {

                claimargs.add("1");
                claimargs.add("2");
                claimargs.add("3");
                claimargs.add("4");
                claimargs.add("5");
                claimargs.add("6");
                claimargs.add("7");
                claimargs.add("8");

            }

            //return the list holding all possible arguments for the second 'slot'
            return claimargs;

        }

        return null;
    }
}

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

        String perms_claim = "landclaimx.claim";
        String perms_owner = "landclaimx.claim.owner";
        String perms_overwrite = "landclaimx.claim.overwrite";
        String perms_clear = "landclaimx.claim.clear";
        String perms_radius = "landclaimx.claim.radius";
        String perms_remove = "landclaimx.claim.remove";
        String perms_remove_other = "landclaimx.claim.remove.other";

        Player player = (Player) sender;

        if (args.length == 1) {

            ArrayList<String> claimargs = new ArrayList<>();

            if (perms(player, perms_owner)) {

                switch (args[0].toLowerCase()) {
                    case "", "o", "ow", "own", "owne" -> claimargs.add("owner");
                }

            }

            if (perms (player, perms_overwrite)) {

                switch (args[0].toLowerCase()) {
                    case "", "o", "ov", "ove", "over", "overw", "overwr", "overwri", "overwrit" ->
                            claimargs.add("overwrite");
                }

            }

            if (perms(player, perms_remove)) {

                switch (args[0].toLowerCase()) {

                    case "", "r", "re", "rem", "remo", "remov" ->
                        claimargs.add("remove");

                }

            }

            if (perms(player, perms_clear)) {

                switch (args[0].toLowerCase()) {

                    case "", "c", "cl", "cle", "clea" ->
                        claimargs.add("clear");

                }

            }

            if (perms(player, perms_radius)) {

                switch (args[0].toLowerCase()) {

                    case "", "r", "ra", "rad", "radi", "radiu" ->
                        claimargs.add("radius");

                }

            }

            switch (args[0].toLowerCase()) {

                case "", "h", "he", "hel" ->
                    claimargs.add("help");

            }

            switch (args[0].toLowerCase()) {

                case "", "i", "in", "inf" ->
                        claimargs.add("info");

            }


            return claimargs;

        }



        if (args.length == 2) {

            ArrayList<String> claimargs = new ArrayList<>();

            String arg = args[0];

            if (perms(player, perms_clear)) {

                if (arg.toLowerCase().equals("clear")) {

                switch (args[1].toLowerCase()) {

                    case "", "c", "co", "con", "conf", "confi", "confir" -> claimargs.add("confirm");
                }

                }

            }

            if (perms(player, perms_radius)) {

                if (arg.toLowerCase().equals("radius")) {

                    if (args[1].toLowerCase().equals("")) {
                        claimargs.add("1");
                        claimargs.add("2");
                        claimargs.add("3");
                        claimargs.add("4");
                        claimargs.add("5");
                        claimargs.add("6");
                        claimargs.add("7");
                        claimargs.add("8");
                    }

                    switch (args[1].toLowerCase()) {

                        case "", "o", "on", "one" ->
                            claimargs.add("1");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "t", "tw", "two" ->
                                claimargs.add("2");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "t", "th", "thr", "thre", "three" ->
                                claimargs.add("3");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "f", "fo", "fou", "four" ->
                                claimargs.add("4");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "f", "fi", "fiv", "five" ->
                                claimargs.add("5");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "s", "si", "six" ->
                                claimargs.add("6");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "s", "se", "sev", "seve", "seven" ->
                                claimargs.add("7");

                    }
                    switch (args[1].toLowerCase()) {

                        case "", "e", "ei", "eig", "eigh", "eight" ->
                                claimargs.add("8");

                    }


                }

            }


            return claimargs;

        }

        return null;

    }


    public boolean perms (Player player, String perm){

        return player.hasPermission(perm);

    }


    public boolean radius(String arg) {

        return arg.equals("radius");

    }

    public boolean clear(String arg) {

        return arg.equals("clear");

    }

    }

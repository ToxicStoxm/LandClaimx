package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClaimCommand implements CommandExecutor {

    private final LandClaimX plugin;

    public ClaimCommand(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

    //private boolean clear = false;
    //private boolean radius = false;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {

            //if the command isn't executed by a player
            sender.sendMessage(plugin.prefix + "Only Players can run this command!");
            return true;

        } else {

            //clear = false;
            //radius = false;

            Player player = (Player) sender;

            String perms_claim = "landclaimx.claim";

            if (perms(player, perms_claim)) {

                String perms_owner = "landclaimx.claim.owner";
                String perms_overwrite = "landclaimx.claim.overwrite";

                if (args.length == 0) {

                    Chunk chunk = player.getLocation().getChunk();

                    //defining a chunkID for every chunk
                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    //if the chunk is already claimed (isChunk function from main class)
                    if (plugin.isChunk(chunkID)) {

                        //permission check
                        if (perms(player, perms_overwrite)) {

                            //player feedback
                            alreadyclaimed(player, "Type '/claim overwrite' to overwrite the previous claim");

                            //permission check
                        } else if (perms(player, perms_owner)){

                            alreadyclaimed(player, "Type '/claim owner' to display the chunk's Owner");

                        }else {

                            //player feedback
                            alreadyclaimed(player, "You don't have the Permissions to overwrite the claim!");
                        }

                    } else {

                        //adds the current chunk to the HashMap 'chunks' (chunkID, UUID)
                        plugin.addChunk(chunkID, player.getUniqueId());

                        //player feedback
                        successclaim(player, false);


                    }

                }


                String perms_clear = "landclaimx.claim.clear";
                String perms_radius = "landclaimx.claim.radius";

                if (args.length >= 3) {

                    msg(plugin.prefix + "You provided too many arguments!", player);
                    return true;

                }

                if (args.length == 1) {

                    String perms_remove = "landclaimx.claim.remove";

                    switch (args[0].toLowerCase()) {

                        case "owner" -> {

                            if (perms(player, perms_owner)) {

                                Chunk chunk = player.getLocation().getChunk();

                                String chunkID = chunk.getX() + "," + chunk.getZ();

                                if (plugin.getOwner(chunkID) == null) {

                                    //player feedback
                                    isnotclaimed(player);

                                } else {

                                    //Putting information about the owner into strings
                                    UUID owneruuid = plugin.getOwner(chunkID);

                                    try {

                                        String ownername = Bukkit.getPlayer(owneruuid).getName();

                                        //player feedback
                                        msg(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername, player);


                                    } catch (NullPointerException e) {

                                        String ownername = Bukkit.getOfflinePlayer(owneruuid).getName();

                                        //player feedback
                                        msg(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername, player);
                                    }

                                }


                            } else {

                                noperms(player);

                            }
                        }

                        case "overwrite" -> {

                            if (perms(player, perms_overwrite)) {

                                Chunk chunk = player.getLocation().getChunk();

                                String chunkID = chunk.getX() + "," + chunk.getZ();

                                plugin.replaceChunk(chunkID, player.getUniqueId());

                                //player feedback
                                successclaim(player, false);

                            } else {

                                noperms(player);

                            }
                        }

                        case "remove" -> {

                            if (perms(player, perms_remove)) {

                                Chunk chunk = player.getLocation().getChunk();

                                String chunkID = chunk.getX() + "," + chunk.getZ();

                                //check if the chunk isn't claimed yet to prevent error
                                if (plugin.getOwner(chunkID) == null) {

                                    //player feedback
                                    isnotclaimed(player);

                                } else {

                                    //permission check
                                    String perms_remove_other = "landclaimx.claim.remove.other";

                                    if (player.getUniqueId().equals(plugin.getOwner(chunkID))) {

                                        plugin.removeChunk(chunkID, player.getUniqueId());
                                        msg(plugin.prefix + "Successfully unclaimed chunk!", player);

                                    } else if (perms(player, perms_remove_other)) {

                                        //removing chunk from HashMap 'chunks'
                                        plugin.removeChunk(chunkID, plugin.getOwner(chunkID));
                                        msg(plugin.prefix + "Successfully unclaimed chunk!", player);


                                    //check if the player tries to remove other players chunk claim without permission
                                    } else {

                                        //player feedback
                                        msg("You can not unclaim chunks, owned by other players!", player);

                                    }
                                }

                            } else {

                                noperms(player);

                            }
                        }

                        case "info" -> plinfo(player);

                        case "help" -> helpmenu(player);

                        case "clear" -> {

                            if (perms(player, perms_clear)) {

                                msg(plugin.prefix + "Are you sure that you want to delete all claimed chunks?", player);
                                msg(plugin.prefix + "Type '/claim clear confirm' to confirm", player);


                            } else {

                                noperms(player);

                            }
                        }

                        case "radius" -> {

                            if (perms(player, perms_radius)) {

                                msg(plugin.prefix + "Please specify a radius between 1 and 8!", player);

                            } else {

                                noperms(player);

                            }
                        }

                        case "default" -> invalargs(player);

                    }
                }


                if (args.length == 2) {

                    /*switch (args[0].toLowerCase()) {

                        case "clear" :

                            if (perms(player, perms_clear)) {

                                clear = true;

                            } else {

                                clear = false;

                            }

                        case "radius" :

                            if (perms(player, perms_radius)) {

                                radius = true;

                            } else {

                                radius = false;

                            }

                    }

                     */

                    switch (args[1].toLowerCase()) {

                        case "confirm" -> {

                            if (!args[0].toLowerCase().equals("clear")) {

                                break;

                            }

                            //if (clear) {

                                if (perms(player, perms_clear)) {

                                    msg(plugin.prefix + "Starting process...", player);

                                    //clearing the HashMap 'chunks'
                                    plugin.clearChunks();
                                    //clearing the save file claims.yml
                                    plugin.clearFile();

                                    //player feedback
                                    msg(plugin.prefix + "Process complete!", player);
                                    msg(plugin.prefix + "Successfully deleted all claimed chunks", player);
                                    //clear = false;

                                } else {

                                    noperms(player);

                                }

                            /*} else {

                                invalargs(player);

                            }

                             */

                        }

                        case "1", "2", "3", "4", "5", "6", "7", "8" -> {

                            if (!args[0].toLowerCase().equals("radius")) {

                                break;

                            }

                            //if (radius) {

                                if (perms(player, perms_radius)) {

                                    int radius = Integer.parseInt(args[1]);

                                    int chunkZ = player.getLocation().getChunk().getZ();
                                    int chunkX = player.getLocation().getChunk().getX();

                                    Bukkit.getLogger().info(plugin.consoleprefix + "Calculating chunks...");
                                    msg(plugin.prefix + "Calculating chunks...", player);

                                    int chunkXm1 = chunkX - 1;
                                    int chunkXp1 = chunkX + 1;

                                    int chunkZm1 = chunkZ - 1;
                                    int chunkZp1 = chunkZ + 1;


                                    int chunkXm2 = chunkX - 2;
                                    int chunkXp2 = chunkX + 2;

                                    int chunkZm2 = chunkZ - 2;
                                    int chunkZp2 = chunkZ + 2;


                                    int chunkXm3 = chunkX - 3;
                                    int chunkXp3 = chunkX + 3;

                                    int chunkZm3 = chunkZ - 3;
                                    int chunkZp3 = chunkZ + 3;


                                    int chunkXm4 = chunkX - 4;
                                    int chunkXp4 = chunkX + 4;

                                    int chunkZm4 = chunkZ - 4;
                                    int chunkZp4 = chunkZ + 4;


                                    int chunkXm5 = chunkX - 5;
                                    int chunkXp5 = chunkX + 5;

                                    int chunkZm5 = chunkZ - 5;
                                    int chunkZp5 = chunkZ + 5;


                                    int chunkXm6 = chunkX - 6;
                                    int chunkXp6 = chunkX + 6;

                                    int chunkZm6 = chunkZ - 6;
                                    int chunkZp6 = chunkZ + 6;


                                    int chunkXm7 = chunkX - 7;
                                    int chunkXp7 = chunkX + 7;

                                    int chunkZm7 = chunkZ - 7;
                                    int chunkZp7 = chunkZ + 7;

                                    int chunkXm8 = chunkX - 8;
                                    int chunkXp8 = chunkX + 8;

                                    int chunkZm8 = chunkZ - 8;
                                    int chunkZp8 = chunkZ + 8;

                                    msg(plugin.prefix + "Please wait!", player);


                                    List<String> chunkID = new ArrayList<>();


                                    //radius 1
                                    if (radius >= 1) {

                                        chunkID.add(chunkX + "," + chunkZ);
                                        chunkID.add(chunkXm1 + "," + chunkZ);
                                        chunkID.add(chunkXm1 + "," + chunkZp1);
                                        chunkID.add(chunkX + "," + chunkZp1);
                                        chunkID.add(chunkXp1 + "," + chunkZp1);
                                        chunkID.add(chunkXp1 + "," + chunkZ);
                                        chunkID.add(chunkXp1 + "," + chunkZm1);
                                        chunkID.add(chunkX + "," + chunkZm1);
                                        chunkID.add(chunkXm1 + "," + chunkZm1);

                                        //msg("Debug1", player);
                                    }

                                    //radius 2
                                    if (radius >= 2) {

                                        chunkID.add(chunkXm2 + "," + chunkZ);
                                        chunkID.add(chunkXm2 + "," + chunkZp1);
                                        chunkID.add(chunkXm2 + "," + chunkZp2);
                                        chunkID.add(chunkXm1 + "," + chunkZp2);
                                        chunkID.add(chunkX + "," + chunkZp2);
                                        chunkID.add(chunkXp1 + "," + chunkZp2);
                                        chunkID.add(chunkXp2 + "," + chunkZp2);
                                        chunkID.add(chunkXp2 + "," + chunkZp1);
                                        chunkID.add(chunkXp2 + "," + chunkZ);
                                        chunkID.add(chunkXp2 + "," + chunkZm1);
                                        chunkID.add(chunkXp2 + "," + chunkZm2);
                                        chunkID.add(chunkXp1 + "," + chunkZm2);
                                        chunkID.add(chunkX + "," + chunkZm2);
                                        chunkID.add(chunkXm1 + "," + chunkZm2);
                                        chunkID.add(chunkXm2 + "," + chunkZm2);
                                        chunkID.add(chunkXm2 + "," + chunkZm1);

                                        //msg("Debug2", player);

                                    }

                                    //radius 3
                                    if (radius >= 3) {

                                        chunkID.add(chunkXm3 + "," + chunkZ);
                                        chunkID.add(chunkXm3 + "," + chunkZp1);
                                        chunkID.add(chunkXm3 + "," + chunkZp2);
                                        chunkID.add(chunkXm3 + "," + chunkZp3);
                                        chunkID.add(chunkXm2 + "," + chunkZp3);
                                        chunkID.add(chunkXm1 + "," + chunkZp3);
                                        chunkID.add(chunkX + "," + chunkZp3);
                                        chunkID.add(chunkXp1 + "," + chunkZp3);
                                        chunkID.add(chunkXp2 + "," + chunkZp3);
                                        chunkID.add(chunkXp3 + "," + chunkZp3);
                                        chunkID.add(chunkXp3 + "," + chunkZp2);
                                        chunkID.add(chunkXp3 + "," + chunkZp1);
                                        chunkID.add(chunkXp3 + "," + chunkZ);
                                        chunkID.add(chunkXp3 + "," + chunkZm1);
                                        chunkID.add(chunkXp3 + "," + chunkZm2);
                                        chunkID.add(chunkXp3 + "," + chunkZm3);
                                        chunkID.add(chunkXp2 + "," + chunkZm3);
                                        chunkID.add(chunkXp1 + "," + chunkZm3);
                                        chunkID.add(chunkX + "," + chunkZm3);
                                        chunkID.add(chunkXm1 + "," + chunkZm3);
                                        chunkID.add(chunkXm2 + "," + chunkZm3);
                                        chunkID.add(chunkXm3 + "," + chunkZm3);
                                        chunkID.add(chunkXm3 + "," + chunkZm2);
                                        chunkID.add(chunkXm3 + "," + chunkZm1);

                                        //msg("Debug3", player);

                                    }


                                    //radius 4
                                    if (radius >= 4) {

                                        chunkID.add(chunkXm4 + "," + chunkZ);
                                        chunkID.add(chunkXm4 + "," + chunkZp1);
                                        chunkID.add(chunkXm4 + "," + chunkZp2);
                                        chunkID.add(chunkXm4 + "," + chunkZp3);
                                        chunkID.add(chunkXm4 + "," + chunkZp4);
                                        chunkID.add(chunkXm3 + "," + chunkZp4);
                                        chunkID.add(chunkXm2 + "," + chunkZp4);
                                        chunkID.add(chunkXm1 + "," + chunkZp4);
                                        chunkID.add(chunkX + "," + chunkZp4);
                                        chunkID.add(chunkXp1 + "," + chunkZp4);
                                        chunkID.add(chunkXp2 + "," + chunkZp4);
                                        chunkID.add(chunkXp3 + "," + chunkZp4);
                                        chunkID.add(chunkXp4 + "," + chunkZp4);
                                        chunkID.add(chunkXp4 + "," + chunkZp3);
                                        chunkID.add(chunkXp4 + "," + chunkZp2);
                                        chunkID.add(chunkXp4 + "," + chunkZp1);
                                        chunkID.add(chunkXp4 + "," + chunkZ);
                                        chunkID.add(chunkXp4 + "," + chunkZm1);
                                        chunkID.add(chunkXp4 + "," + chunkZm2);
                                        chunkID.add(chunkXp4 + "," + chunkZm3);
                                        chunkID.add(chunkXp4 + "," + chunkZm4);
                                        chunkID.add(chunkXp3 + "," + chunkZm4);
                                        chunkID.add(chunkXp2 + "," + chunkZm4);
                                        chunkID.add(chunkXp1 + "," + chunkZm4);
                                        chunkID.add(chunkX + "," + chunkZm4);
                                        chunkID.add(chunkXm1 + "," + chunkZm4);
                                        chunkID.add(chunkXm2 + "," + chunkZm4);
                                        chunkID.add(chunkXm3 + "," + chunkZm4);
                                        chunkID.add(chunkXm4 + "," + chunkZm4);
                                        chunkID.add(chunkXm4 + "," + chunkZm3);
                                        chunkID.add(chunkXm4 + "," + chunkZm2);
                                        chunkID.add(chunkXm4 + "," + chunkZm1);

                                        //msg("Debug4", player);

                                    }

                                    //radius 5
                                    if (radius >= 5) {

                                        chunkID.add(chunkXm5 + "," + chunkZ);
                                        chunkID.add(chunkXm5 + "," + chunkZp1);
                                        chunkID.add(chunkXm5 + "," + chunkZp2);
                                        chunkID.add(chunkXm5 + "," + chunkZp3);
                                        chunkID.add(chunkXm5 + "," + chunkZp4);
                                        chunkID.add(chunkXm5 + "," + chunkZp5);
                                        chunkID.add(chunkXm4 + "," + chunkZp5);
                                        chunkID.add(chunkXm3 + "," + chunkZp5);
                                        chunkID.add(chunkXm2 + "," + chunkZp5);
                                        chunkID.add(chunkXm1 + "," + chunkZp5);
                                        chunkID.add(chunkX + "," + chunkZp5);
                                        chunkID.add(chunkXp1 + "," + chunkZp5);
                                        chunkID.add(chunkXp2 + "," + chunkZp5);
                                        chunkID.add(chunkXp3 + "," + chunkZp5);
                                        chunkID.add(chunkXp4 + "," + chunkZp5);
                                        chunkID.add(chunkXp5 + "," + chunkZp5);
                                        chunkID.add(chunkXp5 + "," + chunkZp4);
                                        chunkID.add(chunkXp5 + "," + chunkZp3);
                                        chunkID.add(chunkXp5 + "," + chunkZp2);
                                        chunkID.add(chunkXp5 + "," + chunkZp1);
                                        chunkID.add(chunkXp5 + "," + chunkZ);
                                        chunkID.add(chunkXp5 + "," + chunkZm1);
                                        chunkID.add(chunkXp5 + "," + chunkZm2);
                                        chunkID.add(chunkXp5 + "," + chunkZm3);
                                        chunkID.add(chunkXp5 + "," + chunkZm4);
                                        chunkID.add(chunkXp5 + "," + chunkZm5);
                                        chunkID.add(chunkXp4 + "," + chunkZm5);
                                        chunkID.add(chunkXp3 + "," + chunkZm5);
                                        chunkID.add(chunkXp2 + "," + chunkZm5);
                                        chunkID.add(chunkXp1 + "," + chunkZm5);
                                        chunkID.add(chunkX + "," + chunkZm5);
                                        chunkID.add(chunkXm1 + "," + chunkZm5);
                                        chunkID.add(chunkXm2 + "," + chunkZm5);
                                        chunkID.add(chunkXm3 + "," + chunkZm5);
                                        chunkID.add(chunkXm4 + "," + chunkZm5);
                                        chunkID.add(chunkXm5 + "," + chunkZm5);
                                        chunkID.add(chunkXm5 + "," + chunkZm4);
                                        chunkID.add(chunkXm5 + "," + chunkZm3);
                                        chunkID.add(chunkXm5 + "," + chunkZm2);
                                        chunkID.add(chunkXm5 + "," + chunkZm1);

                                        //msg("Debug5", player);

                                    }

                                    //radius 6
                                    if (radius >= 6) {

                                        chunkID.add(chunkXm6 + "," + chunkZ);
                                        chunkID.add(chunkXm6 + "," + chunkZp1);
                                        chunkID.add(chunkXm6 + "," + chunkZp2);
                                        chunkID.add(chunkXm6 + "," + chunkZp3);
                                        chunkID.add(chunkXm6 + "," + chunkZp4);
                                        chunkID.add(chunkXm6 + "," + chunkZp5);
                                        chunkID.add(chunkXm6 + "," + chunkZp6);
                                        chunkID.add(chunkXm5 + "," + chunkZp6);
                                        chunkID.add(chunkXm4 + "," + chunkZp6);
                                        chunkID.add(chunkXm3 + "," + chunkZp6);
                                        chunkID.add(chunkXm2 + "," + chunkZp6);
                                        chunkID.add(chunkXm1 + "," + chunkZp6);
                                        chunkID.add(chunkX + "," + chunkZp6);
                                        chunkID.add(chunkXp1 + "," + chunkZp6);
                                        chunkID.add(chunkXp2 + "," + chunkZp6);
                                        chunkID.add(chunkXp3 + "," + chunkZp6);
                                        chunkID.add(chunkXp4 + "," + chunkZp6);
                                        chunkID.add(chunkXp5 + "," + chunkZp6);
                                        chunkID.add(chunkXp6 + "," + chunkZp6);
                                        chunkID.add(chunkXp6 + "," + chunkZp5);
                                        chunkID.add(chunkXp6 + "," + chunkZp4);
                                        chunkID.add(chunkXp6 + "," + chunkZp3);
                                        chunkID.add(chunkXp6 + "," + chunkZp2);
                                        chunkID.add(chunkXp6 + "," + chunkZp1);
                                        chunkID.add(chunkXp6 + "," + chunkZ);
                                        chunkID.add(chunkXp6 + "," + chunkZm1);
                                        chunkID.add(chunkXp6 + "," + chunkZm2);
                                        chunkID.add(chunkXp6 + "," + chunkZm3);
                                        chunkID.add(chunkXp6 + "," + chunkZm4);
                                        chunkID.add(chunkXp6 + "," + chunkZm5);
                                        chunkID.add(chunkXp6 + "," + chunkZm6);
                                        chunkID.add(chunkXp5 + "," + chunkZm6);
                                        chunkID.add(chunkXp4 + "," + chunkZm6);
                                        chunkID.add(chunkXp3 + "," + chunkZm6);
                                        chunkID.add(chunkXp2 + "," + chunkZm6);
                                        chunkID.add(chunkXp1 + "," + chunkZm6);
                                        chunkID.add(chunkX + "," + chunkZm6);
                                        chunkID.add(chunkXm1 + "," + chunkZm6);
                                        chunkID.add(chunkXm2 + "," + chunkZm6);
                                        chunkID.add(chunkXm3 + "," + chunkZm6);
                                        chunkID.add(chunkXm4 + "," + chunkZm6);
                                        chunkID.add(chunkXm5 + "," + chunkZm6);
                                        chunkID.add(chunkXm6 + "," + chunkZm6);
                                        chunkID.add(chunkXm6 + "," + chunkZm5);
                                        chunkID.add(chunkXm6 + "," + chunkZm4);
                                        chunkID.add(chunkXm6 + "," + chunkZm3);
                                        chunkID.add(chunkXm6 + "," + chunkZm2);
                                        chunkID.add(chunkXm6 + "," + chunkZm1);

                                        //msg("Debug6", player);

                                    }

                                    //radius 7
                                    if (radius >= 7) {

                                        chunkID.add(chunkXm7 + "," + chunkZp1);
                                        chunkID.add(chunkXm7 + "," + chunkZp2);
                                        chunkID.add(chunkXm7 + "," + chunkZp3);
                                        chunkID.add(chunkXm7 + "," + chunkZp4);
                                        chunkID.add(chunkXm7 + "," + chunkZp5);
                                        chunkID.add(chunkXm7 + "," + chunkZp6);
                                        chunkID.add(chunkXm7 + "," + chunkZp7);
                                        chunkID.add(chunkXm7 + "," + chunkZp7);
                                        chunkID.add(chunkXm6 + "," + chunkZp7);
                                        chunkID.add(chunkXm5 + "," + chunkZp7);
                                        chunkID.add(chunkXm4 + "," + chunkZp7);
                                        chunkID.add(chunkXm3 + "," + chunkZp7);
                                        chunkID.add(chunkXm2 + "," + chunkZp7);
                                        chunkID.add(chunkXm1 + "," + chunkZp7);
                                        chunkID.add(chunkX + "," + chunkZp7);
                                        chunkID.add(chunkXp1 + "," + chunkZp7);
                                        chunkID.add(chunkXp2 + "," + chunkZp7);
                                        chunkID.add(chunkXp3 + "," + chunkZp7);
                                        chunkID.add(chunkXp4 + "," + chunkZp7);
                                        chunkID.add(chunkXp5 + "," + chunkZp7);
                                        chunkID.add(chunkXp6 + "," + chunkZp7);
                                        chunkID.add(chunkXp7 + "," + chunkZp6);
                                        chunkID.add(chunkXp7 + "," + chunkZp5);
                                        chunkID.add(chunkXp7 + "," + chunkZp4);
                                        chunkID.add(chunkXp7 + "," + chunkZp3);
                                        chunkID.add(chunkXp7 + "," + chunkZp2);
                                        chunkID.add(chunkXp7 + "," + chunkZp1);
                                        chunkID.add(chunkXp7 + "," + chunkZ);
                                        chunkID.add(chunkXp7 + "," + chunkZm1);
                                        chunkID.add(chunkXp7 + "," + chunkZm2);
                                        chunkID.add(chunkXp7 + "," + chunkZm3);
                                        chunkID.add(chunkXp7 + "," + chunkZm4);
                                        chunkID.add(chunkXp7 + "," + chunkZm5);
                                        chunkID.add(chunkXp7 + "," + chunkZm6);
                                        chunkID.add(chunkXp7 + "," + chunkZm7);
                                        chunkID.add(chunkXp7 + "," + chunkZm7);
                                        chunkID.add(chunkXp6 + "," + chunkZm7);
                                        chunkID.add(chunkXp5 + "," + chunkZm7);
                                        chunkID.add(chunkXp4 + "," + chunkZm7);
                                        chunkID.add(chunkXp3 + "," + chunkZm7);
                                        chunkID.add(chunkXp2 + "," + chunkZm7);
                                        chunkID.add(chunkXp1 + "," + chunkZm7);
                                        chunkID.add(chunkX + "," + chunkZm7);
                                        chunkID.add(chunkXm1 + "," + chunkZm7);
                                        chunkID.add(chunkXm2 + "," + chunkZm7);
                                        chunkID.add(chunkXm3 + "," + chunkZm7);
                                        chunkID.add(chunkXm4 + "," + chunkZm7);
                                        chunkID.add(chunkXm5 + "," + chunkZm7);
                                        chunkID.add(chunkXm6 + "," + chunkZm7);
                                        chunkID.add(chunkXm7 + "," + chunkZm6);
                                        chunkID.add(chunkXm7 + "," + chunkZm5);
                                        chunkID.add(chunkXm7 + "," + chunkZm4);
                                        chunkID.add(chunkXm7 + "," + chunkZm3);
                                        chunkID.add(chunkXm7 + "," + chunkZm2);
                                        chunkID.add(chunkXm7 + "," + chunkZm1);
                                        chunkID.add(chunkXm7 + "," + chunkZ);

                                        //msg("Debug7", player);

                                    }

                                    //radius 8
                                    if (radius >= 8) {

                                        chunkID.add(chunkXm8 + "," + chunkZ);
                                        chunkID.add(chunkXm8 + "," + chunkZp1);
                                        chunkID.add(chunkXm8 + "," + chunkZp2);
                                        chunkID.add(chunkXm8 + "," + chunkZp3);
                                        chunkID.add(chunkXm8 + "," + chunkZp4);
                                        chunkID.add(chunkXm8 + "," + chunkZp5);
                                        chunkID.add(chunkXm8 + "," + chunkZp6);
                                        chunkID.add(chunkXm8 + "," + chunkZp7);
                                        chunkID.add(chunkXm8 + "," + chunkZp8);
                                        chunkID.add(chunkXm7 + "," + chunkZp8);
                                        chunkID.add(chunkXm6 + "," + chunkZp8);
                                        chunkID.add(chunkXm5 + "," + chunkZp8);
                                        chunkID.add(chunkXm4 + "," + chunkZp8);
                                        chunkID.add(chunkXm3 + "," + chunkZp8);
                                        chunkID.add(chunkXm2 + "," + chunkZp8);
                                        chunkID.add(chunkXm1 + "," + chunkZp8);
                                        chunkID.add(chunkX + "," + chunkZp8);
                                        chunkID.add(chunkXp1 + "," + chunkZp8);
                                        chunkID.add(chunkXp2 + "," + chunkZp8);
                                        chunkID.add(chunkXp3 + "," + chunkZp8);
                                        chunkID.add(chunkXp4 + "," + chunkZp8);
                                        chunkID.add(chunkXp5 + "," + chunkZp8);
                                        chunkID.add(chunkXp6 + "," + chunkZp8);
                                        chunkID.add(chunkXp7 + "," + chunkZp8);
                                        chunkID.add(chunkXp8 + "," + chunkZp8);
                                        chunkID.add(chunkXp8 + "," + chunkZp7);
                                        chunkID.add(chunkXp8 + "," + chunkZp6);
                                        chunkID.add(chunkXp8 + "," + chunkZp5);
                                        chunkID.add(chunkXp8 + "," + chunkZp4);
                                        chunkID.add(chunkXp8 + "," + chunkZp3);
                                        chunkID.add(chunkXp8 + "," + chunkZp2);
                                        chunkID.add(chunkXp8 + "," + chunkZp1);
                                        chunkID.add(chunkXp8 + "," + chunkZ);
                                        chunkID.add(chunkXp8 + "," + chunkZm1);
                                        chunkID.add(chunkXp8 + "," + chunkZm2);
                                        chunkID.add(chunkXp8 + "," + chunkZm3);
                                        chunkID.add(chunkXp8 + "," + chunkZm4);
                                        chunkID.add(chunkXp8 + "," + chunkZm5);
                                        chunkID.add(chunkXp8 + "," + chunkZm6);
                                        chunkID.add(chunkXp8 + "," + chunkZm7);
                                        chunkID.add(chunkXp8 + "," + chunkZm8);
                                        chunkID.add(chunkXp7 + "," + chunkZm8);
                                        chunkID.add(chunkXp6 + "," + chunkZm8);
                                        chunkID.add(chunkXp5 + "," + chunkZm8);
                                        chunkID.add(chunkXp4 + "," + chunkZm8);
                                        chunkID.add(chunkXp3 + "," + chunkZm8);
                                        chunkID.add(chunkXp2 + "," + chunkZm8);
                                        chunkID.add(chunkXp1 + "," + chunkZm8);
                                        chunkID.add(chunkX + "," + chunkZm8);
                                        chunkID.add(chunkXm1 + "," + chunkZm8);
                                        chunkID.add(chunkXm2 + "," + chunkZm8);
                                        chunkID.add(chunkXm3 + "," + chunkZm8);
                                        chunkID.add(chunkXm4 + "," + chunkZm8);
                                        chunkID.add(chunkXm5 + "," + chunkZm8);
                                        chunkID.add(chunkXm6 + "," + chunkZm8);
                                        chunkID.add(chunkXm7 + "," + chunkZm8);
                                        chunkID.add(chunkXm8 + "," + chunkZm8);
                                        chunkID.add(chunkXm8 + "," + chunkZm7);
                                        chunkID.add(chunkXm8 + "," + chunkZm6);
                                        chunkID.add(chunkXm8 + "," + chunkZm5);
                                        chunkID.add(chunkXm8 + "," + chunkZm4);
                                        chunkID.add(chunkXm8 + "," + chunkZm3);
                                        chunkID.add(chunkXm8 + "," + chunkZm2);
                                        chunkID.add(chunkXm8 + "," + chunkZm1);

                                        //msg("Debug8", player);

                                    }


                                    Bukkit.getLogger().info(plugin.consoleprefix + "Done!");

                                    UUID owner = player.getUniqueId();

                                    int i = chunkID.size() - 1;

                                    ArrayList<Integer> ochunks = new ArrayList<>();
                                    List<UUID> u = new ArrayList<>();

                                    while (i > -1) {

                                        if (!plugin.isChunk(chunkID.get(i))) {

                                            plugin.addChunk(chunkID.get(i), owner);

                                        } else {

                                            if (!plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {

                                                if (player.hasPermission(plugin.perms_overwrite)) {

                                                    plugin.addChunk(chunkID.get(i), owner);

                                                }

                                                ochunks.add(i);
                                                u.add(plugin.getOwner(chunkID.get(i)));

                                            }
                                        }


                                        if (i == 0) {

                                            List<String> owners = new ArrayList<>();

                                            int in = u.size() - 1;


                                            while (in > -1) {

                                                try {

                                                    if (!owners.contains(Bukkit.getPlayer(u.get(in)).getName())) {

                                                        owners.add(Objects.requireNonNull(Bukkit.getPlayer(u.get(in))).getName());

                                                    }

                                                } catch (Exception e) {

                                                    if (!owners.contains(Bukkit.getOfflinePlayer(u.get(in)).getName())) {

                                                        owners.add(Bukkit.getOfflinePlayer(u.get(in)).getName());

                                                    }

                                                }

                                                in = in - 1;

                                            }

                                            //if (!plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {

                                            if (!ochunks.isEmpty()) {

                                                if (player.hasPermission(plugin.perms_overwrite)) {

                                                    msg(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Overwriting because needed permissions are given...", player);
                                                    successclaim(player, true);

                                                } else {

                                                    msg(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Reduce the radius of the command or type '/claim help' for more info!", player);

                                                }

                                            } else {

                                                successclaim(player, true);

                                            }


                                            u.clear();
                                            owners.clear();
                                            ochunks.clear();
                                            chunkID.clear();
                                            //this.radius = false;

                                        }


                                        i = i - 1;

                                    }


                                } else {

                                    noperms(player);

                                }
                          /*  } else {

                            invalargs(player);



                        }

                           */
                    }


                        case "default" ->

                            invalargs(player);

                    }
                }

            }


        }

        return true;
    }


        public boolean perms (Player player, String perm){

            return player.hasPermission(perm);

        }


        public void msg(String msg, Player player) {

            player.sendMessage(msg);

        }

        public void noperms(Player player) {

            String noperms = plugin.prefix + "You do not have the permissions to execute that command!";

            msg(noperms ,player);

        }

        public void invalargs(Player player) {

            String invalargs = plugin.prefix + "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu";

            msg(invalargs, player);

        }

        public void plinfo(Player player) {

            String plinfo = plugin.prefix + "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.version + "§r";

            msg(plinfo, player);

        }

        public void alreadyclaimed(Player player, String suffix) {

            String alredyclaimed = plugin.prefix + "This chunk is already claimed!";

            msg(alredyclaimed + "\n" + suffix, player);

        }

        public void isnotclaimed(Player player) {

            String isnotclaimed = plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it";

            msg(isnotclaimed, player);

        }

        public void successclaim(Player player, Boolean plural) {

            String successclaim = plugin.prefix + "Successfully claimed chunk!";
            String successclaims = plugin.prefix + "Successfully claimed chunks!";

            if (plural) {

                msg(successclaims, player);

            } else {

                msg(successclaim, player);

            }

        }


        public void helpmenu(Player player) {


            String[] helpmenu = new String[12];

            helpmenu[0] = "§b -- /claim --               \n §7Claim the current chunk if it's not been claimed yet§r";
            helpmenu[1] = "§b -- /claim remove --        \n §7Remove the current chunk from your/any claimed chunks list§r";
            helpmenu[2] = "§b -- /claim owner --         \n §7Displays the Name and UUID of the current chunks owner§r";
            helpmenu[3] = "§b -- /claim overwrite --     \n §7Claim the current chunk and overwrite its previous ownership§r";
            helpmenu[4] = "§b -- /claim info --          \n §7Displays Author and Version of this Plugin§r";
            helpmenu[5] = "§b -- /claim help --          \n §7Displays this menu§r";
            helpmenu[6] = "§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━§r";
            helpmenu[7] = "§b -- /claim clear --         \n §7Clear all claimed chunks§r";
            helpmenu[8] = "§b -- /claim clear confirm -- \n §7Confirmation to clear all claimed chunks (/claim clear)§r";
            helpmenu[9] = "§b -- /claim radius <1-8> --  \n §7Claim all chunks in a radius of 1-8. USE CAREFULLY!§r";
            helpmenu[10] = "§7 -- -- §3 Help Menu §bby §3" + plugin.author + "§7 -- -- ";
            helpmenu[11] = "§bVersion: §3" + plugin.version;


            msg(helpmenu[6] + "\n" +
                    helpmenu[10] + "\n" +
                    helpmenu[6] + "\n" +

                    helpmenu[0] + "\n\n" +
                    helpmenu[1] + "\n\n" +
                    helpmenu[2] + "\n\n" +
                    helpmenu[3] + "\n\n" +
                    helpmenu[4] + "\n\n" +
                    helpmenu[7] + "\n\n" +
                    helpmenu[8] + "\n\n" +
                    helpmenu[9] + "\n\n" +

                    helpmenu[5] + "\n\n" +
                    helpmenu[11] + "\n" +
                    helpmenu[6]
                    , player);

        }

        public boolean radiuschecker(int radius, int sub) {

            return radius - sub > -1;

        }

}

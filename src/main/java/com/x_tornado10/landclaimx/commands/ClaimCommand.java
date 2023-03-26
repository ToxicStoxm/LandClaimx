package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import com.x_tornado10.landclaimx.util.Logger;
import com.x_tornado10.landclaimx.util.Perms;
import com.x_tornado10.landclaimx.util.PlayerMessages;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimCommand implements CommandExecutor {

    private final LandClaimX plugin = LandClaimX.getInstance();

    private final Logger logger = plugin.getCustomLogger();
    private final PlayerMessages plmsg = plugin.getPlmsg();

    private final Perms perms = plugin.getPerms();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {

            logger.info("Only Players can run this command!");
            return true;

        } else {

            String perms_claim = perms.getPerms_claim();
            String perms_clear = perms.getPerms_clear();
            String perms_owner = perms.getPerms_owner();
            String perms_radius = perms.getPerms_radius();
            String perms_remove = perms.getPerms_remove();
            String perms_remove_other = perms.getPerms_remove_other();
            String perms_overwrite = perms.getPerms_overwrite();

            Player p = (Player) sender;

            Chunk chunk = p.getLocation().getChunk();
            String chunkID = chunk.getX() + "," + chunk.getZ();

            if (!p.getLocation().getWorld().getName().equals(plugin.getWorldname())) {

                plmsg.msg(p, "You are not allowed to use '/claim' in this dimension!");
                return true;

            }

            if (perms(p, perms_claim)) {

                switch (args.length) {
                    case 0 -> {
                        if (plugin.isChunk(chunkID)) {

                            if (perms(p, perms_overwrite)) {

                                alreadyclaimed(p, "Type '/claim overwrite' to overwrite the previous claim");

                            } else if (perms(p, perms_owner)) {

                                alreadyclaimed(p, "Type '/claim owner' to display the chunk's Owner");

                            } else {


                                alreadyclaimed(p, "You don't have the Permissions to overwrite the claim!");

                            }

                            return true;

                        } else {

                            plugin.addChunk(chunkID, p.getUniqueId());

                            successclaim(p, false);

                        }
                    }
                    case 1 -> {
                        switch (args[0].toLowerCase()) {

                            case "owner":

                                if (!perms(p, perms_owner)) {

                                    noperms(p);
                                    return true;

                                }

                                if (plugin.getOwner(chunkID) == null) {

                                    isnotclaimed(p);

                                } else {

                                    UUID owneruuid = plugin.getOwner(chunkID);

                                    try {

                                        String ownername = Bukkit.getPlayer(owneruuid).getName();

                                        plmsg.msg(p, "The owner of this chunk is: \n" + plugin.getColorprefix() + ownername);


                                    } catch (NullPointerException e) {

                                        String ownername = Bukkit.getOfflinePlayer(owneruuid).getName();

                                        plmsg.msg(p, "The owner of this chunk is: \n" + plugin.getColorprefix() + ownername);

                                    }

                                }
                                break;

                            case "overwrite":

                                if (!perms(p, perms_overwrite)) {

                                    noperms(p);
                                    return true;

                                }

                                plugin.replaceChunk(chunkID, p.getUniqueId());

                                //player feedback
                                successclaim(p, false);

                                break;

                            case "remove":

                                if (!perms(p, perms_remove)) {

                                    noperms(p);
                                    return true;

                                }

                                if (plugin.getOwner(chunkID) == null) {

                                    isnotclaimed(p);

                                } else {

                                    if (p.getUniqueId().equals(plugin.getOwner(chunkID))) {

                                        plugin.removeChunk(chunkID, p.getUniqueId());
                                        plmsg.msg(p, "Successfully unclaimed chunk!");

                                    } else if (perms(p, perms_remove_other)) {

                                        plugin.removeChunk(chunkID, plugin.getOwner(chunkID));
                                        plmsg.msg(p, "Successfully unclaimed chunk!");

                                    } else {

                                        plmsg.msg(p, "You can not unclaim chunks, owned by other players!");

                                    }
                                }
                                break;

                            case "info":
                                plinfo(p);
                                break;

                            case "help":
                                helpmenu(p);
                                break;

                            case "clear":

                                if (!perms(p, perms_clear)) {

                                    noperms(p);
                                    return true;

                                }
                                plmsg.msg(p, "Are you sure that you want to delete all claimed chunks?");
                                plmsg.msg(p, "Type '/claim clear confirm' to confirm");

                                break;
                            case "radius":
                                if (!perms(p, perms_radius)) {

                                    noperms(p);
                                    return true;

                                }

                                plmsg.msg(p, "Please specify a radius between 1 and 8!");


                            default:
                                invalargs(p);
                                break;
                        }
                    }
                    case 2 -> {
                        switch (args[1].toLowerCase()) {
                            case "confirm" -> {
                                if (!args[0].toLowerCase().equals("clear")) {

                                    invalargs(p);
                                    return true;

                                }
                                if (!perms(p, perms_clear)) {

                                    noperms(p);
                                    return true;

                                }
                                plmsg.msg(p, "Starting process...");
                                plugin.clearClaims();
                                plmsg.msg(p, "Process complete!");
                                plmsg.msg(p, "Successfully deleted all claimed chunks");
                            }
                            case "1", "2", "3", "4", "5", "6", "7", "8" -> {

                                if (!args[0].toLowerCase().equals("radius")) {

                                    invalargs(p);
                                    return true;

                                }

                                if (!perms(p, perms_radius)) {

                                    noperms(p);
                                    return true;

                                }

                                int radius = Integer.parseInt(args[1]);

                                int chunkZ = p.getLocation().getChunk().getZ();
                                int chunkX = p.getLocation().getChunk().getX();

                                logger.info("Calculating chunks...");
                                plmsg.msg(p, "Calculating chunks...");

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


                                List<String> chunkIDs = new ArrayList<>();

                                if (radius >= 1) {

                                    chunkIDs.add(chunkX + "," + chunkZ);
                                    chunkIDs.add(chunkXm1 + "," + chunkZ);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp1);
                                    chunkIDs.add(chunkX + "," + chunkZp1);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp1 + "," + chunkZ);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm1);
                                    chunkIDs.add(chunkX + "," + chunkZm1);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm1);

                                }
                                if (radius >= 2) {

                                    chunkIDs.add(chunkXm2 + "," + chunkZ);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp2);
                                    chunkIDs.add(chunkX + "," + chunkZp2);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp2 + "," + chunkZ);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm2);
                                    chunkIDs.add(chunkX + "," + chunkZm2);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm1);

                                }
                                if (radius >= 3) {

                                    chunkIDs.add(chunkXm3 + "," + chunkZ);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp3);
                                    chunkIDs.add(chunkX + "," + chunkZp3);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp3 + "," + chunkZ);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm3);
                                    chunkIDs.add(chunkX + "," + chunkZm3);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm1);

                                }
                                if (radius >= 4) {

                                    chunkIDs.add(chunkXm4 + "," + chunkZ);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp4);
                                    chunkIDs.add(chunkX + "," + chunkZp4);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp4 + "," + chunkZ);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm4);
                                    chunkIDs.add(chunkX + "," + chunkZm4);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm1);

                                }
                                if (radius >= 5) {

                                    chunkIDs.add(chunkXm5 + "," + chunkZ);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp5);
                                    chunkIDs.add(chunkX + "," + chunkZp5);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp5 + "," + chunkZ);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm5);
                                    chunkIDs.add(chunkX + "," + chunkZm5);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm1);

                                }

                                //radius 6
                                if (radius >= 6) {

                                    chunkIDs.add(chunkXm6 + "," + chunkZ);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp6);
                                    chunkIDs.add(chunkX + "," + chunkZp6);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp6 + "," + chunkZ);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm6);
                                    chunkIDs.add(chunkX + "," + chunkZm6);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm1);

                                }

                                //radius 7
                                if (radius >= 7) {

                                    chunkIDs.add(chunkXm7 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp7);
                                    chunkIDs.add(chunkX + "," + chunkZp7);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp7 + "," + chunkZ);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm7);
                                    chunkIDs.add(chunkX + "," + chunkZm7);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm1);
                                    chunkIDs.add(chunkXm7 + "," + chunkZ);

                                }

                                //radius 8
                                if (radius >= 8) {

                                    chunkIDs.add(chunkXm8 + "," + chunkZ);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp1);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp2);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp3);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp4);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp5);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp6);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp7);
                                    chunkIDs.add(chunkXm8 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm7 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm6 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm5 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm4 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm3 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm2 + "," + chunkZp8);
                                    chunkIDs.add(chunkXm1 + "," + chunkZp8);
                                    chunkIDs.add(chunkX + "," + chunkZp8);
                                    chunkIDs.add(chunkXp1 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp2 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp3 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp4 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp5 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp6 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp7 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp8);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp7);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp6);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp5);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp4);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp3);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp2);
                                    chunkIDs.add(chunkXp8 + "," + chunkZp1);
                                    chunkIDs.add(chunkXp8 + "," + chunkZ);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm1);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm2);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm3);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm4);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm5);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm6);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm7);
                                    chunkIDs.add(chunkXp8 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp7 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp6 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp5 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp4 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp3 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp2 + "," + chunkZm8);
                                    chunkIDs.add(chunkXp1 + "," + chunkZm8);
                                    chunkIDs.add(chunkX + "," + chunkZm8);
                                    chunkIDs.add(chunkXm1 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm2 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm3 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm4 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm5 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm6 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm7 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm8);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm7);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm6);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm5);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm4);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm3);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm2);
                                    chunkIDs.add(chunkXm8 + "," + chunkZm1);

                                }

                                logger.info("Done!");

                                UUID owner = p.getUniqueId();

                                ArrayList<Integer> ochunks = new ArrayList<>();

                                List<UUID> u = new ArrayList<>();

                                for (int i = chunkIDs.size() - 1; i > -1; i--) {

                                    if (!plugin.isChunk(chunkIDs.get(i))) {

                                        plugin.addChunk(chunkIDs.get(i), owner);

                                    } else {

                                        if (!plugin.getOwner(chunkIDs.get(i)).equals(p.getUniqueId())) {

                                            if (perms(p, perms_overwrite)) {

                                                plugin.replaceChunk(chunkIDs.get(i), owner);

                                            }

                                            ochunks.add(i);
                                            u.add(plugin.getOwner(chunkIDs.get(i)));

                                        }

                                    }

                                    if (i == 0) {

                                        List<String> owners = new ArrayList<>();


                                        for (int in = u.size() - 1; in > -1; in--) {

                                            try {

                                                if (!owners.contains(Bukkit.getPlayer(u.get(in)).getName())) {

                                                    owners.add(Objects.requireNonNull(Bukkit.getPlayer(u.get(in))).getName());

                                                }

                                            } catch (Exception e) {

                                                if (!owners.contains(Bukkit.getOfflinePlayer(u.get(in)).getName())) {

                                                    owners.add(Bukkit.getOfflinePlayer(u.get(in)).getName());

                                                }

                                            }

                                        }

                                        if (!ochunks.isEmpty()) {

                                            if (perms(p, perms_overwrite)) {

                                                plmsg.msg(p, "Your selected area overlaps with chunks claimed by other players: ");

                                                for (String pl : owners) {

                                                    plmsg.msg(p, pl);

                                                }

                                                plmsg.msg(p, "Overwriting because needed permissions are given...");

                                                successclaim(p, true);

                                            } else {

                                                plmsg.msg(p, "Your selected area overlaps with chunks claimed by other players ");

                                                for (String pl : owners) {

                                                    plmsg.msg(p, pl);

                                                }

                                                plmsg.msg(p, "Reduce the radius of the command or type '/claim help' for more info!");

                                            }

                                        } else {

                                            successclaim(p, true);

                                        }

                                        u.clear();
                                        owners.clear();
                                        ochunks.clear();
                                        chunkIDs.clear();

                                    }

                                }
                            }

                            default -> invalargs(p);
                        }
                    }
                    default -> invalargs(p);
                }

            } else {

                noperms(p);

            }

        }

        return true;
    }


        public boolean perms (Player player, String perm){

            return player.hasPermission(perm);

        }

        public void noperms(Player player) {

            plmsg.msg(player, "You do not have the permissions to execute that command!");

        }

        public void invalargs(Player player) {

            plmsg.msg(player, "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu");

        }

        public void plinfo(Player player) {

            plmsg.msg(player, "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.getVersion() + "§r");

        }

        public void alreadyclaimed(Player player, String suffix) {

            plmsg.msg(player, "This chunk is already claimed!" + "\n" + suffix);

        }

        public void isnotclaimed(Player player) {

            plmsg.msg(player, "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");

        }

        public void successclaim(Player player, Boolean plural) {

            if (plural) {

              plmsg.msg(player, "Successfully claimed chunks!");

            } else {

                plmsg.msg(player, "Successfully claimed chunk!");

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
            helpmenu[10] = "§7 -- -- §3 Help Menu §bby §3" + plugin.getAuthor() + "§7 -- -- ";
            helpmenu[11] = "§bVersion: §3" + plugin.getVersion();


            plmsg.msg(player,
                    helpmenu[6] + "\n" +
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
                    );

        }

        public boolean radiuschecker(int radius, int sub) {

            return radius - sub > -1;

        }

}

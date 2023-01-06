package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;


public class ClaimCommandOld implements CommandExecutor {


    private final LandClaimX plugin;

    public ClaimCommandOld(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

    public String radius1 = "1";
    public String radius2 = "2";
    public String radius3 = "3";
    public String radius4 = "4";
    public String radius5 = "5";
    public String radius6 = "6";
    public String radius7 = "7";
    public String radius8 = "8";




    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        //Player Feedback messages
        String noperms = plugin.prefix + "You do not have the permissions to execute that command!";
        String invalargs = plugin.prefix + "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu";
        String plinfo = plugin.prefix + "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.version + "§r";
        String alredyclaimed = plugin.prefix + "This chunk is already claimed!" + "\n";
        String isnotclaimed = plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it";
        String successclaim = plugin.prefix + "Successfully claimed chunk!";
        String successclaims = plugin.prefix + "Successfully claimed chunks!";


        //if the command is executed by a player
        if (!(sender instanceof Player)) {

            //if the command isn't executed by a player
            sender.sendMessage(plugin.prefix + "Only Players can run this command!");
            return true;

        } else {

            //defining player
            Player player = (Player) sender;
            //general plugin information. Displayed when using '/claim info'

            //permission check
            if (player.hasPermission(plugin.perms_claim)) {

                if (args.length >= 2) {

                    String word = args[0];
                    String word2 = args[1];

                    if (word.equals("owner") || word.equals("overwrite") || word.equals("info") || word.equals("help")  || word.equals("remove")) {

                        if (word2 == null) {


                        } else {

                            sendmessage(invalargs, player);
                            return true;

                        }

                    }

                }

                //if the player didn't provide any arguments
                if (args.length == 0) {

                    //current chunk gets defined
                    Chunk chunk = player.getLocation().getChunk();

                    //defining a chunkID for every chunk
                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    //if the chunk is already claimed (isChunk function from main class)
                    if (plugin.isChunk(chunkID)) {

                        //permission check
                        if (player.hasPermission(plugin.perms_overwrite)) {

                            //player feedback
                            sendmessage(alredyclaimed + "Type '/claim overwrite' to overwrite the previous claim", player);

                            //permissio ncheck
                        } else if (player.hasPermission(plugin.perms_owner)){

                            sendmessage(alredyclaimed + "Type '/claim owner' to display the chunk's Owner", player);

                        }else {

                            //player feedback
                            sendmessage(alredyclaimed + "You don't have the Permissions to overwrite the claim!", player);

                        }

                    } else {

                        //adds the current chunk to the HashMap 'chunks' (chunkID, UUID)
                        plugin.addChunk(chunkID, player.getUniqueId());
                        //player feedback
                        player.sendMessage(successclaim);


                    }
                    //if the player provided 1 argument
                } else if (args.length == 1) {


                    String word = args[0];

                    //argument check for the first argument, if the player provided wrong arguments a help message is send
                    if (!word.equals("owner") && !word.equals("overwrite") && !word.equals("info") && !word.equals("help") && !word.equals("clear") && !word.equals("radius") && !word.equals("remove")) {

                        //player feedback
                        sendmessage(invalargs, player);
                        return true;

                    }



                    if (word.equals("radius")) {

                        sendmessage(plugin.prefix + "Please enter a number between 1 and 8!", player);

                    }


                    //if the first argument equals to 'owner'
                    if (word.equals("owner")) {


                        //permission check
                        if (player.hasPermission(plugin.perms_owner)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                sendmessage(isnotclaimed, player);
                                return true;

                            } else {

                                //Putting information about the owner into strings
                                String owneruuid = plugin.getOwner(chunkID).toString();

                                try {

                                    String ownername = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getPlayer(owneruuid)).getName());

                                    //player feedback
                                    sendmessage(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername, player);


                                } catch (Exception e) {

                                    String ownername = Bukkit.getOfflinePlayer(plugin.getOwner(chunkID)).getName();

                                    //player feedback
                                    sendmessage(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername, player);
                                }

                            }

                        } else {

                            //player feedback
                            sendmessage(noperms, player);

                        }


                    }





                    //if the first argument equals to 'overwrite'
                    if (word.equals("overwrite")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_overwrite)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            plugin.addChunk(chunkID, player.getUniqueId());

                            //player feedback
                            sendmessage(successclaim, player);

                        } else {

                            //player feedback
                            sendmessage(noperms, player);

                        }

                    }






                    //if the first argument equals to 'remove'
                    if (word.equals("remove")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_remove)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            //check if the chunk isn't claimed yet to prevent error
                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                sendmessage(isnotclaimed, player);
                                return true;

                            } else {

                                //permission check
                                if (player.hasPermission(plugin.perms_remove_other)) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    sendmessage(successclaim, player);

                                    //check if the player tries to remove his own chunk claim
                                } else if (((Player) sender).getUniqueId().equals(plugin.getOwner(chunkID))) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    sendmessage(successclaim, player);

                                    //check if the player tries to remove other players chunk claim without permission
                                } else {

                                    //player feedback
                                    sendmessage("You can not unclaim chunks, owned by other players!", player);

                                }
                            }


                        }else {

                            //player feedback
                            sendmessage(noperms, player);

                        }

                    }




                    //if first argument equals to 'info'
                    if (word.equals("info")) {

                        //send info message
                        sendmessage(plinfo, player);

                    }



                    //if first argument equals to 'help'
                    if (word.equals("help")) {

                        //defining the help menu
                        String[] helpmenu = new String[10];

                        //defining the lines of the help menu
                        helpmenu[0] = "§b -- /claim --               \n §7Claim the current chunk if it's not been claimed yet§r";
                        helpmenu[1] = "§b -- /claim remove --        \n §7Remove the current chunk from your/any claimed chunks list§r";
                        helpmenu[2] = "§b -- /claim owner --         \n §7Displays the Name and UUID of the current chunks owner§r";
                        helpmenu[3] = "§b -- /claim overwrite --     \n §7Claim the current chunk and overwrite its previous ownership§r";
                        helpmenu[4] = "§b -- /claim info --          \n §7Displays Author and Version of this Plugin§r";
                        helpmenu[5] = "§b -- /claim help --          \n §7Displays this menu§r";
                        helpmenu[6] = "§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
                        helpmenu[7] = "§b -- /claim clear --         \n §7Clear all claimed chunks§r";
                        helpmenu[8] = "§b -- /claim clear confirm -- \n §7Confirmation to clear all claimed chunks (/claim clear)§r";
                        helpmenu[9] = "§b -- /claim radius <1-8> --  \n §7Claim all chunks in a radius of 1-8. USE CAREFULLY!§r";

                        //defining the structure of the help menu
                        sendmessage(helpmenu[6] + "\n" +
                                plinfo.replace("["," ").replace("]"," ") + "\n\n" +

                                helpmenu[0] + "\n\n" +
                                helpmenu[1] + "\n\n" +
                                helpmenu[2] + "\n\n" +
                                helpmenu[3] + "\n\n" +
                                helpmenu[4] + "\n\n" +
                                helpmenu[7] + "\n\n" +
                                helpmenu[8] + "\n\n" +
                                helpmenu[9] + "\n\n" +

                                helpmenu[5] + "\n" +
                                helpmenu[6]
                                , player);


                    }

                    //if the first argument equals 'clear'
                    if (word.equals("clear")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_clear)) {

                            //player feedback
                            sendmessage(plugin.prefix +"Are you sure that you want to delete all claimed chunks?", player);
                            sendmessage(plugin.prefix + "Type '/claim clear confirm' to confirm", player);

                        } else {

                            //player feedback
                            sendmessage(noperms, player);

                        }


                    }

                    //if the player provided more than 1 argument
                } else {

                    String word = args[0];

                    //if the player provided 2 arguments
                    if (args.length == 2) {

                        String word2 = args[1];


                        if (word.equals("radius")) {

                            if (player.hasPermission(plugin.perms_radius)) {


                                if (radius1to8(word2)) {

                                   int chunkZ = player.getLocation().getChunk().getZ();
                                   int chunkX = player.getLocation().getChunk().getX();

                                   Bukkit.getLogger().info(plugin.consoleprefix + "Calculating chunks...");
                                   sendmessage(plugin.prefix + "Calculating chunks...", player);


                                       int chunkXm1 = chunkX-1;
                                       int chunkXp1 = chunkX+1;

                                       int chunkZm1 = chunkZ-1;
                                       int chunkZp1 = chunkZ+1;


                                       int chunkXm2 = chunkX-2;
                                       int chunkXp2 = chunkX+2;

                                       int chunkZm2 = chunkZ-2;
                                       int chunkZp2 = chunkZ+2;


                                       int chunkXm3 = chunkX-3;
                                       int chunkXp3 = chunkX+3;

                                       int chunkZm3 = chunkZ-3;
                                       int chunkZp3 = chunkZ+3;


                                       int chunkXm4 = chunkX-4;
                                       int chunkXp4 = chunkX+4;

                                       int chunkZm4 = chunkZ-4;
                                       int chunkZp4 = chunkZ+4;


                                       int chunkXm5 = chunkX-5;
                                       int chunkXp5 = chunkX+5;

                                       int chunkZm5 = chunkZ-5;
                                       int chunkZp5 = chunkZ+5;


                                       int chunkXm6 = chunkX-6;
                                       int chunkXp6 = chunkX+6;

                                       int chunkZm6 = chunkZ-6;
                                       int chunkZp6 = chunkZ+6;


                                       int chunkXm7 = chunkX-7;
                                       int chunkXp7 = chunkX+7;

                                       int chunkZm7 = chunkZ-7;
                                       int chunkZp7 = chunkZ+7;

                                       int chunkXm8 = chunkX-8;
                                       int chunkXp8 = chunkX+8;

                                       int chunkZm8 = chunkZ-8;
                                       int chunkZp8 = chunkZ+8;


                                       sendmessage(plugin.prefix + "Please wait!", player);


                                       List<String> chunkID = new ArrayList<>();

                                       if (radius1to8(word2)) {

                                           chunkID.add(chunkX + "," + chunkZ);
                                           chunkID.add(chunkXm1 + "," + chunkZ);
                                           chunkID.add(chunkXm1 + "," + chunkZp1);
                                           chunkID.add(chunkX + "," + chunkZp1);
                                           chunkID.add(chunkXp1 + "," + chunkZp1);
                                           chunkID.add(chunkXp1 + "," + chunkZ);
                                           chunkID.add(chunkXp1 + "," + chunkZm1);
                                           chunkID.add(chunkX + "," + chunkZm1);
                                           chunkID.add(chunkXm1 + "," + chunkZm1);

                                       }

                                       if (radiusmorethan2(word2)) {

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

                                       }

                                       if (radiusmorethan3(word2)) {

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

                                       }

                                       if (radiusmorethan4(word2)) {

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

                                       }



                                       if (radiusmorethan5(word2)) {

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

                                       }

                                       if (radiusmorethan6(word2)) {

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

                                       }

                                       if (radiusmorethan7(word2)) {

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

                                       }

                                       if (word2.equals(radius8)) {

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

                                       }

                                       Bukkit.getLogger().info(plugin.consoleprefix + "Done!");

                                       UUID owner = player.getUniqueId();

                                       int i = chunkID.size()-1;


                                       while (i > -1) {

                                           if (!plugin.isChunk(chunkID.get(i))) {

                                               plugin.addChunk(chunkID.get(i), owner);

                                           } else {

                                               if (player.hasPermission(plugin.perms_overwrite) && !plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {

                                                   plugin.addChunk(chunkID.get(i), owner);

                                               }
                                           }



                                               List<UUID> u = new ArrayList<>();

                                               u.add(plugin.getOwner(chunkID.get(i)));



                                               if (i == 0) {

                                                   List<String> owners = new ArrayList<>();

                                                   int in = u.size() - 1;


                                                   while (in > -1) {

                                                       try {

                                                           owners.add(Objects.requireNonNull(Bukkit.getPlayer(u.get(in))).getName());

                                                       } catch (Exception e) {

                                                           owners.add(Bukkit.getOfflinePlayer(u.get(in)).getName());

                                                       }

                                                       in = in-1;

                                                   }

                                                   if (!plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {


                                                       if (player.hasPermission(plugin.perms_overwrite)) {

                                                           sendmessage(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Overwriting because needed permissions are given...", player);
                                                           sendmessage(successclaims, player);

                                                       } else {

                                                           sendmessage(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Reduce the radius of the command or type '/claim help' for more info!", player);

                                                       }

                                                   } else {

                                                       sendmessage(successclaims, player);

                                                   }


                                                   u.clear();
                                                   owners.clear();

                                               }


                                           i = i - 1;

                                       }



                                   } else {

                                    sendmessage(invalargs, player);

                                }


                            } else {

                               sendmessage(noperms, player);


                            }

                        }


                        //if the first argument equals 'clear'
                        if (word.equals("clear")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_clear)) {

                                //if the second argument equals 'confirm'
                                if (word2.equals("confirm")) {

                                    //player feedback
                                    sendmessage(plugin.prefix + "Starting process...", player);

                                    //clearing the HashMap 'chunks'
                                    plugin.clearChunks();
                                    //clearing the save file claims.yml
                                    plugin.clearFile();

                                    //player feedback
                                    sendmessage(plugin.prefix + "Process complete!", player);
                                    sendmessage(plugin.prefix + "Successfully deleted all claimed chunks", player);

                                } else {

                                    sendmessage(invalargs, player);

                                }

                            }else {

                                //player feedback
                            sendmessage(noperms, player);

                            }
                        }


                    } else {

                        //player feedback
                        sendmessage(plugin.prefix + "You provided too many arguments!", player);

                    }

                }



            } else {

                //player feedback
                sendmessage(noperms, player);

            }

        }

        return true;

    }

    public boolean radius1to8(String word2) {


        return word2.equals(radius1) || word2.equals(radius2) || word2.equals(radius3) || word2.equals(radius4) || word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }


    public boolean radiusmorethan2(String word2) {

        return word2.equals(radius2) || word2.equals(radius3) || word2.equals(radius4) || word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }

    public boolean radiusmorethan3(String word2) {

        return word2.equals(radius3) || word2.equals(radius4) || word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }

    public boolean radiusmorethan4(String word2) {

        return word2.equals(radius4) || word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }

    public boolean radiusmorethan5(String word2) {

        return word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }

    public boolean radiusmorethan6(String word2) {

        return word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8);

    }

    public boolean radiusmorethan7(String word2) {

        return word2.equals(radius7) || word2.equals(radius8);

    }


    public void sendmessage(String message, Player player) {

        player.sendMessage(message);

    }


    public void args_validation(String[] args) {

        if (args.length == 1) {



        }


    }

}

package com.x_tornado10.landclaimx;

import com.x_tornado10.landclaimx.commands.ClaimCommandTabCompletion;
import com.x_tornado10.landclaimx.commands.ClaimCommand;
import com.x_tornado10.landclaimx.handlers.ConfigHandler;
import com.x_tornado10.landclaimx.util.FileLocations;
import com.x_tornado10.landclaimx.util.Logger;
import com.x_tornado10.landclaimx.util.Perms;
import com.x_tornado10.landclaimx.util.PlayerMessages;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

//MAIN CLASS
public final class LandClaimX extends JavaPlugin {

    private static LandClaimX instance;

   private HashMap<String, UUID> chunks;

    private String prefix;
    private String colorprefix;

    private String version;
    private String author;

    private String perms_claim;
    private String perms_remove;
    private String perms_remove_other;
    private String perms_clear;
    private String perms_radius;
    private String perms_owner;
    private String perms_overwrite;

    private long start;
    private long finish;

    private Perms perms;
    private FileLocations fl;
    private Logger logger;
    private PlayerMessages plmsg;
    private String worldname;
    private boolean doclaimsreadprint;

    private ConfigHandler configHandler;

   @Override
   public void onLoad() {

       instance = this;
       start = System.currentTimeMillis();
       prefix = "[" + getDescription().getPrefix() + "] ";
       colorprefix = "§0§l[§b" + getDescription().getPrefix() + "§0§l]:§r ";
       saveDefaultConfig();

       logger = new Logger(prefix);
       fl = new FileLocations();
       plmsg = new PlayerMessages(colorprefix);
       perms = new Perms();
       configHandler = new ConfigHandler(getConfig());

       saveConfig();
       reloadConfig();

       createFiles();

       perms_claim = perms.getPerms_claim();
       perms_clear = perms.getPerms_clear();
       perms_owner = perms.getPerms_owner();
       perms_radius = perms.getPerms_radius();
       perms_remove = perms.getPerms_remove();
       perms_remove_other = perms.getPerms_remove_other();
       perms_overwrite = perms.getPerms_overwrite();
       
       version = getDescription().getVersion();
       author = getDescription().getAuthors().get(0);

       doclaimsreadprint = configHandler.getDoClaimsReadPrint();
       worldname = configHandler.getWorldname();

       chunks = getHashMapFromTextFile();

   }


    @Override
    public void onEnable() {

        // Plugin startup logic
        logger.info("Welcome back!");
        logger.info("Starting up...");
        logger.info("Checking for dependencies...");

        if (Bukkit.getPluginManager().getPlugin("dynmap") == null) {

            logger.severe("Dependencies: Plugin 'dynmap' wasn't found!");

        } else {

            logger.info("Dependencies: Dynmap version " + Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("dynmap")).getDescription().getVersion() + " was found and validated!");

        }

        logger.info("Loading config.yml...");
        logger.info("--Prefix: " + prefix);
        logger.info("--Ingame Prefix: " + colorprefix);
        logger.info("--Version: " + version);
        logger.info("--Authors: " + author);
        logger.info("Successfully loaded config.yml");

        logger.info("Loading claims.yml...");

        logger.info("");
        logger.info("---------------------------------------------------------------------------------------------");

            if (doclaimsreadprint) {

                for (Map.Entry entry : chunks.entrySet()) {

                    try {

                        Bukkit.getLogger().info(prefix + "Chunk: " + entry.getKey() + " <==> " + "Owner: " + Bukkit.getPlayer((UUID) entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());

                    } catch (Exception e) {

                        Bukkit.getLogger().info(prefix + "Chunk: " + entry.getKey() + " <==> " + "Owner: " + Bukkit.getOfflinePlayer((UUID) entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());
                    }
                }


        } else {

                logger.info("ReadLog from file 'claims.yml' is disabled");
                logger.info("To enable, it change 'doclaimsreadprint' to 'true' in 'config.yml'");

            }
        //console log
        logger.info("---------------------------------------------------------------------------------------------");
        logger.info("");
        logger.info("Loaded claims.yml");

        if (worldname == null || worldname.isBlank()) {

            worldname = Bukkit.getWorlds().get(0).getName();

        }

        getCommand("claim").setExecutor(new ClaimCommand());
        getCommand("claim").setTabCompleter(new ClaimCommandTabCompletion());
        
        finish = System.currentTimeMillis();
        logger.info("Successfully enabled! (took " + (finish - start) + "ms)");

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic

        logger.info("Saving files...");

        writeHashMapToTextFile();

        logger.info("Everything saved!");
        logger.info("Successfully disabled!");
    }

    public void createFiles() {

       File config_yml = new File(fl.getClaims_yml());

       File datadir = new File(fl.getDatadir());

       ArrayList<File> files = new ArrayList<>();

       files.add(config_yml);

       if (datadir.mkdirs()) {

           logger.info(datadir + " was successfully created!");

       }

       for (File file : files) {

           if (!file.exists()) {

               try {

                   if (file.createNewFile()) {

                       logger.info(file.getName() + " was successfully created!");

                   }
               } catch (IOException e) {

                   logger.severe("Something went wrong while trying to create files! Please restart the server!");

               }

           }


       }


    }

    private void writeHashMapToTextFile() {

        File claims = new File(fl.getClaims_yml());

        //writing HashMap 'chunks' to claims.yml
        if (chunks != null && claims.exists()) {

            try (BufferedWriter bf = new BufferedWriter(new FileWriter(claims))) {

                for (Map.Entry<String, UUID> entry : chunks.entrySet()) {

                    bf.write(entry.getKey() + ":" + entry.getValue());

                    bf.newLine();

                }

                bf.flush();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

    }

    private HashMap<String, UUID> getHashMapFromTextFile() {

        HashMap<String, UUID> mapFileContents = new HashMap<>();

        File claims = new File(fl.getClaims_yml());

        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(claims));

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(":");

                String chunkID = parts[0].trim();
                UUID UUID = java.util.UUID.fromString(parts[1].trim());

                if (!chunkID.equals("") && !String.valueOf(UUID).equals("")) {

                    mapFileContents.put(chunkID, UUID);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }

        return mapFileContents;

    }

    //function for adding a new chunk to the HashMap chunks. Is triggered by '/claim'
    public void addChunk(String chunk, UUID owner) {

        chunks.put(chunk, owner);

    }

    //function for checking if the current chunk is already claimed (returns true/false). Also triggered by 'claim'
    public boolean isChunk(String chunk) {

        return chunks.containsKey(chunk);
    }

    //function for getting the Owner of the current chunk (returns UUID). Triggered by '/claim owner'
    public UUID getOwner(String chunk) {

        return chunks.get(chunk);

    }

    public void removeChunk(String chunk, UUID owner) {

        chunks.remove(chunk, owner);

    }

    public void replaceChunk(String chunk, UUID owner) {

       if (chunks.get(chunk) != null) {

           chunks.replace(chunk, owner);

       } else {

           chunks.put(chunk, owner);

       }

    }

    public void clearClaims() {

        chunks.clear();

        File claims= new File(fl.getClaims_yml());

        try {

            PrintWriter pw = new PrintWriter(claims);
            pw.print("");
            pw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public String getWorldname() {
        return worldname;
    }

    public static LandClaimX getInstance() {
        return instance;
    }

    public Logger getCustomLogger() {
        return logger;
    }

    public PlayerMessages getPlmsg() {
        return plmsg;
    }

    public Perms getPerms() {
        return perms;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColorprefix() {
        return colorprefix;
    }
}

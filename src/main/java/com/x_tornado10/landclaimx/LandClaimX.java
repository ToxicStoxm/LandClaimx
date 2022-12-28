package com.x_tornado10.landclaimx;

import com.x_tornado10.landclaimx.commands.ClaimCommand;
import com.x_tornado10.landclaimx.commands.ClaimCommandTabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;;


public final class LandClaimX extends JavaPlugin {

    private File claimsFile;
    private FileConfiguration claims;

   private static HashMap<String, UUID> chunks;

   final static String FilePath = "F:\\Plugintestserver\\plugins\\Landclaimx\\claims.yml";


    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info(consoleprefix + "Welcome back!");
        Bukkit.getLogger().info(consoleprefix + "Starting up...");





        saveDefaultConfig();

        claimsFile = new File(getDataFolder(), "claims.yml");

        if (!claimsFile.exists()) {

            claimsFile.getParentFile().mkdirs();

            try {
                claimsFile.createNewFile();
            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        Bukkit.getLogger().info(consoleprefix + "Loading config.yml...");
        Bukkit.getLogger().info(consoleprefix + "Loaded config.yml");

        Bukkit.getLogger().info(consoleprefix + "Loading claims.yml...");
        Bukkit.getLogger().info(consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "---------------------------------------------------------------------------------------------");


        this.chunks = new HashMap<>();

        Map<String, UUID> mapFromFile = getHashMapFromTextFile();

        for(Map.Entry<String, UUID> entry : mapFromFile.entrySet()){

            Bukkit.getLogger().info(consoleprefix + "Chunk: "+ entry.getKey() + " <==> " + "Owner: " + Bukkit.getPlayer(entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());

        }
        Bukkit.getLogger().info(consoleprefix + "---------------------------------------------------------------------------------------------");
        Bukkit.getLogger().info(consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "Loaded claims.yml");

        Bukkit.getLogger().info(consoleprefix + "Enabled!");



        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("claim").setTabCompleter(new ClaimCommandTabCompletion());

    }

    public static Map<String, UUID> getHashMapFromTextFile() {

        Map<String, UUID> mapFileContents = new HashMap<String, UUID>();

        BufferedReader br = null;
        try {

            File file = new File(FilePath);

            br = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(":");

                String chunkID = parts[0].trim();
                UUID UUID = java.util.UUID.fromString(parts[1].trim());

                if (!chunkID.equals("") && !UUID.equals("")) {
                    chunks.put(chunkID, UUID);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
                ;
            }
        }

        return chunks;

    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getLogger().info(consoleprefix + "Saving files...");

        if (chunks == null) {

        } else {

            File file = new File(FilePath);

            BufferedWriter bf = null;
            ;

            try {
                bf = new BufferedWriter(new FileWriter(file));

                for (Map.Entry<String, UUID> entry : chunks.entrySet()) {

                    bf.write(entry.getKey() + ":" + entry.getValue());

                    bf.newLine();

                }

                bf.flush();
            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    bf.close();

                } catch (Exception e) {
                }

            }
        }

        save();

        Bukkit.getLogger().info(consoleprefix + "Everthing saved!");

        Bukkit.getLogger().info(consoleprefix + "Good Bye!");

        Bukkit.getLogger().info(consoleprefix + "shutting down...");
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_Blue = "\u001B[34m";

    public String prefix = getConfig().getString("Plugin.prefix").toString();
    public String consoleprefix = getConfig().getString("Plugin.consoleprefix").toString();
    public String version = getConfig().getString("Version").toString();

    public String author = "x_Tornado10";

    public void addChunk(String chunk, UUID owner) {

        chunks.put(chunk, owner);

    }

    public boolean isChunk(String chunk) {

        return chunks.containsKey(chunk);
    }

    public UUID getOwner(String chunk) {

        return chunks.get(chunk);

    }

    public void removeChunk(String chunk, UUID owner) {

        chunks.remove(chunk, owner);

    }

    public void save() {

            saveConfig();

    }

    public void clearFile() {

        try {

            PrintWriter pw = new PrintWriter(FilePath);
            pw.print("");
            pw.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }

    public void clearChunks() {

        chunks.clear();

    }

}

package de.jgaertig.sulfurFun.commands;

import de.jgaertig.sulfurFun.SulfurFun;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NewGame implements CommandExecutor, TabCompleter {

    private final SulfurFun plugin;
    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, String> setupPlayers = new HashMap<>();

    public NewGame(SulfurFun plugin) {
        this.plugin = plugin;
        // arenas.yml erstellen / laden
        this.file = new File(plugin.getDataFolder(), "arenas.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Prüfen ob Sender ein Spieler ist
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Warning: This Command is for players only!");
            return false;
        }
        // Prüfen ob genügend Argumente im Command sind
        if (!(args.length == 2)) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /newgame <type_of_minigame> <name_of_your_arena>");
            return false;
        }

        // Schöne Strings und Variablen erstellen
        Player player = (Player) sender;
        String type = args[0];
        String name = args[1];
        String redgoal1 = "";
        String redgoal2 = "";
        String redplayerspawn = "";
        String bluegoal1 = "";
        String bluegoal2 = "";
        String blueplayerspawn = "";
        String ballspawn = "";
        String playerperteam = "";
        String playerdamage = "";

        if (config.contains(name)) {
            player.sendMessage(ChatColor.RED + "Warning: '" + name + "' already exists!");
            return true;
        }

        // if Schleife
        if (type.equalsIgnoreCase("football")) {

            player.sendMessage(ChatColor.BLUE + "Step 1: " + ChatColor.WHITE + "Please Right-Click on the first corner of the Blue Goal.");


            // Werte speichern
            config.set(name + ".type", type);

            // Datei speichern
            try {
                config.save(file);
                player.sendMessage(ChatColor.GREEN + "Arena " + name + " is now saved!");
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Problem by saving data!");
                e.printStackTrace();
            }


        } else {
            player.sendMessage(ChatColor.YELLOW + "Unknown type of minigame");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("football", "upcoming");
        }
        if (args.length == 2) {
            return List.of("<name_of_arena>");
        }
        return List.of();
    }
}
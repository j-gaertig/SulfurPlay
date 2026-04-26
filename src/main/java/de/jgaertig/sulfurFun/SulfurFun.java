package de.jgaertig.sulfurFun;

import de.jgaertig.sulfurFun.commands.DeleteGame;
import de.jgaertig.sulfurFun.commands.NewGame;
import de.jgaertig.sulfurFun.listeners.SetupListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SulfurFun extends JavaPlugin {

    // Instanzvariablen für die Konfiguration
    private File arenaFile;
    private FileConfiguration arenaConfig;

    @Override
    public void onEnable() {
        // Initialisiert den Plugin-Ordner und die Arena-Datei
        setupConfiguration();

        // Initialisiert die Listener und Commands
        setupManagers();

        // Zeigt das Plugin-Logo in der Konsole an
        sendEnableMessage();
    }

    @Override
    public void onDisable() {
        // Logik für das Ausschalten des Plugins
    }

    // Hilfsmethoden für die Organisation
    private void setupConfiguration() {
        // Erstellt den Datenordner, falls er nicht existiert
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Lädt oder erstellt die arenas.yml
        arenaFile = new File(getDataFolder(), "arenas.yml");
        if (!arenaFile.exists()) {
            saveResource("arenas.yml", false);
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
    }

    private void setupManagers() {
        // Initialisiert den SetupListener
        SetupListener setupListener = new SetupListener();

        // Initialisiert die Commands
        NewGame newGameCommand = new NewGame(this, setupListener);
        DeleteGame deleteGameCommand = new DeleteGame(this, setupListener);

        // Verknüpft den Listener mit dem Command (für Rückfragen)
        setupListener.setNewGameCommand(newGameCommand);

        // Registriert die Commands bei Bukkit
        getCommand("newgame").setExecutor(newGameCommand);
        getCommand("newgame").setTabCompleter(newGameCommand);

        getCommand("deletegame").setExecutor(deleteGameCommand);
        getCommand("deletegame").setTabCompleter(deleteGameCommand);

        // Registriert den Listener für Events
        getServer().getPluginManager().registerEvents(setupListener, this);
    }

    // Zugriffsmethoden für andere Klassen
    public FileConfiguration getArenaConfig() {
        // Liefert die aktuelle Konfiguration zurück 📖
        return arenaConfig;
    }

    public void saveArenaConfig() {
        // Schreibt Änderungen dauerhaft in die Datei 💾
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
            getLogger().severe("Could not save arenas.yml!");
        }
    }

    private void sendEnableMessage() {
        // Sendet das Logo und Status-Infos an die Konsole
        String gold = ChatColor.GOLD.toString();
        String yellow = ChatColor.YELLOW.toString();
        String green = ChatColor.GREEN.toString();
        String gray = ChatColor.GRAY.toString();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(gold + ChatColor.BOLD + "           SulfurFun");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(gold + "      ████████████████████      ");
        Bukkit.getConsoleSender().sendMessage(gold + "      ██                ██      ");
        Bukkit.getConsoleSender().sendMessage(gold + "      ██                ██      ");
        Bukkit.getConsoleSender().sendMessage(gold + "      ██                ██      ");
        Bukkit.getConsoleSender().sendMessage(gold + "      ██                ██      ");
        Bukkit.getConsoleSender().sendMessage(gold + "      ██                ██      ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(yellow + "    ██████            ██████    ");
        Bukkit.getConsoleSender().sendMessage(yellow + "    ██████            ██████    ");
        Bukkit.getConsoleSender().sendMessage(yellow + "    ██████            ██████    ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(yellow + "              ████              ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(gray + "Version: " + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(green + "Plugin loaded ...");
        Bukkit.getConsoleSender().sendMessage("");
    }
}
package de.jgaertig.sulfurFun.commands;

import de.jgaertig.sulfurFun.SulfurFun;
import de.jgaertig.sulfurFun.listeners.SetupListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class DeleteGame implements CommandExecutor, TabCompleter {

    // Instanzvariablen
    private final SulfurFun plugin;
    private final SetupListener setupListener;

    // Konstruktor
    public DeleteGame(SulfurFun plugin, SetupListener setupListener) {
        this.plugin = plugin;
        this.setupListener = setupListener;
    }

    // Haupt-Command Logik
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Prüft, ob die Anzahl der Argumente korrekt ist
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /deletegame <name_of_existing_arena>");
            return false;
        }

        String arenaName = args[0];

        // Prüft, ob die Arena in der Konfiguration existiert
        if (plugin.getArenaConfig().contains(arenaName)) {
            // Löscht den gesamten Abschnitt der Arena aus der Config
            plugin.getArenaConfig().set(arenaName, null);

            // Bricht laufende Setup-Sessions für diese Arena ab
            setupListener.stopSessionsForArena(arenaName);

            // Speichert die Änderungen dauerhaft in der Datei
            plugin.saveArenaConfig();

            sender.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " is now deleted!");
        } else {
            sender.sendMessage(ChatColor.RED + "This arena doesn't exist.");
        }

        return true;
    }

    // Tab-Vorschläge
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Liefert alle existierenden Arenen-Namen für die Autovervollständigung
        if (args.length == 1) {
            return List.of(plugin.getArenaConfig().getKeys(false).toArray(new String[0]));
        }

        return List.of();
    }
}
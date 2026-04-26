package de.jgaertig.sulfurFun.commands;

import de.jgaertig.sulfurFun.SulfurFun;
import de.jgaertig.sulfurFun.listeners.SetupListener;
import de.jgaertig.sulfurFun.models.FootballSetup;
import de.jgaertig.sulfurFun.models.GameSetup;
import de.jgaertig.sulfurFun.models.SetupSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NewGame implements CommandExecutor, TabCompleter {

    // Instanzvariablen
    private final SulfurFun plugin;
    private final Map<String, GameSetup> availableGames = new HashMap<>();
    private SetupListener setupListener;

    // Konstruktor
    public NewGame(SulfurFun plugin, SetupListener setupListener) {
        this.plugin = plugin;
        this.setupListener = setupListener;

        // Registriert verfügbare Spielmodi
        availableGames.put("football", new FootballSetup());
    }

    // Getter und Setter
    public SulfurFun getPlugin() {
        return this.plugin;
    }

    public void setSetupListener(SetupListener setupListener) {
        this.setupListener = setupListener;
    }

    // Haupt-Command Logik
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Prüft, ob der Absender ein Spieler ist
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Warning: This Command is for players only!");
            return false;
        }

        Player player = (Player) sender;

        // Prüft die Anzahl der Argumente
        if (args.length != 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /newgame <type_of_minigame> <name_of_your_arena>");
            return false;
        }

        String type = args[0].toLowerCase();
        String name = args[1];

        // Prüft, ob die Arena bereits existiert
        if (plugin.getArenaConfig().contains(name)) {
            player.sendMessage(ChatColor.RED + "Warning: '" + name + "' already exists!");
            return true;
        }

        // Führt die Aktion basierend auf dem Spieltyp aus
        if (availableGames.containsKey(type)) {
            GameSetup selectedSetup = availableGames.get(type);

            // Startet eine neue Setup-Session
            setupListener.addPlayer(player.getUniqueId(), name, selectedSetup);
            plugin.getArenaConfig().set(name + ".type", type);

            SetupSession session = setupListener.getSession(player.getUniqueId());
            askNextStep(player, session);

            plugin.saveArenaConfig();
        }

        return true;
    }

    // Tab-Vorschläge
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Schlägt Spielmodi vor
        if (args.length == 1) {
            return List.copyOf(availableGames.keySet());
        }

        // Schlägt Platzhalter für den Namen vor
        if (args.length == 2) {
            return List.of("<name_of_arena>");
        }

        return List.of();
    }

    // Schnittstellen für den Listener
    public void handleSetupStep(Player player, Location loc) {
        saveToConfig(player, loc);
        finishStep(player);
    }

    public void handleSetupStep(Player player, int value) {
        saveToConfig(player, value);
        finishStep(player);
    }

    // Interne Hilfsmethoden zur Verarbeitung
    private void askNextStep(Player player, SetupSession session) {
        int index = session.getStep() - 1;
        GameSetup setup = session.getGameSetup();

        // Prüft, ob weitere Schritte im Setup vorhanden sind
        if (index < setup.getMessages().length) {
            String message = setup.getMessages()[index];
            player.sendMessage(ChatColor.BLUE + "Next Step: " + ChatColor.WHITE + message);
        } else {
            player.sendMessage(ChatColor.GOLD + "All positions are saved.");
            setupListener.removePlayer(player.getUniqueId());
        }
    }

    private void saveToConfig(Player player, Object value) {
        SetupSession session = setupListener.getSession(player.getUniqueId());
        String stepName = session.getGameSetup().getSteps()[session.getStep() - 1];
        String path = session.getArenaName() + "." + stepName;

        // Speichert entweder Koordinaten oder einfache Werte
        if (value instanceof Location) {
            Location loc = (Location) value;
            plugin.getArenaConfig().set(path + ".world", loc.getWorld().getName());
            plugin.getArenaConfig().set(path + ".x", loc.getBlockX());
            plugin.getArenaConfig().set(path + ".y", loc.getBlockY());
            plugin.getArenaConfig().set(path + ".z", loc.getBlockZ());
        } else {
            plugin.getArenaConfig().set(path, value);
        }

        plugin.saveArenaConfig();
    }

    private void finishStep(Player player) {
        SetupSession session = setupListener.getSession(player.getUniqueId());

        // Erhöht den Fortschritt und fragt den nächsten Schritt ab
        session.nextStep();
        askNextStep(player, session);
    }
}
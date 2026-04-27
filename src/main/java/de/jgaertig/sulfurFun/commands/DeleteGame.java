package de.jgaertig.sulfurFun.commands;

import de.jgaertig.sulfurFun.SulfurFun;
import de.jgaertig.sulfurFun.listeners.SetupListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class DeleteGame implements CommandExecutor, TabCompleter {

    private final SulfurFun plugin;
    private final SulfurFun.LanguageManager languageManager;
    private final SetupListener setupListener;

    public DeleteGame(SulfurFun plugin, SetupListener setupListener, SulfurFun.LanguageManager languageManager) {
        this.plugin = plugin;
        this.languageManager = languageManager;
        this.setupListener = setupListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 1. Prüfung der Argumente
        if (args.length != 1) {
            languageManager.send(sender, "messages.deletegame.usage");
            return false;
        }

        String arenaName = args[0];

        // 2. Prüfung, ob Arena existiert
        if (plugin.getArenaConfig().contains(arenaName)) {
            plugin.getArenaConfig().set(arenaName, null);
            setupListener.stopSessionsForArena(arenaName);
            plugin.saveArenaConfig();

            // Hier nutzen wir Platzhalter: In der YAML sollte z.B. stehen:
            // deleted: "&aArena &e%name% &awurde gelöscht!"
            languageManager.send(sender, "messages.deletegame.deleted", "%name%", arenaName);
        } else {
            // Fehlermeldung, wenn Arena nicht existiert
            languageManager.send(sender, "messages.deletegame.doesnotexist");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of(plugin.getArenaConfig().getKeys(false).toArray(new String[0]));
        }
        return List.of();
    }
}
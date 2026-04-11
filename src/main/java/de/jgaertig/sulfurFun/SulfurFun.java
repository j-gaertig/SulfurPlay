package de.jgaertig.sulfurFun;

import de.jgaertig.sulfurFun.commands.NewGame;
import de.jgaertig.sulfurFun.listeners.SetupListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;

public final class SulfurFun extends JavaPlugin {

    @Override
    public void onEnable() {
        File folder = getDataFolder();
        if (!folder.exists()) folder.mkdirs();

        NewGame newGameCommand = new NewGame(this, null);

        SetupListener setupListener = new SetupListener(newGameCommand);

        newGameCommand.setSetupListener(setupListener);

        getCommand("newgame").setExecutor(newGameCommand);
        getCommand("newgame").setTabCompleter(newGameCommand);

        getServer().getPluginManager().registerEvents(setupListener, this);


        String gold = ChatColor.GOLD.toString();
        String yellow = ChatColor.YELLOW.toString();
        String green = ChatColor.GREEN.toString();
        String gray = ChatColor.GRAY.toString();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(gold + ChatColor.BOLD + "      Sulfur");
        Bukkit.getConsoleSender().sendMessage(gold + "  .-----------.");
        Bukkit.getConsoleSender().sendMessage(gold + "  |    " + yellow + "FUN" + gold + "    |");
        Bukkit.getConsoleSender().sendMessage(gold + "  |           |");
        Bukkit.getConsoleSender().sendMessage(gold + "  |           |");
        Bukkit.getConsoleSender().sendMessage(gold + "  |  " + yellow + "☐     ☐" + gold + "  |");
        Bukkit.getConsoleSender().sendMessage(gold + "  '-----------'");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(green + "Plugin loaded ...");
        Bukkit.getConsoleSender().sendMessage(gray + "Version: " + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
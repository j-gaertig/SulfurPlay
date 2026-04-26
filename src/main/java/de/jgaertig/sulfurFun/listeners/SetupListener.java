package de.jgaertig.sulfurFun.listeners;

import de.jgaertig.sulfurFun.commands.NewGame;
import de.jgaertig.sulfurFun.models.GameSetup;
import de.jgaertig.sulfurFun.models.SetupSession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupListener implements Listener {

    // Instanzvariablen
    private NewGame newGameCommand;
    private final Map<UUID, SetupSession> sessions = new HashMap<>();

    // Konstruktor und Initialisierung
    public SetupListener() {}

    public void setNewGameCommand(NewGame newGameCommand) {
        this.newGameCommand = newGameCommand;
    }

    // Verwaltung der Setup-Sessions
    public void addPlayer(UUID uuid, String arenaName, GameSetup gameSetup) {
        // Erstellt eine neue Session für den Spieler
        sessions.put(uuid, new SetupSession(arenaName, gameSetup));
    }

    public void removePlayer(UUID uuid) {
        // Entfernt den Spieler aus der Map
        sessions.remove(uuid);
    }

    public SetupSession getSession(UUID uuid) {
        // Liefert die aktuelle Session zurück
        return sessions.get(uuid);
    }

    public void stopSessionsForArena(String arenaName) {
        // Beendet alle Sessions, die zu einer bestimmten Arena gehören
        sessions.entrySet().removeIf(entry -> {
            boolean match = entry.getValue().getArenaName().equalsIgnoreCase(arenaName);
            if (match) {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null) {
                    p.sendMessage(ChatColor.RED + "The setup was cancelled because the arena was deleted.");
                }
            }
            return match;
        });
    }

    // Event-Handler für Interaktionen
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Filtert nach Rechtsklicks auf Blöcke mit der Haupthand
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Prüft, ob der Spieler eine aktive Session hat
        if (!sessions.containsKey(uuid)) return;

        event.setCancelled(true);
        SetupSession session = sessions.get(uuid);
        int currentIndex = session.getStep() - 1;
        GameSetup.StepType currentType = session.getGameSetup().getStepTypes()[currentIndex];

        // Verarbeitet den Schritt, wenn ein Klick erwartet wird
        if (currentType == GameSetup.StepType.CLICK) {
            Block block = event.getClickedBlock();
            if (block != null) {
                newGameCommand.handleSetupStep(player, block.getLocation());
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "Please type the value in chat instead of clicking.");
        }
    }

    // Event-Handler für Chat-Eingaben
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Prüft, ob der Spieler eine aktive Session hat
        if (!sessions.containsKey(uuid)) return;

        SetupSession session = sessions.get(uuid);
        int currentIndex = session.getStep() - 1;
        GameSetup.StepType currentType = session.getGameSetup().getStepTypes()[currentIndex];

        // Verarbeitet die Nachricht, wenn eine Chat-Eingabe erwartet wird
        if (currentType == GameSetup.StepType.CHAT) {
            event.setCancelled(true);
            String message = event.getMessage();

            // Holt das Muster für den aktuellen Schritt aus dem Setup
            String pattern = session.getGameSetup().getInputPatterns()[currentIndex];

            // Prüfung: Wenn das Muster null ist ODER die Nachricht zum Muster passt
            if (pattern == null || message.matches(pattern)) {

                // Wir deklarieren eine Variable für den fertigen Wert (Zahl oder Text)
                Object finalValue;

                // Wenn das Muster nur Zahlen zulässt, wandeln wir es um
                if (pattern != null && pattern.contains("[0-9]")) {
                    finalValue = Integer.parseInt(message); // Hier wird aus "5" die echte Zahl 5
                } else {
                    finalValue = message; // Ansonsten bleibt es ein String
                }

                // Jetzt übergeben wir den finalValue (als Object) an die Zentrale
                Bukkit.getScheduler().runTask(newGameCommand.getPlugin(), () -> {
                    newGameCommand.handleSetupStep(player, finalValue);
                });

            } else {
                // Wenn das Muster existiert, aber die Nachricht nicht passt
                player.sendMessage(ChatColor.RED + "Invalid input! Please follow the instructions.");
            }
        }
    }

    // Event-Handler für Spieleraustritte
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Säubert die Daten beim Verlassen des Servers
        removePlayer(event.getPlayer().getUniqueId());
    }
}
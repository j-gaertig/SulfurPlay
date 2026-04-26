package de.jgaertig.sulfurFun.models;

public class SetupSession {

    // Instanzvariablen
    private String arenaName;
    private GameSetup gameSetup;
    private int step = 1;

    // Konstruktor
    public SetupSession(String arenaName, GameSetup gameSetup) {
        this.arenaName = arenaName;
        this.gameSetup = gameSetup;
    }

    // Getter-Methoden
    public String getArenaName() {
        return arenaName;
    }

    public GameSetup getGameSetup() {
        return gameSetup;
    }

    public int getStep() {
        return step;
    }

    // Logik zur Zustandsänderung
    public void nextStep() {
        // Erhöht den aktuellen Schrittzähler um 1
        step++;
    }
}
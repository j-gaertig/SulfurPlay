package de.jgaertig.sulfurFun.models;

public class SetupSession {

    private String arenaName;
    private int step = 1;

    public SetupSession(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaName() { return arenaName; }
    public int getStep() { return step; }

    public void nextStep() { step++; }

}

package de.jgaertig.sulfurFun.models;

public interface GameSetup {

    public enum StepType {
        CLICK, // Für Koordinaten/Blöcke
        CHAT // Für Zahlen/Texteingaben
    }

    String[] getSteps();        // Die Namen der Config-Pfade (z.B. "bluegoal1")
    String[] getMessages();     // Die Nachrichten an den Spieler
    String getType();           // Der Name des Modus (z.B. "football")
    StepType[] getStepTypes(); // Gibt zurück, ob ein Schritt CLICK oder CHAT ist
}
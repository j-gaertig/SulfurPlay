package de.jgaertig.sulfurFun.models;

public class FootballSetup implements GameSetup {
    @Override
    public String getType() { return "football"; }

    @Override
    public String[] getSteps() {
        return new String[]{"bluegoal1", "bluegoal2", "blueplayerspawn", "redgoal1", "redgoal2", "redplayerspawn", "ballspawn", "maxplayer", "touchball"};
    }

    @Override
    public String[] getMessages() {
        return new String[]{
                "Right-click blue goal corner 1",
                "Right-click the OPPOSITE (diagonal) blue goal corner 2", // Klarer Hinweis
                "Right-click blue team's spawn location",
                "Right-click red goal corner 1",
                "Right-click the OPPOSITE (diagonal) red goal corner 2", // Klarer Hinweis
                "Right-click red team's spawn location",
                "Right-click ball spawn location",
                "Type max players per team (1 or more)", // Dein neuer Bereich
                "Allow players to touch the ball with hands? (yes/no)" // Deine neue Abfrage
        };
    }

    @Override
    public StepType[] getStepTypes() {
        return new StepType[]{
                StepType.CLICK, StepType.CLICK, StepType.CLICK, // blue goal 1, 2, spawn
                StepType.CLICK, StepType.CLICK, StepType.CLICK, // red goal 1, 2, spawn
                StepType.CLICK,                                 // ball spawn
                StepType.CHAT, StepType.CHAT                                  // max player
        };
    }

    // Yes/No: "^(yes|no)$" | Zahlen ab 1: "^[1-9][0-9]*$" | egal: null
    @Override
    public String[] getInputPatterns() {
        return new String[] {
                null, null, null,
                null, null, null,
                null,
                "^[1-9][0-9]*$", "^(yes|no)$"
        };
    }


}
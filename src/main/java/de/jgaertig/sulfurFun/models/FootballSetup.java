package de.jgaertig.sulfurFun.models;

public class FootballSetup implements GameSetup {
    @Override
    public String getType() { return "football"; }

    @Override
    public String[] getSteps() {
        return new String[]{"bluegoal1", "bluegoal2", "blueplayerspawn", "redgoal1", "redgoal2", "redplayerspawn", "ballspawn", "maxplayer"};
    }

    @Override
    public String[] getMessages() {
        return new String[]{
                "Right-click blue goal corner 1",
                "Right-click blue goal corner 2",
                "Right-click blue spawn",
                "Right-click red goal corner 1",
                "Right-click red goal corner 2",
                "Right-click red spawn",
                "Right-click ball spawn",
                "Type max players in chat"
        };
    }

    @Override
    public StepType[] getStepTypes() {
        return new StepType[]{
                StepType.CLICK, StepType.CLICK, StepType.CLICK, // blue goal 1, 2, spawn
                StepType.CLICK, StepType.CLICK, StepType.CLICK, // red goal 1, 2, spawn
                StepType.CLICK,                                 // ball spawn
                StepType.CHAT                                   // max player
        };
    }
}
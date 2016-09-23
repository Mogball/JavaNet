package Misc;

import org.newdawn.slick.command.Command;

public class Action implements Command {

    private final Execution action;

    public Action(Execution action) {
        this.action = action;
    }

    public void execute() {
        action.execute();
    }

}

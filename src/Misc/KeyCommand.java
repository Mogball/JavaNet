package Misc;

import org.newdawn.slick.command.Command;

public class KeyCommand implements Command {

    private final int key;

    public KeyCommand(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}

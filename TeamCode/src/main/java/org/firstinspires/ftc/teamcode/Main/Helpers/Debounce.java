package org.firstinspires.ftc.teamcode.Main.Helpers;

public class Debounce {
    private boolean previousState = false;

    public boolean isPressed(boolean currentState) {
        boolean isPressed = currentState && !previousState;
        previousState = currentState;
        return isPressed;
    }
}

package ija.ijaProject.game.simulation;

/**
 * Utility class to manage the simulation mode state.
 * This class provides a global flag to indicate whether the game is currently running in simulation mode.
 */
public class SimulationState {
    private static boolean isSimulationMode = false;

    /**
     * Returns whether the simulation mode is active.
     *
     * @return {@code true} if simulation mode is enabled; {@code false} otherwise
     */
    public static boolean isSimulationMode() {
        return isSimulationMode;
    }

    public static void setSimulationMode(boolean mode) {
        isSimulationMode = mode;
    }
}
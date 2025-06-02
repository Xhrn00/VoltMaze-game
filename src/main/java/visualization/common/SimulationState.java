package visualization.common;

public class SimulationState {
    private static boolean isSimulationMode = false;

    public static boolean isSimulationMode() {
        return isSimulationMode;
    }

    public static void setSimulationMode(boolean mode) {
        isSimulationMode = mode;
    }
}
package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;
import frc.robot.commands.TeleopSwerve;

import java.util.LinkedList;

public class TranslationPID {
        
    public static PIDController strafePID = createPIDController();
    int windowSize = 20;
    LinkedList<Double> errorHistory = new LinkedList<>();

    private static PIDController createPIDController() {
        PIDController pid = new PIDController(1, 0.5, 0.2);
        pid.setTolerance(0.1); // allowable distance error
        pid.enableContinuousInput(0, 360); // it is faster to go 1 degree from 359 to 0 instead of 359 degrees
        pid.setSetpoint(TeleopSwerve.trapShootDistance); // 0 = apriltag distance "goal"
        return pid;
    }



    public double getFilteredError(double newError) {
        errorHistory.add(newError);
        if (errorHistory.size() > windowSize) {
            errorHistory.poll();
        }
        return errorHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }



    public double getT() {
        double calculatedValue = strafePID.calculate(getFilteredError(limelightData.distance2d));
        return calculatedValue;
    }
}

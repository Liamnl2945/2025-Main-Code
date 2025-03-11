package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;

import java.util.LinkedList;


public class AprilTagPointLock {

    public static PIDController rotationPID = createPIDController();
    int windowSize = 20;
    LinkedList<Double> errorHistory = new LinkedList<>();

    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.005, 0.001, 0.0005);//good values, further tuning would be optimal but unnecessary. Slight oscillation but good response with these.
        pid.setTolerance(0.5); // allowable angle error
        pid.enableContinuousInput(0, 360); // it is faster to go 1 degree from 359 to 0 instead of 359 degrees
        pid.setSetpoint(0); // 0 = apriltag angle/offset
        return pid;
    }


    public double getFilteredError(double newError) {
        errorHistory.add(newError);
        if (errorHistory.size() > windowSize) {
            errorHistory.poll();
        }
        return errorHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getR() {
            double calculatedValue = rotationPID.calculate(getFilteredError(limelightData.TagXOffset));
            return -calculatedValue;//Sign needs to change based off swerve orientation

    }
}


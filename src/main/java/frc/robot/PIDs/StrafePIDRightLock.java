

package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;

import java.util.LinkedList;

public class StrafePIDRightLock {

    public static PIDController strafePID = createPIDController();

    int windowSize = 20;
    LinkedList<Double> errorHistory = new LinkedList<>();

    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.08, 0.004, 0.001);
        pid.setTolerance(8); // allowable angle error
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



    public double getS() {
        double calculatedValue = strafePID.calculate(-limelightData.algaeXOffset);
        System.out.println(calculatedValue);
        return calculatedValue;
    }

}

package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;

public class StrafePID {
    
    public static PIDController strafePID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.08, 0.004, 0.001);
        pid.setTolerance(8); // allowable angle error
        pid.enableContinuousInput(0, 360); // it is faster to go 1 degree from 359 to 0 instead of 359 degrees
        pid.setSetpoint(0); // 0 = apriltag angle/offset
        return pid;
    }
  
    public double getS() {
        double calculatedValue = strafePID.calculate(-limelightData.TagYaw);
        System.out.println(calculatedValue);
        return calculatedValue;
    }

}

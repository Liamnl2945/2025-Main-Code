package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;
import frc.robot.commands.TeleopSwerve;

public class TranslationPID {
        
    public static PIDController strafePID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(1, 0.5, 0.2);
        pid.setTolerance(0.1); // allowable distance error
        pid.enableContinuousInput(0, 360); // it is faster to go 1 degree from 359 to 0 instead of 359 degrees
        pid.setSetpoint(TeleopSwerve.trapShootDistance); // 0 = apriltag distance "goal"
        return pid;
    }
  
    public double getT() {
        double calculatedValue = strafePID.calculate((limelightData.distance2d));
        return calculatedValue;
    }
}

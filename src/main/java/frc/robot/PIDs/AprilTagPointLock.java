package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.limelightData;
import frc.robot.RobotContainer;

public class AprilTagPointLock {

    public static PIDController rotationPID = createPIDController();

    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.005, 0.001, 0.0005);//good values, further tuning would be optimal but unnecessary. Slight oscillation but good response with these.
        pid.setTolerance(0.5); // allowable angle error
        pid.enableContinuousInput(0, 360); // it is faster to go 1 degree from 359 to 0 instead of 359 degrees
        pid.setSetpoint(0); // 0 = apriltag angle/offset
        return pid;
    }

    public double getR() {
        if (RobotContainer.aim.getAsBoolean()) {//Variable aim is for tag lineup & spinner windup, autointake is for tag. If both are pressed simultaneously, 'aim' takes priority. Since this PID is only called if teleop swerve is successfully overwritten, we only need to check for one variable.
            double calculatedValue = rotationPID.calculate(limelightData.TagXOffset);
            return -calculatedValue;//Sign needs to change based off swerve orientation
        } else {
            double calculatedValue = rotationPID.calculate(limelightData.NoteXOffset);
            return -calculatedValue;//Sign needs to change based off swerve orientation
        }
    }
}


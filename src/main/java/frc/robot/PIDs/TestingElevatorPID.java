package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.RobotContainer;
import frc.robot.commands.TeleopSwerve;
import frc.robot.limelightData;
import frc.robot.subsystems.Elevator;

public class TestingElevatorPID {
        
    public static PIDController elevatorPID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.035  , 0.001, 0.00005);
        pid.setTolerance(0.05); // allowable distance error
        pid.setSetpoint(0);
        return pid;
    }

    public double getSpeed(double error) {
        double rawOutput = elevatorPID.calculate(error);
        double maxOutput = 1; // Example maximum absolute value of the PID output
        System.out.println("PID USED WITH ERROR: " + error + " AND OUTPUT: " + -(rawOutput/maxOutput));
        return -(rawOutput/maxOutput); // Scales the output to [-1, 1]
    }


}

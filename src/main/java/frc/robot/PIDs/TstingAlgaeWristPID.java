package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;

public class TstingAlgaeWristPID {
        
    public static PIDController algaeWristPID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.1, 0.0002, 0.0003);
        pid.setTolerance(0.1); // allowable distance error
        pid.setSetpoint(0);
        return pid;
    }

    public double getSpeed(double error) {

        double rawOutput = algaeWristPID.calculate(error);
        double maxOutput = 1; // Example maximum absolute value of the PID output
        //System.out.println("PID USED WITH ERROR: " + error + " AND OUTPUT: " + -(rawOutput/maxOutput));
        return -(rawOutput/maxOutput); // Scales the output to [-1, 1]
    }


}

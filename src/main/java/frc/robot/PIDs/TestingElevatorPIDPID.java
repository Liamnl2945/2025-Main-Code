package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;

public class TestingElevatorPIDPID {
        
    public static PIDController elevatorPIDPID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.5, 0.1, 0.1);
        pid.setTolerance(0.05); // allowable distance error
        pid.setSetpoint(0);
        return pid;
    }


    public double getSpeed(double error) {
        System.out.println("DOUBLE PID USED WITH ERROR: " + error + " AND OUTPUT: " + elevatorPIDPID.calculate(error));
        return  elevatorPIDPID.calculate(error);
    }


}

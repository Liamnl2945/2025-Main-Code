package frc.robot.PIDs;

import edu.wpi.first.math.controller.PIDController;

public class TestingElevatorPIDPID {
        
    public static PIDController elevatorPIDPID = createPIDController();
    private static PIDController createPIDController() {
        PIDController pid = new PIDController(0.005, 0.001, 0.0015);
        pid.setTolerance(0.05); // allowable distance error
        pid.setSetpoint(0);
        return pid;
    }

    public double getSpeed(double error) {
        return  elevatorPIDPID.calculate(error);
    }


}

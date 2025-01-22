package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class ElevatorCom extends Command {

    private final Elevator Elevator;
    public final Joystick manipulator; 

    public ElevatorCom(Elevator elevator, Joystick manipulator){
        
        this.Elevator = elevator;
        this.manipulator = manipulator;

        addRequirements(elevator);
    }

    @Override
    public void execute(){
        double elevatorSpeed = manipulator.getRawAxis(1);
    

    if (Math.abs(elevatorSpeed) < 0.15) {
        elevatorSpeed = 0;
    }

        frc.robot.subsystems.Elevator.runElevator(elevatorSpeed);
    }
    
}

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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
        int stickButton = 0;
        JoystickButton left = new JoystickButton(manipulator, XboxController.Button.kLeftStick.value);
        JoystickButton right = new JoystickButton(manipulator, XboxController.Button.kRightStick.value);

        if(right.getAsBoolean()){
            stickButton = -1;
        }
        else if(left.getAsBoolean()){
            stickButton = 1;
        }



        frc.robot.subsystems.Elevator.runElevator(elevatorSpeed, stickButton);
    }
    
}

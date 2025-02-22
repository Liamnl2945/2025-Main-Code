package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.AlgaeIntake;

public class AlgaeIntakeCom extends Command {

    public AlgaeIntake algaeIntake;
    public Joystick manipulator;

    public AlgaeIntakeCom(AlgaeIntake algaeIntake, Joystick manipulator){

        this.algaeIntake = algaeIntake;
        this.manipulator = manipulator;

        addRequirements(algaeIntake);
    }

    @Override
    public void execute() {
        boolean dpadNot = RobotContainer.dpadNull.getAsBoolean();
        boolean dpadUp = RobotContainer.dpadUp.getAsBoolean();
        boolean dpadDown = RobotContainer.dpadDown.getAsBoolean();
        if(dpadUp){
            frc.robot.subsystems.AlgaeIntake.RunAlgaeIntake(1);
        }
        else if(dpadDown){
            frc.robot.subsystems.AlgaeIntake.RunAlgaeIntake(-1);
        }
        else{
            frc.robot.subsystems.AlgaeIntake.RunAlgaeIntake(0);
        }
    }
}

/*package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Elevator;

public class autoL3 extends Command {


    public autoL3(){
        frc.robot.subsystems.Elevator.autoL3();
    }


}
*/
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Elevator;

public class autoL3 extends InstantCommand {

    private final Elevator elevator;

    public autoL3(Elevator elevator) {
        this.elevator = elevator;
    }
    @Override
    public void execute() {
        elevator.autoL3(); // Call the autoL3 function within the command
    }
}
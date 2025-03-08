package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.I2CBruh;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.LIGHT;

public class I2CCom extends Command {
    public final Joystick manipulator;
    public final I2CBruh I2C;
    public I2CCom(I2CBruh I2C, Joystick manipulator){
        this.manipulator = manipulator;
        this.I2C = I2C;
        addRequirements(I2C);

    }
    @Override
    public void execute(){
        JoystickButton left = new JoystickButton(manipulator, XboxController.Button.kLeftStick.value);
        frc.robot.subsystems.I2CBruh.PrintColorData(left.getAsBoolean());
    }
}

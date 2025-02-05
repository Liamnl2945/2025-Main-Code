package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.I2CBruh;
import frc.robot.subsystems.LIGHT;

public class I2CCom extends Command {
    private I2CBruh I2C = new I2CBruh();


    public I2CCom(I2CBruh I2C){

        this.I2C = I2C;

        addRequirements(I2C);
    }
    @Override
    public void execute(){
        frc.robot.subsystems.I2CBruh.PrintColorData();
    }
}

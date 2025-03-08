package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LIGHT;

public class LEDCom extends Command {

     private final LIGHT leds;

    public LEDCom(LIGHT leds){

        this.leds = leds;

        addRequirements(leds);
    }

    @Override
    public void execute(){
    //    frc.robot.subsystems.LIGHT.runLeds();
    }

}

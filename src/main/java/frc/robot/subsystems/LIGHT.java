package frc.robot.subsystems;

import com.ctre.phoenix.led.*;
import frc.robot.RobotContainer;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;
import frc.robot.subsystems.Elevator;

public class LIGHT extends SubsystemBase {
    public static CANdle led;
    public static FireAnimation fireAnimation;
    static double elevatorSetting;
    public LIGHT (){
        led = new CANdle(1);
        CANdleConfiguration config = new CANdleConfiguration();
        config.stripType = CANdle.LEDStripType.RGB;
        config.statusLedOffWhenActive = true; // Turns off status LED
        led.configAllSettings(config);
        fireAnimation = new FireAnimation();
    }

    public static void runLeds(){
        elevatorSetting = Elevator.selected;
        switch( "" + (int) elevatorSetting){
            case "0":
                led.setLEDs(100, 100, 100);

            case "5.32":
                led.setLEDs(0, 255, 0);
                break;
            case "14.6":
                led.setLEDs(0, 255, 255);
                break;
            case "22.25":
                led.setLEDs(255, 0, 255);
                break;
            case "31.5":
                led.setLEDs(255, 0, 0);
                break;
            case "-1":
                led.setLEDs(255, 255, 255);
                break;
        }


    }
}

package frc.robot.subsystems;

import frc.robot.RobotContainer;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.Animation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;


public class LEDSubsystem extends SubsystemBase{//NOTE the CANdle has 8 built in LEDs, so any external LED's will be starting on index "8" since the Candle already uses 0-7.

    // Create a rainbow animation
    private static final Animation rainbowAnimation = new RainbowAnimation(1, 1, 60);
    private static final Animation larsonAnimation = new LarsonAnimation(255, 255, 255, 3, 1, /*TODO SET*/3, BounceMode.Front, 3, 1);


    
    public LEDSubsystem() {
        CANdle candle = new CANdle(constants.CANdle.CANdle);
        FireAnimation fireAnimation = new FireAnimation();
        candle.animate(fireAnimation);
    }

//    public void animate(){
//        if(RobotContainer.aim.getAsBoolean() && temp != rainbowAnimation) {
//            temp = rainbowAnimation;
//            candle.animate(rainbowAnimation);
//        }
//        else if(RobotContainer.autoIntake.getAsBoolean() && temp != fireAnimation) {//basic LED test code
//            temp = fireAnimation;
//            candle.animate(fireAnimation);
//        }
//        else if(temp != larsonAnimation && !RobotContainer.aim.getAsBoolean() && !RobotContainer.autoIntake.getAsBoolean()){
//            temp = larsonAnimation;
//            candle.animate(larsonAnimation);
//        }
//    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
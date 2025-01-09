package frc.robot.subsystems;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;


public class Climber extends SubsystemBase{   
   private static Pigeon2 gyro;
   private static double skew;
   public static final DigitalInput botLimitSwitch = new DigitalInput(constants.Climber.botLimitSwitch);
    
   static TalonFX climberLeft = new TalonFX(constants.Climber.climberLeft);
         
       // static TalonFX climberRight = new TalonFX(constants.Climber.climberRight);
        //public Pigeon2 gyro;


        public Climber(){
        climberLeft.setInverted(true);
        //climberRight.setInverted(true);
        // make the movement not inverted
        climberLeft.setNeutralMode(NeutralModeValue.Brake);
        //climberRight.setNeutralMode(NeutralModeValue.Brake);
        // brake if speed is 0
     }

     public static void climbUpLeft(double speed){
      //System.out.println(botLimitSwitch.get() + " " + speed);

     climberLeft.set(0);

      if(speed < 0){ // if up
         climberLeft.set(speed);
         //System.out.println("GOING UP");
      }
      else if(speed > 0 && !botLimitSwitch.get()){ //if down ad no switch
        climberLeft.set(speed);
         //System.out.println("GOING DOWN");

      }
      else if(speed > 0 && botLimitSwitch.get()){ //if down and switch
       climberLeft.set(0);
         //System.out.println("HIT THE BOTTOM & GOING DOWN");

      }
      else if(botLimitSwitch.get()){
         climberLeft.set(0);
        // System.out.println("HIT THE LIMIT SWITch");

      }
     }
     
    
}




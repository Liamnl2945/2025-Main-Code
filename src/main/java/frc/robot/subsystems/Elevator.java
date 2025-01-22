package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;

public class Elevator extends SubsystemBase {

    private final static TalonFX elevatorMotor = new TalonFX(constants.Elevator.elevator);
        
        public Elevator(){
            elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
        }
    
        public static void runElevator(double speed){
    

        if(Math.abs(speed) < 0.1){ 
            elevatorMotor.set(0); 
        }
        else{
            elevatorMotor.set(-speed);
        } 
    }
}

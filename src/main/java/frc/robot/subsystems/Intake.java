package frc.robot.subsystems;



import frc.robot.constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private final WPI_TalonSRX intakeTop = new WPI_TalonSRX(constants.Intake.intakeTop);
    private final WPI_TalonSRX intakeBot = new WPI_TalonSRX(constants.Intake.intakeBot);
    private final double speed = 1.0;

public Intake(){
    intakeTop.configFactoryDefault();
    intakeBot.configFactoryDefault();

    intakeTop.setInverted(true);
    intakeBot.setInverted(true);
    
    intakeTop.setNeutralMode(NeutralMode.Brake);
    intakeBot.setNeutralMode(NeutralMode.Brake);
}

public void runIntake() {
    intakeTop.set(speed);
    intakeBot.set(speed);
    //System.out.println("RUN INTAKE \n\n\n");
}

public void reverseIntake() {
    intakeTop.set(-speed);
    intakeBot.set(-speed);
    //System.out.println("REVERSE INTAKE \n\n\n");
}
public void slowReverseIntake(){
    intakeTop.set(-.2);
    intakeBot.set(-.2);
    //System.out.println("REVERSE INTAKE SLOW \n\n\n");
}

public void stopIntake(){
    intakeTop.set(0);
    intakeBot.set(0);
}




}
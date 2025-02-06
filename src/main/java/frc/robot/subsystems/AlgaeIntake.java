package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants;
import frc.robot.RobotContainer;

public class AlgaeIntake extends SubsystemBase {
    private final static TalonFX elevatorMotor = new TalonFX(constants.AlgaeIntake.intakeArm);
    private static VictorSPX top = new VictorSPX(constants.AlgaeIntake.intakeTop);
    private static VictorSPX bottom = new VictorSPX(constants.AlgaeIntake.intakeBot);

    public AlgaeIntake(){
        top.setInverted(true);
        bottom.setInverted(true);
    }

    public static void RunAlgaeIntake(){
        if(RobotContainer.dpad == 0) {
            elevatorMotor.set(0.09);
        }
        if(RobotContainer.dpad== -1) {
            elevatorMotor.set(0);
        }
        if(RobotContainer.dpad == 180) {
            elevatorMotor.set(-0.09);
        }


        if(RobotContainer.leftTriggerAxis > 0.9){
            // out take
            /* top.set(VictorSPXControlMode.Velocity, 0.5); // one method
            bottom.set(VictorSPXControlMode.Velocity, -0.5); */
            top.set(ControlMode.PercentOutput, 0.5);  // the other method
            bottom.set(ControlMode.PercentOutput, -0.5);

        } else if (RobotContainer.rightTriggerAxis > 0.9) {
            // intake
            /* top.set(VictorSPXControlMode.Velocity, -0.5); // one method
            bottom.set(VictorSPXControlMode.Velocity, 0.5); */
            top.set(ControlMode.PercentOutput, -0.5);  // the other method
            bottom.set(ControlMode.PercentOutput, 0.5);
        }else if (RobotContainer.leftTriggerAxis < 0.9 || RobotContainer.rightTriggerAxis < 0.9) {
            /* top.set(VictorSPXControlMode.Velocity, 0); // one method
            bottom.set(VictorSPXControlMode.Velocity, 0); */
            top.set(ControlMode.PercentOutput, 0);  // the other method
            bottom.set(ControlMode.PercentOutput, 0);
        } // motor mania
    }
}

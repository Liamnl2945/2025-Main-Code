package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PIDs.TestingElevatorPID;
import frc.robot.PIDs.TstingAlgaeWristPID;
import frc.robot.Robot;
import frc.robot.constants;
import frc.robot.RobotContainer;

public class AlgaeIntake extends SubsystemBase {
    private final static TalonFX algaeWrist = new TalonFX(constants.AlgaeIntake.intakeArm, "rio");
    private static VictorSPX top = new VictorSPX(constants.AlgaeIntake.intakeTop);
    private static VictorSPX bottom = new VictorSPX(constants.AlgaeIntake.intakeBot);
    private static String tsSoFweakingStateVariable = "Manual";
    private static TstingAlgaeWristPID pid = new TstingAlgaeWristPID();
    private static double targetPos = 0;

    public AlgaeIntake(){
        top.setInverted(true);
        bottom.setInverted(true);
    }

    public static void RunAlgaeIntake(double speed){
        if(!RobotContainer.algaeLimitSwitch.get()){
            System.out.println("Algae limit switch pressed");
        }
        if(tsSoFweakingStateVariable.equals("Calibrating")) {
                // algaeWrist.set(0.04);

                if (!RobotContainer.algaeLimitSwitch.get()) {
                    for(int i=0; i < 6+9; i++) {
                        System.out.println("boi you touching the limit switch ts so tuff");
                        algaeWrist.set(-0);
                        algaeWrist.setPosition(-0);
                        tsSoFweakingStateVariable = "Manual";
                    }
                }
        }
// freaking > fweaking
        if(tsSoFweakingStateVariable.equals("Manual")) {

            if(Math.abs(speed) > 0.1){
                targetPos += (speed/10);
            }
            algaeWrist.set(pid.getSpeed(targetPos - algaeWrist.getPosition().getValueAsDouble()));
            if(RobotContainer.tsSoAlgaeCalibrate.getAsBoolean()) {
                tsSoFweakingStateVariable = "Calibrating";
            }
            if (RobotContainer.leftTriggerAxis.getAsBoolean()) {
                System.out.println("ALGAE outtaking: " + RobotContainer.leftTriggerAxis);
                // out take
            /* top.set(VictorSPXControlMode.Velocity, 0.5); // one method
            bottom.set(VictorSPXControlMode.Velocity, -0.5); */
                top.set(ControlMode.PercentOutput, -0.5);  // the other method
                bottom.set(ControlMode.PercentOutput, 0.5);

            } else if (RobotContainer.rightTriggerAxis.getAsBoolean()) {
                System.out.println("ALGAE INTAKING: " + RobotContainer.rightTriggerAxis);
                // intake
            /* top.set(VictorSPXControlMode.Velocity, -0.5); // one method
            bottom.set(VictorSPXControlMode.Velocity, 0.5); */
                top.set(ControlMode.PercentOutput, 0.5);  // the other method
                bottom.set(ControlMode.PercentOutput, -0.5);
            } else if (!RobotContainer.leftTriggerAxis.getAsBoolean() && !RobotContainer.rightTriggerAxis.getAsBoolean()) {
            /* top.set(VictorSPXControlMode.Velocity, 0); // one method
            bottom.set(VictorSPXControlMode.Velocity, 0); */
                top.set(ControlMode.PercentOutput, 0);  // the other method
                bottom.set(ControlMode.PercentOutput, 0);
            } // motor mania
        }
    }
}

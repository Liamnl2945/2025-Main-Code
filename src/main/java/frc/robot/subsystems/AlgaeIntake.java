package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PIDs.TestingElevatorPID;
import frc.robot.PIDs.TstingAlgaeWristPID;
import frc.robot.Robot;
import frc.robot.constants;
import frc.robot.RobotContainer;

public class AlgaeIntake extends SubsystemBase {
    private final static TalonFX algaeWrist = new TalonFX(constants.AlgaeIntake.intakeArm);
    private static VictorSPX top = new VictorSPX(constants.AlgaeIntake.intakeTop);
    private static VictorSPX bottom = new VictorSPX(constants.AlgaeIntake.intakeBot);
    private static String tsSoFweakingStateVariable = "Manual";
    private static TstingAlgaeWristPID pid = new TstingAlgaeWristPID();
    private static double targetPos = 0;
    private static boolean continuousIntake = false;
    private static double maxAlgaeArmRotations = 10;
    private static double intakeOuttakeSpeedManual = 0.5;
    private static double intakeOuttakeSpeedContinuous = 0.2;

    public AlgaeIntake(){
        algaeWrist.setNeutralMode(NeutralModeValue.Brake);
        top.setInverted(true);
        bottom.setInverted(true);
    }

    public static void RunAlgaeIntake(double speed){
        if(!RobotContainer.algaeLimitSwitch.get()){
            System.out.println("Algae limit switch pressed");
        }
        if(tsSoFweakingStateVariable.equals("Calibrating")) {
                // algaeWrist.set(0.04); //te auto calibrate speed; needs to go until it hits the switch

                if (!RobotContainer.algaeLimitSwitch.get()) {
                    for(int i=0; i < 6+9; i++) {
                        System.out.println("boi you touching the limit switch ts so tuff");
                        algaeWrist.set(0);
                        algaeWrist.setPosition(0);
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

            if(targetPos >= maxAlgaeArmRotations) {//limiting rotations of the algae arm so it doesn't break itself
                targetPos = maxAlgaeArmRotations;
            }else if(targetPos <= 0){
                targetPos = 0;
            }
            if(RobotContainer.tsSoAlgaeCalibrate.getAsBoolean()) {
                //tsSoFweakingStateVariable = "Calibrating"; TODO fix when limit switch added
            }
            if (RobotContainer.leftTriggerAxis.getAsBoolean()) {
                // out take
                top.set(ControlMode.PercentOutput, -intakeOuttakeSpeedManual);  // the other method
                bottom.set(ControlMode.PercentOutput, intakeOuttakeSpeedManual);
                continuousIntake = false;
            } else if (RobotContainer.rightTriggerAxis.getAsBoolean()) {
                // intake
                top.set(ControlMode.PercentOutput, intakeOuttakeSpeedManual);
                bottom.set(ControlMode.PercentOutput, -intakeOuttakeSpeedManual);
                continuousIntake = true;
            } else if (!continuousIntake){
                top.set(ControlMode.PercentOutput, intakeOuttakeSpeedManual);
                bottom.set(ControlMode.PercentOutput, intakeOuttakeSpeedManual);
            }
            else if(continuousIntake){
                top.set(ControlMode.PercentOutput, intakeOuttakeSpeedContinuous);
                bottom.set(ControlMode.PercentOutput, -intakeOuttakeSpeedContinuous);
            }
        }
    }
}

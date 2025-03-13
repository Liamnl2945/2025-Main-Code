package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.Timer;
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
    private static String tsSoFweakingStateVariable = "Calibrating";
    private static TstingAlgaeWristPID pid = new TstingAlgaeWristPID();
    private static double targetPos = 0;
    private static double maxAlgaeArmRotations = -0.768;
    private static int themove = 0;



    public AlgaeIntake(){
        algaeWrist.setNeutralMode(NeutralModeValue.Brake);
        top.setInverted(true);
        bottom.setInverted(true);
    }

    public static void RunAlgaeIntake(double speed){
        System.out.println(RobotContainer.algaeLimitSwitch.get());
            if(tsSoFweakingStateVariable.equals("Calibrating")) {
                System.out.println(tsSoFweakingStateVariable);
                algaeWrist.set(-0.05); // auto-calibrate speed; needs to go until it hits the switch
                if (RobotContainer.algaeLimitSwitch.get()) {
                    algaeWrist.set(0);
                    algaeWrist.setPosition(0);
                    tsSoFweakingStateVariable = "Manual";
                    targetPos = 0;
                }
            }

            if(tsSoFweakingStateVariable.equals("Manual")) {
                //System.out.println(RobotContainer.algaeLimitSwitch.get());
                if(Math.abs(speed) > 0.1) {
                    targetPos += (speed / 5);
                }
               // if(targetPos < 0){
               //     targetPos = 0;
               // }else if(targetPos > maxAlgaeArmRotations){
                //    targetPos = maxAlgaeArmRotations;
             //   }
                algaeWrist.set(pid.getSpeed(targetPos - algaeWrist.getPosition().getValueAsDouble()));

                if(RobotContainer.tsSoAlgaeCalibrate.getAsBoolean() && !RobotContainer.heightToggle.getAsBoolean()) {
                    System.out.println("I have brain cancer"); // me too buddy, me too
                    tsSoFweakingStateVariable = "Calibrating";
                }

                // Intake/Outtake control
                if(RobotContainer.leftTriggerAxis.getAsBoolean()) {
                   //top.set(ControlMode.PercentOutput, -1);
                   //bottom.set(ControlMode.PercentOutput, 1);
                    themove = 2;
                } else if (RobotContainer.rightTriggerAxis.getAsBoolean()) {
                    //top.set(ControlMode.PercentOutput, 1);
                    //bottom.set(ControlMode.PercentOutput, -1);
                    themove = 1;
                }
                if(themove == 1){
                    top.set(ControlMode.PercentOutput, 0.3);
                    bottom.set(ControlMode.PercentOutput, -0.3);
                    if(!RobotContainer.rightTriggerAxis.getAsBoolean()){
                        themove = 3;
                    }
                }else if(themove == 2){
                    top.set(ControlMode.PercentOutput, -1);
                    bottom.set(ControlMode.PercentOutput, 1);
                    if(!RobotContainer.leftTriggerAxis.getAsBoolean()){
                        themove = 0;
                    }
                }else if(themove == 3){
                    top.set(ControlMode.PercentOutput, 0.25);
                    bottom.set(ControlMode.PercentOutput, -0.25);
                }else if(themove == 0){
                    top.set(ControlMode.PercentOutput, 0);
                    bottom.set(ControlMode.PercentOutput, 0);
                }
            }


    }
}

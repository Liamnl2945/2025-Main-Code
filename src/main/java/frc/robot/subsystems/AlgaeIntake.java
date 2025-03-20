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
    private static TalonFX top = new TalonFX(constants.AlgaeIntake.intakeTop);
    private static VictorSPX bottom = new VictorSPX(constants.AlgaeIntake.intakeBot);
    private static String tsSoFweakingStateVariable = "Calibrating";
    private static TstingAlgaeWristPID pid = new TstingAlgaeWristPID();
    private static double targetPos = 0;
    private static double maxAlgaeArmRotations = 4;

    private static Timer intakeTimer = new Timer();
     // Update every 50ms


    public AlgaeIntake(){
        algaeWrist.setNeutralMode(NeutralModeValue.Brake);
        //top.setInverted(true);
        bottom.setInverted(true);
        intakeTimer.start();
    }

    public static void RunAlgaeIntake(double speed){
        System.out.println("Rotation " + targetPos);
        //System.out.println(RobotContainer.algaeLimitSwitch.get());
            if(tsSoFweakingStateVariable.equals("Calibrating")) {
                System.out.println(tsSoFweakingStateVariable);
                algaeWrist.set(-0.1); // auto-calibrate speed; needs to go until it hits the switch
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
                    System.out.println("I have brain cancer");
                    tsSoFweakingStateVariable = "Calibrating";
                }

                if (targetPos >= maxAlgaeArmRotations  ) {
                    targetPos -= speed/5;
                }
                if(targetPos < 0) {
                    targetPos += speed/5;
                }
                if(RobotContainer.algaeLimitSwitch.get() && algaeWrist.get() < 0) {
                    algaeWrist.set(0);
                }


                // Intake/Outtake control
                if(RobotContainer.leftTriggerAxis.getAsBoolean()) {
                   top.set(1);
                  // bottom.set(ControlMode.PercentOutput, 1);
                } else if (RobotContainer.rightTriggerAxis.getAsBoolean()) {
                    top.set(-0.5);
                   //bottom.set(ControlMode.PercentOutput, -1);
                } else {
                   top.set(0);
                    //bottom.set(ControlMode.PercentOutput, 0);
                }
            }


    }
}

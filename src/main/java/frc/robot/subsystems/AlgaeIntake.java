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
    private static double maxAlgaeArmRotations = 10;

    private static Timer intakeTimer = new Timer();
    private static final double INTAKE_UPDATE_INTERVAL = 0.05;  // Update every 50ms


    public AlgaeIntake(){
        algaeWrist.setNeutralMode(NeutralModeValue.Brake);
        top.setInverted(true);
        bottom.setInverted(true);
        intakeTimer.start();
    }

    public static void RunAlgaeIntake(double speed){
        if (intakeTimer.get() >= INTAKE_UPDATE_INTERVAL) {
            if(tsSoFweakingStateVariable.equals("Calibrating")) {
                algaeWrist.set(-0.04); // auto-calibrate speed; needs to go until it hits the switch
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
                algaeWrist.set(pid.getSpeed(targetPos - algaeWrist.getPosition().getValueAsDouble()));

                if(RobotContainer.tsSoAlgaeCalibrate.getAsBoolean()) {
                    tsSoFweakingStateVariable = "Calibrating";
                }

                // Intake/Outtake control
                if(RobotContainer.leftTriggerAxis.getAsBoolean()) {
                   top.set(ControlMode.PercentOutput, -0.5);
                   bottom.set(ControlMode.PercentOutput, 0.5);
                } else if (RobotContainer.rightTriggerAxis.getAsBoolean()) {
                    top.set(ControlMode.PercentOutput, 0.5);
                   bottom.set(ControlMode.PercentOutput, -0.5);
                } else {
                   top.set(ControlMode.PercentOutput, 0);
                    bottom.set(ControlMode.PercentOutput, 0);
                }
            }
            intakeTimer.reset();  // Reset the timer after every update
        }
    }
}

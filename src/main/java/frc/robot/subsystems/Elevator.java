package frc.robot.subsystems;






import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.PIDs.TestingElevatorPID;
import frc.robot.PIDs.TestingElevatorPIDPID;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;

public class Elevator extends SubsystemBase {
    static double selected = -1;
    public static double height = 0;
    public static double intook = 0;
    private static I2CBruh colorSensor = new I2CBruh();
    private static TestingElevatorPID pid = new TestingElevatorPID();
    private static TestingElevatorPIDPID pidForDaPid = new TestingElevatorPIDPID();

    private final static TalonFX elevatorMotor = new TalonFX(constants.Elevator.elevator, "rio");
    private final static TalonFX indexerMotor = new TalonFX(constants.Elevator.indexer, "rio");

    public Elevator() {
        indexerMotor.setNeutralMode(NeutralModeValue.Brake);
        elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
        elevatorMotor.setPosition(0);

    }
//among us

    public static void runElevator(double speed, int stickButton) {
        height = elevatorMotor.getPosition().getValueAsDouble() / 165;
        //  System.out.println(stickButton);
        if (intook == 0 || intook == 1) {
            switch (stickButton) {
                case -1:
                    intook = 1;
                    break;
                case 1:
                    intook = 2;
                    break;
            }
        }
        if (RobotContainer.heightToggle.getAsBoolean()) {
            if (RobotContainer.L1.getAsBoolean()) {//FOR ALL VALUES OF SELECTED, they are target rotations for the PID. For example, if L1 sets selected to 10, then it will raise the arm 10 motor rotations high.
                selected = 34;
            }
            if (RobotContainer.L2.getAsBoolean()) {
                selected = 70;
            }
            if (RobotContainer.L3.getAsBoolean()) {
                selected = 115;
            }
            if (RobotContainer.L4.getAsBoolean()) {
                selected = 165;//HIGHEST POSSIBLE
            }
            if (RobotContainer.L0.getAsBoolean()) {
                selected = 0;
            }
        }

        if (Math.abs(speed) >= 0.1) {
            selected = -1;
        }
        if (RobotContainer.L0.getAsBoolean() && !RobotContainer.heightToggle.getAsBoolean()) {
            selected = -2;
        }

        if (selected != -1 && selected != -2) {
            elevatorMotor.set(pid.getSpeed(pidForDaPid.getSpeed(selected - elevatorMotor.getPosition().getValueAsDouble())));//pass in the error as a function of the distance between our current rotations and the setpoint rotation
            //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
        } else if (selected == -2) {
            elevatorMotor.set(-0.02);
            if (RobotContainer.elevatorLimitSwitch.get()) {
                elevatorMotor.set(0);
                elevatorMotor.setPosition(0);
                //System.out.println("boi you touching the limit switch ts so tuff");
                selected = 0;
            }
        } else {
            if (Math.abs(speed) < 0.1) {
                elevatorMotor.set(0);
                //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
            } else {
                elevatorMotor.set(speed / 5);
            }
            //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
        }
        if (intook == 1) {
            indexerMotor.set(0.3);
            if (colorSensor.coralDetected) {
                indexerMotor.set(0);
                intook = 3;
            }
        } else if (intook == 2) {
            indexerMotor.set(-0.3);
            intook = 0;
        } else if (intook == 0) {
            indexerMotor.set(0);
        } else if (intook == 3) {
            if (stickButton == -1) {
                indexerMotor.set(0.3);
            }
            if (!colorSensor.coralDetected) {
                intook = 0;
                indexerMotor.set(0);
            }
        }


    }

    public static void autoL3() {
        selected = 115;
        while (elevatorMotor.getPosition().getValueAsDouble() != selected) {
            elevatorMotor.set(pid.getSpeed(pidForDaPid.getSpeed(selected - elevatorMotor.getPosition().getValueAsDouble())));

        }
    }
}

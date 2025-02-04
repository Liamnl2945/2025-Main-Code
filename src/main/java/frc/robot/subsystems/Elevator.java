package frc.robot.subsystems;

import frc.robot.PIDs.TestingElevatorPID;
import frc.robot.RobotContainer;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants;

public class Elevator extends SubsystemBase {
    static double selected = -1;
    static boolean l0 = false;
    static boolean l1 = false;
    static boolean l2 = false;
    static boolean l3 = false;
    static boolean l4 = false;
    static double error;
    private static TestingElevatorPID pid = new TestingElevatorPID();

    private final static TalonFX elevatorMotor = new TalonFX(constants.Elevator.elevator);

        public Elevator(){
            elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
            elevatorMotor.setPosition(0);
        }

        public static void runElevator(double speed){
            System.out.println("Elevator position: " + elevatorMotor.getPosition().getValueAsDouble());
            if(RobotContainer.heightToggle.getAsBoolean()) {
                if(RobotContainer.L1.getAsBoolean()){//FOR ALL VALUES OF SELECTED, they are target rotations for the PID. For example, if L1 sets selected to 10, then it will raise the arm 10 motor rotations high.
                    selected = 5.32;
                }
                if (RobotContainer.L2.getAsBoolean()){
                    selected = 14.6;
                }
                if (RobotContainer.L3.getAsBoolean()){
                    selected = 22.25;
                }
                if (RobotContainer.L4.getAsBoolean()){
                    selected = 31.5;//HIGHEST POSSIBLE
                }
                if(RobotContainer.L0.getAsBoolean()){
                    selected = 0;
                }
            }
            if(Math.abs(speed) >= 0.1){
                selected = -1;
            }
            if(RobotContainer.L0.getAsBoolean() && !RobotContainer.heightToggle.getAsBoolean()){
                elevatorMotor.setPosition(0);
            }
            if(selected != -1) {
                elevatorMotor.set(pid.getSpeed(selected - elevatorMotor.getPosition().getValueAsDouble()));//pass in the error as a function of the distance between our current rotations and the setpoint rotation
            }
            else{
                if(Math.abs(speed) < 0.1){
                    elevatorMotor.set(0);
                }
                else{
                    elevatorMotor.set(-speed);
                }
            }

    }
}

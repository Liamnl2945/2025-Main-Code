package frc.robot.subsystems;






import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.PIDs.TestingElevatorPID;
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
    private static TestingElevatorPID pid = new TestingElevatorPID();

    private final static TalonFX elevatorMotor = new TalonFX(constants.Elevator.elevator, "rio");
    private final static TalonFX indexerMotor = new TalonFX(constants.Elevator.indexer, "rio");

        public Elevator(){
            indexerMotor.setNeutralMode(NeutralModeValue.Brake);
            elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
            elevatorMotor.setPosition(0);
        }
//among us
        public static void runElevator(double speed, int dpad){
            System.out.println(dpad);
            switch(dpad){
                case 0:
                    indexerMotor.set(0);
                    break;
                case -1:
                    indexerMotor.set(0.5);
                    break;
                case 1:
                    indexerMotor.set(-0.5);
                    break;
            }

            if(RobotContainer.heightToggle.getAsBoolean()) {
                if(RobotContainer.L1.getAsBoolean()){//FOR ALL VALUES OF SELECTED, they are target rotations for the PID. For example, if L1 sets selected to 10, then it will raise the arm 10 motor rotations high.
                    selected = 34;
                }
                if (RobotContainer.L2.getAsBoolean()){
                    selected = 70;
                }
                if (RobotContainer.L3.getAsBoolean()){
                    selected = 115;
                }
                if (RobotContainer.L4.getAsBoolean()){
                    selected = 165;//HIGHEST POSSIBLE
                }
                if(RobotContainer.L0.getAsBoolean()){
                    selected = 0;
                }
            }

            if(Math.abs(speed) >= 0.1){
                selected = -1;
            }
            if(RobotContainer.L0.getAsBoolean() && !RobotContainer.heightToggle.getAsBoolean()){
                selected = -2;
            }
            if(selected != -1 && selected != -2){
                elevatorMotor.set(pid.getSpeed(selected - elevatorMotor.getPosition().getValueAsDouble()));//pass in the error as a function of the distance between our current rotations and the setpoint rotation
                //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
            }
            else if(selected == -2){
                elevatorMotor.set(-0.02);
                if(RobotContainer.elevatorLimitSwitch.get()){
                    elevatorMotor.set(0);
                    elevatorMotor.setPosition(0);
                    System.out.println("boi you touching the limit switch ts so tuff");
                    selected = 0;
                }
            }
            else{
                if(Math.abs(speed) < 0.1){
                    elevatorMotor.set(0);
                    //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());

                }
                else{
                    elevatorMotor.set(-speed/5);
                }
               //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());

            }



    }
}

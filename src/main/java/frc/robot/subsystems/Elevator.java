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
    public static int intook = 0;
    public static double l1Height = -34;
    public static double l2Height = -70;
    public static double l3Height = -108;
    public static double l4Height = -147;
    private static I2CBruh colorSensor = new I2CBruh();
    private static TestingElevatorPID pid = new TestingElevatorPID();
    private static TestingElevatorPIDPID pidForDaPid = new TestingElevatorPIDPID();
    private static int counter = 0;

    private final static TalonFX elevatorMotor = new TalonFX(constants.Elevator.elevator, "rio");
    private final static TalonFX indexerMotor = new TalonFX(constants.Elevator.indexer, "rio");

        public Elevator(){
            indexerMotor.setNeutralMode(NeutralModeValue.Brake);
            elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
            elevatorMotor.setPosition(0);
           
        }
//among us

        public static void runElevator(double speed, int stickButton){
            height = elevatorMotor.getPosition().getValueAsDouble() / 165;
            System.out.println(elevatorMotor.getPosition().getValueAsDouble());

            if(RobotContainer.heightToggle.getAsBoolean()) {
                if(RobotContainer.L1.getAsBoolean()){//FOR ALL VALUES OF SELECTED, they are target rotations for the PID. For example, if L1 sets selected to 10, then it will raise the arm 10 motor rotations high.
                    selected = l1Height;
                }
                if (RobotContainer.L2.getAsBoolean()){
                    selected = l2Height;
                }
                if (RobotContainer.L3.getAsBoolean()){
                    selected = l3Height;
                }
                if (RobotContainer.L4.getAsBoolean()){
                    selected = l4Height;//HIGHEST POSSIBLE
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
                if(RobotContainer.elevatorLimitSwitch.get()){
                    selected = -1;
                }
            }
            
            else if(selected == -2){
                elevatorMotor.set(0.02);
                if(RobotContainer.elevatorLimitSwitch.get()){
                    elevatorMotor.set(0);
                    elevatorMotor.setPosition(0);
                    selected = 0;
                    intook = 0;
                }
            }
            
            else{
                if(Math.abs(speed) < 0.1){
                    elevatorMotor.set(0);
                    //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
                }
                else{
                    elevatorMotor.set(speed/5);
                }
               //System.out.println("Elevator position:" + elevatorMotor.getPosition().getValueAsDouble());
            }
            if (intook == 0 || intook == 1) {
                if (stickButton == -1) {
                    intook = 1;
                }
            }

            if(stickButton == 1){
                intook = 0;
            }

            if(intook == 1){
                indexerMotor.set(0.15);
                if(!RobotContainer.intakeLimitSwitch.get()){//initial intake, until the color sensor is triggered
                    indexerMotor.set(0);
                    intook = 3;
                }
            }else if(intook == 2){//normal reverse
                indexerMotor.set(-0.1);
                intook = 0;
            }else if(intook == 0){//no operation case
                indexerMotor.set(0);
            }else if(intook == 3){//go backwards slowly until we dont see the coral anymore
                if(RobotContainer.intakeLimitSwitch.get()){
                    counter++;
                    if(counter > 10){
                        intook = 4;
                        indexerMotor.set(0);
                        counter = 0;
                    }
                }else{
                    indexerMotor.set(-0.05);
                }
            }else if(intook == 4){//waiting for the second button press to spit it out
                if(stickButton == -1){
                        intook = 5;
                }else{
                    indexerMotor.set(0);
                }
            }
            else if(intook == 5){
                if(elevatorMotor.getPosition().getValueAsDouble() < 130){
                    counter++;
                    indexerMotor.set(0.2);
                }
                else {
                    counter++;
                    indexerMotor.set(0.25);
                }
                if(counter > 25){
                    indexerMotor.set(0);
                    intook = 0;
                    counter = 0;
                    selected = 0;
                }
            }


        }
        public static void autoL3(){
            while (!RobotContainer.elevatorLimitSwitch.get()) {
                selected = -2;
            }

            selected = l3Height;
            if(elevatorMotor.getPosition().getValueAsDouble() <= l3Height + 5){
                intook = 5;
                selected = 0;
            }

        }

        public static void autoL4(){
            while (!RobotContainer.elevatorLimitSwitch.get()) {
                selected = -2;
            }

            selected = l4Height;
            if(elevatorMotor.getPosition().getValueAsDouble() <= l4Height + 5){
                intook = 5;
                selected = 0;
            }

        }

        public static void autoIntake() {
            intook = 1;
        }
}

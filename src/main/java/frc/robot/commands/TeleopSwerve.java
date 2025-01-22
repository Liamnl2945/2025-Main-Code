package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.constants;
import frc.robot.limelightData;
import frc.robot.PIDs.AprilTagPointLock;
import frc.robot.PIDs.AprilTagRotationLock;
import frc.robot.PIDs.StrafePID;
import frc.robot.PIDs.SwerveDriveMotorPID;
import frc.robot.PIDs.SwerveTurnMotorPID;
import frc.robot.PIDs.TranslationPID;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;

import edu.wpi.first.wpilibj2.command.Command;


public class TeleopSwerve extends Command {

    private boolean isPointLocked = false;
    private boolean isRotationLocked = false;
    private boolean isTrapLocked = false;

    public static double trapShootDistance = 2; //TODO tune to robot
    public static double trapShooterSpeed = 0.0; //TODO tune to robot

    private final double moveSpeedLimiter = 0.5;//ex, 0.5 = 50%
    private final double rotationSpeedLimiter = 0.3;

    private AprilTagPointLock rotationPIDAprilTagPointLock;
    private AprilTagRotationLock rotationPIDAprilTagRotationLock;
    private StrafePID StrafePIDLock;
    private TranslationPID TranslationPIDLock;
    private SwerveTurnMotorPID turnPID;
    private SwerveDriveMotorPID drivePID;
    private Pigeon2 gyro;
    private Swerve s_Swerve;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;
    private BooleanSupplier slowMove;

    private double translationVal;
    private double strafeVal;
    private double rotationVal;
    private double tagYaw;
    private double gyroValue;
    private double setpoint;

    public double calculateError(double setPointf, double currentPoint){
        return ((setPointf - currentPoint)%360);
    }

    private double getXComp(double[] vector){
        return Math.cos(vector[1]);
    }

    private double getYComp(double[] vector){
        return Math.sin(vector[1]);
    }

    public TeleopSwerve(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup,
        BooleanSupplier robotCentricSup, BooleanSupplier aiming) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;

        // Initialize PID controllers
        this.rotationPIDAprilTagPointLock = new AprilTagPointLock();
        this.rotationPIDAprilTagRotationLock = new AprilTagRotationLock();

        gyro = new Pigeon2(constants.Swerve.pigeonID);
        gyro.getConfigurator().apply(new Pigeon2Configuration());
        gyro.setYaw(0);
    }

    @Override
    public void execute() {
        if (limelightData.TagValid && RobotContainer.aim.getAsBoolean() || ((limelightData.noteValid)&&(RobotContainer.autoIntake.getAsBoolean()))){//if limelight sees tag and the aiming is pressed
            if (limelightData.tagID == 7 || limelightData.tagID == 4 ||  limelightData.tagID == 5 || limelightData.noteValid) {//Speaker logic & note lineup
                //System.out.println("\n SHOOTER LOCKED To Speaker");
                if(!isPointLocked){//this is to prevent it from creating a new PID everytime, and will use the same PID
                    //System.out.println("\n PID STATEMENT");
                    isPointLocked = true;
                    isRotationLocked = false;
                    rotationPIDAprilTagPointLock = new AprilTagPointLock();//create a new PID controller for lockon sequence
                    rotationVal = rotationPIDAprilTagPointLock.getR();//get rotation value from PID
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    System.out.println("POINT LOCKED");
                }
                else{
                    rotationVal = rotationPIDAprilTagPointLock.getR();
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                }
            }
            else if (limelightData.tagID == 6) {//amp logic
                if(!isRotationLocked){//this is to prevent it from creating a new PID controller everytime, and will use the same PID & LL data
                    isRotationLocked = true;
                    isPointLocked = false;
                    setpoint = (gyro.getYaw().getValueAsDouble() + limelightData.TagYaw) % 360;
                    rotationPIDAprilTagRotationLock = new AprilTagRotationLock();
                    rotationVal = rotationPIDAprilTagRotationLock.getRd(calculateError(setpoint, gyro.getYaw().getValueAsDouble()));
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                }
                else{
                    rotationVal = rotationPIDAprilTagRotationLock.getRd(calculateError(setpoint, gyro.getYaw().getValueAsDouble()));
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                }
            }else if(limelightData.tagID == 12 || limelightData.tagID == 13 || limelightData.tagID == 14 || limelightData.tagID == 15 || limelightData.tagID == 16){ //trap detection logic
                if(!isTrapLocked){

                    isTrapLocked = true;
                    isPointLocked = false;
                    isRotationLocked = false;
                    StrafePIDLock = new StrafePID();
                    rotationPIDAprilTagPointLock = new AprilTagPointLock();//create a new PID controller for lockon sequence
                    TranslationPIDLock = new TranslationPID();

                    strafeVal = StrafePIDLock.getS();
                    translationVal = -TranslationPIDLock.getT();
                    rotationVal = rotationPIDAprilTagPointLock.getR();//get an overwrite rotation value from PID, no controller rotation effect if active
                }
                else{
                    isPointLocked = false;
                    isRotationLocked = false;

                    strafeVal = StrafePIDLock.getS();
                    translationVal = -TranslationPIDLock.getT();
                    rotationVal = rotationPIDAprilTagPointLock.getR();//get rotation value from PID
                }
            }
        } else {
            isPointLocked = false;
            isRotationLocked = false;
            isTrapLocked = false;
            rotationVal =  -MathUtil.applyDeadband(rotationSup.getAsDouble(), constants.stickDeadband)*rotationSpeedLimiter;//invert sign if robot is turing the wrong direction. Would normally stick rotation speed limiter in 'drive' method, but it would interfere with PID calculations
            translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
            strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
        }

        // Drive
        s_Swerve.drive(
            new Translation2d(translationVal*moveSpeedLimiter, strafeVal*moveSpeedLimiter).times(constants.Swerve.maxSpeed),
            rotationVal * constants.Swerve.maxAngularVelocity,
            !robotCentricSup.getAsBoolean(),
            true

        );
    }
}

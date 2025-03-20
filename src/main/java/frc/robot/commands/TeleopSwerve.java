package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import frc.robot.PIDs.*;
import frc.robot.subsystems.Elevator;
import frc.robot.RobotContainer;
import frc.robot.constants;
import frc.robot.limelightData;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;

import edu.wpi.first.wpilibj2.command.Command;

import static frc.robot.subsystems.Elevator.height;


public class TeleopSwerve extends Command {

    private boolean isPointLocked = false;
    private boolean isRotationLocked = false;
    private boolean isTrapLocked = false;

    public static double trapShootDistance = 2; //TODO tune to robot
    public static double trapShooterSpeed = 0.0; //TODO tune to robot

    public static int alignValue = 1;

    private final double moveSpeedLimiter = 0.8*(1-(height*0.9));//limit swerve speed based on elevator height
    private final double rotationSpeedLimiter = 0.65*(0.2*(1-(height * 0.9)));

    private AprilTagPointLock rotationPID;
    private TranslationPID translationPID;
    private StrafePID strafePID;
    private static StrafePIDLeftLock strafePIDLeftLock = new StrafePIDLeftLock();
    private static StrafePIDRightLock strafePIDRightLock = new StrafePIDRightLock();
    private static StrafePIDMiddleLock strafePIDMiddleLock = new StrafePIDMiddleLock();

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

    private boolean flag = false;

    private Rotation2d rotationHold;


    private Pose2d poseHold;
    private SwerveModulePosition[] swerveHold;

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

        gyro = new Pigeon2(constants.Swerve.pigeonID);
        gyro.getConfigurator().apply(new Pigeon2Configuration());
        gyro.setYaw(0);
    }

    @Override
    public void execute() {
        if(RobotContainer.dpadRightDriver.getAsBoolean()){
            alignValue = 1;
        }
        else if(RobotContainer.dpadLeftDriver.getAsBoolean()){
            alignValue = -1;
        }
        else if(RobotContainer.dpadUpDriver.getAsBoolean()){
            alignValue = 0;
        }

        if(limelightData.TagValid && RobotContainer.swerveAlign.getAsBoolean() && !RobotContainer.swerveOverride.getAsBoolean()){//if limelight sees tag and the aiming is pressed
            rotationVal =  -MathUtil.applyDeadband(rotationSup.getAsDouble(), constants.stickDeadband)*rotationSpeedLimiter;//invert sign if robot is turing the wrong direction. Would normally stick rotation speed limiter in 'drive' method, but it would interfere with PID calculations
            translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
            if(!flag){
                flag = true;
                rotationHold = s_Swerve.getHeading();
                s_Swerve.setHeading(new Rotation2d(0));
            }



            switch (alignValue) {
                case 1:
                    if(limelightData.TagAlgaeValid) {
                        strafeVal = strafePIDRightLock.getS();
                    }
                    else {
                        strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    }
                    break;
                case -1:
                    if(limelightData.TagSnakeValid) {
                        strafeVal = strafePIDLeftLock.getS();
                    } else {
                        strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    }
                    break;
                case 0:
                    if(limelightData.TagAlgaeValid) {
                        strafeVal = strafePIDMiddleLock.getS();
                    } else {
                        strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    }
                    break;
            }


        } else {
            if(flag){
                flag = false;
                s_Swerve.setHeading(rotationHold);
            }

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
            -rotationVal * constants.Swerve.maxAngularVelocity,
            !robotCentricSup.getAsBoolean(),
            true

        );

    }


}

package frc.robot.commands;

//import programs

import frc.robot.RobotContainer;
import frc.robot.constants;
import frc.robot.limelightData;
import frc.robot.PIDs.AprilTagPointLock;
import frc.robot.PIDs.AprilTagRotationLock;
import frc.robot.subsystems.Swerve;

//import functions

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

//import gyros cope

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

//import math things

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;

//import command base

import edu.wpi.first.wpilibj2.command.Command;


public class SwerveAim extends Command {
    
    //variable declaration

    private boolean isPointLocked = false; 
    private boolean isRotationLocked = false; 

    
    private AprilTagPointLock rotationPIDAprilTagPointLock;
    private AprilTagRotationLock rotationPIDAprilTagRotationLock;
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



    public SwerveAim(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, BooleanSupplier robotCentricSup) {
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
        //System.out.println("\n SWERVE AIM EXECUTING");
        if (limelightData.TagValid) { // If Limelight sees a target
            //System.out.println("\n AIM LOCKED");

            if (limelightData.tagID == 4 || limelightData.tagID == 7) {//Speaker logic
                    //System.out.println("\n SHOOTER LOCKED To Speaker");
                if(!isPointLocked){//this is to prevent it from creating a new PID everytime, and will use the same PID
                    //System.out.println("\n PID STATEMENT");
                    isPointLocked = true;
                    isRotationLocked = false; 
                    rotationPIDAprilTagPointLock = new AprilTagPointLock();//create a new PID controller for lockon sequence
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);//standard strafe/translate input
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    rotationVal = rotationPIDAprilTagPointLock.getR();//get rotation value from PID
                    //System.out.println(limelightData.targetXOffset);
                }
                else{
                    //System.out.println("\n IN THE SHOOTER ELSE STATEMENT");
                    translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), constants.stickDeadband);
                    strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), constants.stickDeadband);
                    rotationVal = rotationPIDAprilTagPointLock.getR();

                }
            } 
            
        }
        s_Swerve.drive(
            new Translation2d(translationVal, strafeVal).times(!RobotContainer.slowMove.getAsBoolean() ? constants.Swerve.maxSpeed : constants.Swerve.maxSpeed*0.15), 
            rotationVal * constants.Swerve.maxAngularVelocity, 
            !robotCentricSup.getAsBoolean(), 
            true
        );
        
    }
}

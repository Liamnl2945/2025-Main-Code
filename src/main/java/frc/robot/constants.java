package frc.robot;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.PIDConstants;
import edu.wpi.first.math.geometry.Translation2d;
import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.util.COTSTalonFXSwerveConstants;
import frc.lib.util.SwerveModuleConstants;

public class constants {

    public static final double stickDeadband = 0.15;
    
    public static final class AlgaeIntake{
        public static final int intakeArm = 9;
        public static final int intakeTop = 10;
        public static final int intakeBot = 11;
    }

    public static class Elevator{
        public static final int elevator = 8;
        public static final int  indexer = 12;
    }

    public static final class CANdle {
        public static final int CANdle = 0;
    }



    public static final class Swerve {
        public static final int pigeonID = 20;

        public static final COTSTalonFXSwerveConstants  chosenAngleModule =  //TODO: This must be tuned to specific robot
        COTSTalonFXSwerveConstants.SDS.MK4i.Falcon500(COTSTalonFXSwerveConstants.SDS.MK4i.driveRatios.L1);

        public static final COTSTalonFXSwerveConstants  chosenDriveModule =  //TODO: This must be tuned to specific robot
                COTSTalonFXSwerveConstants.SDS.MK4i.KrakenX60(COTSTalonFXSwerveConstants.SDS.MK4i.driveRatios.L1);

            /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(23.875); //TODO: This must be tuned to specific robot
        public static final double wheelBase = Units.inchesToMeters(23.875); //TODO: This must be tuned to specific robot
        public static final double wheelCircumference = chosenDriveModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 0.61, trackWidth / 0.61),
            new Translation2d(wheelBase / 0.61, -trackWidth / 0.61),
            new Translation2d(-wheelBase / 0.61, trackWidth / 0.61),
            new Translation2d(-wheelBase / 0.61, -trackWidth / 0.61));

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenDriveModule.driveGearRatio;
        public static final double angleGearRatio = chosenAngleModule.angleGearRatio;

        /* Motor Inverts */
        public static final InvertedValue angleMotorInvert = chosenAngleModule.angleMotorInvert;
        
        public static final InvertedValue driveMotorInvert = chosenDriveModule.driveMotorInvert;
 
        /* Angle Encoder Invert */
        public static final SensorDirectionValue cancoderInvert = chosenAngleModule.cancoderInvert;
        
        /* Swerve Current Limiting */
        public static final int angleCurrentLimit = 25;
        public static final int angleCurrentThreshold = 40;
        public static final double angleCurrentThresholdTime  = 0.1;
        public static final boolean angleEnableCurrentLimit  = true;

        public static final int driveCurrentLimit = 35;
        public static final int driveCurrentThreshold = 60;
        public static final double driveCurrentThresholdTime = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.4;
        public static final double closedLoopRamp = 0.4;

        /* Angle Motor PID Values */
        public static final double angleKP = 35;
        public static final double angleKI = 3;
        public static final double angleKD = 3;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.12; //TODO: This must be tuned to specific robot
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values 
         * Divide SYSID values by 12 to convert from volts to percent output for CTRE */
        public static final double driveKS = (0.32); //TODO: This must be tuned to specific robot
        public static final double driveKV = (1.51 );
        public static final double driveKA = (0.27 );

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static double  maxSpeed = 5.1; //TODO: This must be tuned to specific robot

        public static double accelerationLimit = 5;
        /** Radians per Second */
        public static double maxAngularVelocity = 10; //TODO: This must be tuned to specific robot

        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Coast;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;

        /* Module Specific Constants */
        /* Front Left Module - Module 0 */
        public static final class Mod0 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 0;
            public static final int angleMotorID = 4;
            public static final int canCoderID = 0;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-80);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Front Right Module - Module 1 */
        public static final class Mod1 { //TODO: This must be tuned to specific robot
            
            public static final int driveMotorID = 1;
            public static final int angleMotorID = 5;
            public static final int canCoderID = 1;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-20);
            public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
        
        /* Back Left Module - Module 2 */
        public static final class Mod2 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 2;
            public static final int angleMotorID = 6;
            public static final int canCoderID = 2;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-103);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Right Module - Module 3 */
        public static final class Mod3 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 3;
            public static final int angleMotorID = 7;
            public static final int canCoderID = 3;   
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-24);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        public static final PIDConstants translationConstants = new PIDConstants(5.0, 0.0, 0.0);
        public static final PIDConstants rotationConstants = new PIDConstants(5.0, 0.0, 0.0);
      
    }
     
  


    
}

package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public final class CTREConfigs {
    public TalonFXConfiguration swerveAngleFXConfig = new TalonFXConfiguration();
    public TalonFXConfiguration swerveDriveFXConfig = new TalonFXConfiguration();
    public CANcoderConfiguration swerveCANcoderConfig = new CANcoderConfiguration();


    public CTREConfigs(){
         /** Swerve CANCoder Configuration */
         swerveCANcoderConfig.MagnetSensor.SensorDirection = constants.Swerve.cancoderInvert;

         /** Swerve Angle Motor Configurations */
         /* Motor Inverts and Neutral Mode */
         swerveAngleFXConfig.MotorOutput.Inverted = constants.Swerve.angleMotorInvert;
         swerveAngleFXConfig.MotorOutput.NeutralMode = constants.Swerve.angleNeutralMode;
 
         /* Gear Ratio and Wrapping Config */
         swerveAngleFXConfig.Feedback.SensorToMechanismRatio = constants.Swerve.angleGearRatio;
         swerveAngleFXConfig.ClosedLoopGeneral.ContinuousWrap = true;
         
         /* Current Limiting */
         swerveAngleFXConfig.CurrentLimits.SupplyCurrentLimitEnable = constants.Swerve.angleEnableCurrentLimit;
         swerveAngleFXConfig.CurrentLimits.SupplyCurrentLimit = constants.Swerve.angleCurrentLimit;
         swerveAngleFXConfig.CurrentLimits.SupplyCurrentLowerLimit = constants.Swerve.angleCurrentThreshold;
         swerveAngleFXConfig.CurrentLimits.SupplyCurrentLowerTime = constants.Swerve.angleCurrentThresholdTime;
 
         /* PID Config */
         swerveAngleFXConfig.Slot0.kP = constants.Swerve.angleKP;
         swerveAngleFXConfig.Slot0.kI = constants.Swerve.angleKI;
         swerveAngleFXConfig.Slot0.kD = constants.Swerve.angleKD;
 
         /** Swerve Drive Motor Configuration */
         /* Motor Inverts and Neutral Mode */
         swerveDriveFXConfig.MotorOutput.Inverted = constants.Swerve.driveMotorInvert;
         swerveDriveFXConfig.MotorOutput.NeutralMode = constants.Swerve.driveNeutralMode;
 
         /* Gear Ratio Config */
         swerveDriveFXConfig.Feedback.SensorToMechanismRatio = constants.Swerve.driveGearRatio;
 
         /* Current Limiting */
         swerveDriveFXConfig.CurrentLimits.SupplyCurrentLimitEnable = constants.Swerve.driveEnableCurrentLimit;
         swerveDriveFXConfig.CurrentLimits.SupplyCurrentLimit = constants.Swerve.driveCurrentLimit;
         swerveDriveFXConfig.CurrentLimits.SupplyCurrentLowerLimit = constants.Swerve.driveCurrentThreshold;
         swerveDriveFXConfig.CurrentLimits.SupplyCurrentLowerTime = constants.Swerve.driveCurrentThresholdTime;
 
         /* PID Config */
         swerveDriveFXConfig.Slot0.kP = constants.Swerve.driveKP;
         swerveDriveFXConfig.Slot0.kI = constants.Swerve.driveKI;
         swerveDriveFXConfig.Slot0.kD = constants.Swerve.driveKD;
 
         /* Open and Closed Loop Ramping */
         swerveDriveFXConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = constants.Swerve.openLoopRamp;
         swerveDriveFXConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod = constants.Swerve.openLoopRamp;
 
         swerveDriveFXConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = constants.Swerve.closedLoopRamp;
         swerveDriveFXConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = constants.Swerve.closedLoopRamp;
     }
}
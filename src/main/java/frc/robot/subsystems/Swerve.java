package frc.robot.subsystems;

import frc.robot.SwerveModule;
import frc.robot.constants;
import frc.robot.commands.TeleopSwerve;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import frc.robot.RobotContainer;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;


public class Swerve extends SubsystemBase {
    public SwerveDriveOdometry odometry;

    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;

    public double tempHeadingOffset;

    //private Field2d field = new Field2d();

    public Swerve() {
        gyro = new Pigeon2(constants.Swerve.pigeonID);
        gyro.getConfigurator().apply(new Pigeon2Configuration());
        //gyro.setYaw(0);

        


        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, constants.Swerve.Mod0.constants),
            new SwerveModule(1, constants.Swerve.Mod1.constants),
            new SwerveModule(2, constants.Swerve.Mod2.constants),
            new SwerveModule(3, constants.Swerve.Mod3.constants)
        };
        odometry = new SwerveDriveOdometry(constants.Swerve.swerveKinematics, getGyroYaw(), getModulePositions());


         try{
      RobotConfig config = RobotConfig.fromGUISettings();

      // Configure AutoBuilder
      AutoBuilder.configure(
        this::getPose, 
        this::resetPose, 
        this::getSpeeds, 
        this::driveRobotRelative, 
        new PPHolonomicDriveController(
          constants.Swerve.translationConstants,
          constants.Swerve.rotationConstants
        ),
        config,
        () -> {
            // Boolean supplier that controls when the path will be mirrored for the red alliance
            // This will flip the path being followed to the red side of the field.
            // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

            var alliance = DriverStation.getAlliance();
            if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
            }
            return false;
        },
        this
      );
    }catch(Exception e){
      DriverStation.reportError("Failed to load PathPlanner config and configure AutoBuilder", e.getStackTrace());
    }


        // Set up custom logging to add the current path to a field 2d widget
       // PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));

        //SmartDashboard.putData("Field", field);
    }
     @Override
    public void periodic(){
        odometry.update(getGyroYaw(), getModulePositions());  

        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCANcoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }
        //field.setRobotPose(getPose());
        //System.out.println(gyro.getAccelerationX());
        //System.out.println(gyro.getAccelerationY());
    }

   public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates = 
        constants.Swerve.swerveKinematics.toSwerveModuleStates(
            fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                    translation.getX(),
                    translation.getY(),
                    rotation,
                    getHeading()
                )
                : new ChassisSpeeds(
                    translation.getX(),
                    translation.getY(),
                        rotation)
                );
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
        mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
    }
} 

 /* Used by SwerveControllerCommand in Auto */
 public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, constants.Swerve.maxSpeed);
    
    for(SwerveModule mod : mSwerveMods){
        mod.setDesiredState(desiredStates[mod.moduleNumber], false);
    }
}

public SwerveModuleState[] getModuleStates(){
    SwerveModuleState[] states = new SwerveModuleState[4];
    for(SwerveModule mod : mSwerveMods){
        states[mod.moduleNumber] = mod.getState();
    }
    return states;
}


public SwerveModulePosition[] getModulePositions(){
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for(SwerveModule mod : mSwerveMods){
        positions[mod.moduleNumber] = mod.getPosition();
    }
    return positions;
}

public double[] getSwerveVector(){
    return new double[]{getHeading().getDegrees(), Math.sqrt(Math.pow(getSpeeds().vxMetersPerSecond, 2)*Math.pow(getSpeeds().vyMetersPerSecond, 2))};//perfrom pythagorean theorum to get overall speed vector
}

public Pose2d getPose() {
    return odometry.getPoseMeters();
}

public void setPose(Pose2d pose) {
    odometry.resetPosition(getGyroYaw(), getModulePositions(), pose);
}

public Rotation2d getHeading(){//Returns the normal heading of the robot UNLESS we are autointake (noteAim), in which case the robots movements become relative to the note & its alignments with the camera. Left on the joystick will move the robot left, relative to the bots current position to the note. Keep in mind rotation (should be) dynamically shifting to face the note, so this new left will change in accordance to that.
     if(RobotContainer.autoIntake.getAsBoolean()){
             return new Rotation2d(3.14159265358) ;
     }
    return getPose().getRotation();
}

public void setHeading(Rotation2d heading){
    odometry.resetPosition(getGyroYaw(), getModulePositions(), new Pose2d(getPose().getTranslation(), heading));
}

public void zeroHeading(){
    odometry.resetPosition(getGyroYaw(), getModulePositions(), new Pose2d(getPose().getTranslation(), new Rotation2d()));
}

public Rotation2d getGyroYaw() {
    return Rotation2d.fromDegrees((gyro.getYaw().getValueAsDouble()) );
}

public void resetModulesToAbsolute(){
    for(SwerveModule mod : mSwerveMods){
        mod.resetToAbsolute();
    }
}

     public ChassisSpeeds getButton(Translation2d translation, double rotation, boolean fieldRelative,boolean halfSpeed){
        if(fieldRelative && halfSpeed){
           return ChassisSpeeds.fromFieldRelativeSpeeds(
                (translation.getX()/3), 
                (translation.getY()/3),
                (rotation/3), 
                getGyroYaw()
                );
        }
    else if(!fieldRelative && halfSpeed){
            return new ChassisSpeeds(
                (translation.getX()/3), 
                (translation.getY()/3),
                (rotation/3)
            );
        }
    else if(fieldRelative && !halfSpeed){
        return ChassisSpeeds.fromFieldRelativeSpeeds(
                        translation.getX(), 
                        translation.getY(), 
                        rotation, 
                        getGyroYaw()
                    );
        }
    else{
        return new ChassisSpeeds(
                        translation.getX(), 
                        translation.getY(), 
                        rotation);
        }
    }
    public void resetPose(Pose2d pose) {
        odometry.resetPosition((getGyroYaw()), getModulePositions(), pose);
    }

    public ChassisSpeeds getRobotRelativeSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
        return ChassisSpeeds.fromFieldRelativeSpeeds(
            fieldRelativeSpeeds.vxMetersPerSecond ,
            fieldRelativeSpeeds.vyMetersPerSecond ,
            fieldRelativeSpeeds.omegaRadiansPerSecond,
            getHeading()
        );
    }
    public ChassisSpeeds getSpeeds() {
        return (constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates()));
      }
    

    public void driveRobotRelative(ChassisSpeeds robotRelativeSpeeds) {
        SwerveModuleState[] targetStates = constants.Swerve.swerveKinematics.toSwerveModuleStates(robotRelativeSpeeds);
        setStates(targetStates);
    }

    public void setStates(SwerveModuleState[] targetStates) {
        // Increase the value as needed (e.g., 1.0 for 1 m/s)
        SwerveDriveKinematics.desaturateWheelSpeeds(targetStates, 4.5); //TODO tune this NERD
        
        for (int i = 0; i < mSwerveMods.length; i++) {
          mSwerveMods[i].setDesiredState(targetStates[i], false);
        }
      }

     
    
      public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[mSwerveMods.length];
        for (int i = 0; i < mSwerveMods.length; i++) {
          positions[i] = mSwerveMods[i].getPosition();
        }
        return positions;
      }
    
    
      


      
    

    

    

   

    

    
}
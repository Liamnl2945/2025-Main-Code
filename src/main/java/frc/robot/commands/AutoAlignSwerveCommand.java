package frc.robot.commands;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.PIDs.*;
import frc.robot.constants;
import frc.robot.limelightData;
import frc.robot.subsystems.Swerve;
import frc.robot.RobotContainer;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import static frc.robot.subsystems.Elevator.height;


public class AutoAlignSwerveCommand extends Command {
   private static StrafePIDLeftLock strafePIDLeftLock = new StrafePIDLeftLock();

   private Pigeon2 gyro;
   private final Swerve s_Swerve;
   private double strafeVal;


   private boolean flag = false;

   private Rotation2d rotationHold;

    public AutoAlignSwerveCommand(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup,
                                  BooleanSupplier robotCentricSup) {
      this.s_Swerve = s_Swerve;
      addRequirements(s_Swerve);

         // Initialize PID controllers

         gyro = new Pigeon2(constants.Swerve.pigeonID);
         gyro.getConfigurator().apply(new Pigeon2Configuration());
         gyro.setYaw(0);
        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)

    }

    /**
     * The initial subroutine of a command.  Called once when the command is initially scheduled.
     */
    @Override
    public void initialize() {
        s_Swerve.setHeading(new Rotation2d(0));


    }

    /**
     * The main body of a command.  Called repeatedly while the command is scheduled.
     * (That is, it is called repeatedly until {@link #isFinished()}) returns true.)
     */
    @Override
    public void execute() {
        while ((limelightData.snakeXOffset-1) >= 0.2) {
            System.out.println("if this doesn't work im gonna do 9/11");
            if (limelightData.TagValid) {//if limelight sees tag and the aiming is pressed

                if (limelightData.TagSnakeValid) {
                    strafeVal = strafePIDLeftLock.getS();
                }
            }
            s_Swerve.drive(
            new Translation2d(0.05, strafeVal).times(constants.Swerve.maxSpeed),
                    0 * constants.Swerve.maxAngularVelocity,
                    RobotContainer.robotCentric.getAsBoolean(),
                    true
            );

        }
        //TODO MAKE THIS SET SWERVE TO 0 MAKEITNOT MOVE



    }

}

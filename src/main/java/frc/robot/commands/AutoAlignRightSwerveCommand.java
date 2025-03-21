package frc.robot.commands;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.PIDs.StrafePIDRightLock;
import frc.robot.RobotContainer;
import frc.robot.constants;
import frc.robot.limelightData;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


public class AutoAlignRightSwerveCommand extends Command {
    private static StrafePIDRightLock strafePIDRightLock = new StrafePIDRightLock();

    private Pigeon2 gyro;
    private int aligns = 0;
    private final Swerve s_Swerve;
    private double strafeVal;

    // also listen to Red by King Crimson it's a basic prog rock favorite but Starless literally changed my life and how I think of music, for me One More Red Nightmare is alsoa highlight

    private Rotation2d rotationHold;

    public AutoAlignRightSwerveCommand(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup,
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
        rotationHold = s_Swerve.getHeading();
        s_Swerve.setHeading(new Rotation2d(0));
        while ((limelightData.snakeXOffset - 1) >= 0.2) {
            System.out.println("if this doesn't work im gonna do 9/11"); // chat gpt didn't fuck with this line but I like it 😅
            aligns++;
            System.out.println("aligned" + aligns + "times");
            if (limelightData.TagValid) {//if limelight sees tag

                if (limelightData.TagSnakeValid) {
                    strafeVal = strafePIDRightLock.getS();
                    s_Swerve.drive(
                            new Translation2d(0.05, strafeVal).times(constants.Swerve.maxSpeed),
                            0 * constants.Swerve.maxAngularVelocity,
                            RobotContainer.robotCentric.getAsBoolean(),
                            true
                    );
                }
            }


        }
        //prints "done" and drives forward until deadline canceled - Walden

        System.out.println("Done");

        s_Swerve.drive(
                new Translation2d(0.1, 0).times(constants.Swerve.maxSpeed),
                0 * constants.Swerve.maxAngularVelocity,
                RobotContainer.robotCentric.getAsBoolean(),
                true
        );
        s_Swerve.setHeading(rotationHold);

    }

    /**
     * The main body of a command.  Called repeatedly while the command is scheduled.
     * (That is, it is called repeatedly until {@link #isFinished()}) returns true.)
     */
    @Override
    public void execute() {




    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("AutoAlignRightSwerveCommand ended. Interrupted? " + interrupted);
    }

}

// This fine command has been provided by the honorable Richard M. Daley and Walden O. Oberloh
// (Chicago brothers get it)
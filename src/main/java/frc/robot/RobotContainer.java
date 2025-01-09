package frc.robot;

import java.util.List;

import com.ctre.phoenix.led.CANdle;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Intake_Indexer;
import frc.robot.commands.Intake_IndexerReverse;
import frc.robot.commands.RunIndexerCommand;
import frc.robot.commands.RunShooter;
import frc.robot.commands.SetShooterSpeedAuto;
import frc.robot.commands.SetShooterSpeedByAprilTag;
import frc.robot.commands.ShooterReverse;
import frc.robot.commands.ShooterSpeedup;
import frc.robot.commands.StopShooterCommand;
import frc.robot.commands.SwerveAim;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.climberCom;
import frc.robot.commands.intake_IndexerAuto;
import frc.robot.commands.intake_indexerStop;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.indexer;

public class RobotContainer {
    //LED subsystem
    private final LEDSubsystem m_LedSubsystem = new LEDSubsystem();

    //intake
    private final Intake I_Intake = new Intake();
    //indexer
    private final indexer I_Indexer = new indexer();
    //shooter
    private final Shooter S_Shooter = new Shooter();
    //climber
    private final Climber C_Climber = new Climber();
    //Swerve
    private static final Swerve s_Swerve = new Swerve();

    public static double[] getRobotVector() {
        return s_Swerve.getSwerveVector();
    }
    //limelight

    //AUTOS
    private SendableChooser<Command> autoChooser;
    private final ParallelCommandGroup lockGroup;
    private final SequentialCommandGroup indexerCmd;
    private final SequentialCommandGroup indexerStopCommand;


    //ShuffleboardTab limelightTab;

    //joysticks

    public final static Joystick driver = new Joystick(0);

    public final static Joystick manipulator = new Joystick(1);


    // Controller Buttons Used

    // **********************************************************
    // Xbox Controller - 1/Driver - Port 0
    // ----------------------------------------------------------
    // Left Stick (Translate along X and Y plane) - "Swerve"
    // Right Stick (Rotate about Z-Axis) - Drive Right/Left
    // Button Y - Zero the gyro
    // Left Bumper - Robot Centric drive mode
    // Right Bumper - Speed throttle
    //
    // **********************************************************

    // **********************************************************
    // Xbox Controller - 2/Operator - Port 1
    // ----------------------------------------------------------
    // Left Trigger -
    // Right Trigger -
    // Left Bumper
    // Button A - Normal Shoot
    // Button B - Slow Shoot
    // Button X - Reverse Shooter
    // Button Y - Aim limelight
    // D-Pad up - intake & indexer in
    // D-Pad Down - intake & indexer out
    // **********************************************************


    //Timer  rumbleTimer = new Timer();

    //Driver Buttons
    //Driver Buttons
    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kX.value);
    public final static JoystickButton slowMove = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);


    //manip
    public final static JoystickButton aim = new JoystickButton(manipulator, XboxController.Button.kRightBumper.value);
    public final static JoystickButton autoIntake = new JoystickButton(manipulator, XboxController.Button.kLeftBumper.value);
    public final JoystickButton shooterReverse = new JoystickButton(manipulator, XboxController.Button.kB.value);
    public final JoystickButton slowShoot = new JoystickButton(manipulator, XboxController.Button.kX.value);
    public final JoystickButton ShootLimeLight = new JoystickButton(manipulator, XboxController.Button.kY.value);

    //one button code
    private final Intake_Indexer intake_Indexer = new Intake_Indexer(I_Intake, I_Indexer, manipulator);
    private final climberCom climberCommand = new climberCom(C_Climber, manipulator);
    private final SetShooterSpeedByAprilTag ShooterAprilTags = new SetShooterSpeedByAprilTag(S_Shooter);
    private final RunIndexerCommand RunIndexerCommand = new RunIndexerCommand(I_Indexer);
    private final RunShooter RunShooter = new RunShooter(S_Shooter);
    //intake
    //public final JoystickButton intakeIn = new JoystickButton(manipulator, XboxController.Button.kLeftBumper.value);
    // public final JoystickButton intakeOut = new JoystickButton(manipulator, XboxController.Button.kRightBumper.value);
    //public final JoystickButton intakeSlow = new JoystickButton(manipulator, XboxController.Button.kA.value);

    //indexer
    //public final JoystickButton indexerIn = new JoystickButton(manipulator, XboxController.Button.kB.value);
    //public final JoystickButton indexerOut = new JoystickButton(manipulator, XboxController.Button.kY.value);

    //shooter

    //public final JoystickButton shooterRun = new JoystickButton(manipulator, XboxController.Button.kA.value);

    //private RotationSource hijackableRotation = new JoystickLock(); // get rotation from driver input;


    public RobotContainer() {
        //CommandScheduler.getInstance().registerSubsystem(m_LedSubsystem);
        lockGroup = new ParallelCommandGroup(
                new SetShooterSpeedAuto(S_Shooter),
                new SwerveAim(s_Swerve,
                        () -> -driver.getRawAxis(translationAxis),
                        () -> -driver.getRawAxis(strafeAxis),
                        () -> -driver.getRawAxis(rotationAxis),
                        () -> robotCentric.getAsBoolean()
                ));
        Command lockGroupgtimed = lockGroup.withTimeout(1);

        indexerCmd = new SequentialCommandGroup(
                new WaitCommand(0.25),
                new intake_IndexerAuto(I_Intake, I_Indexer)
        );
        indexerStopCommand = new SequentialCommandGroup(
                new intake_indexerStop(I_Intake, I_Indexer)
        );
        Command indexerinverted = new SequentialCommandGroup(
                new Intake_IndexerReverse(I_Intake, I_Indexer)
        );

        Command shooterReverseCommand = new ShooterReverse(S_Shooter);
        Command ShooterReverseTime = shooterReverseCommand.withTimeout(0.5);
        Command indexerStopTimed = indexerStopCommand.withTimeout(.005);
        Command indexerTimed = indexerCmd.withTimeout(1.5);
        Command indexerReverse = indexerinverted.withTimeout(.25);
        //Command indexerRunThing = indexerCmd.withTimeout(4);
        Command StopShooterCommand = new StopShooterCommand(S_Shooter);
        Command StopShooterTime = StopShooterCommand.withTimeout(.025);
        //Command StopShooterInstant = new InstantCommand(new StopShooterCommand(S_Shooter));
        Command ShooterRev = new ShooterSpeedup(S_Shooter);
        Command TimedRev = ShooterRev.withTimeout(1.5);
        Command RunShooter = new RunShooter(S_Shooter);
        Command RunShooterTime = RunShooter.withTimeout(.5);


        NamedCommands.registerCommand("Lock Sequence", lockGroupgtimed); // Set a desired name
        NamedCommands.registerCommand("IndexerRun", indexerTimed);
        NamedCommands.registerCommand("indexer Stop", indexerStopTimed);
        NamedCommands.registerCommand("indexer Reverse", indexerReverse);
        NamedCommands.registerCommand("Indexer4Reverse", indexerTimed);
        NamedCommands.registerCommand("ShooterReverse", ShooterReverseTime);
        NamedCommands.registerCommand("ShooterRev", TimedRev);
        NamedCommands.registerCommand("RunShooter", RunShooterTime);


        // Build an auto chooser. This will use Commands.none() as the default option.
        // autoChooser = AutoBuilder.buildAutoChooser();


        NamedCommands.registerCommand("Lock Print", Commands.print("RUNNING LOCK SEQUENCE"));
        NamedCommands.registerCommand("Lock FINISHED Print", Commands.print("LOCK SEQUENCE FINISHED"));
        NamedCommands.registerCommand("Intaked Print", Commands.print("INTAKE RAN"));
        NamedCommands.registerCommand("Stop Shooter", StopShooterTime);
        NamedCommands.registerCommand("SHOOTER RUN", RunShooter);
        NamedCommands.registerCommand("Intake & Indexer", intake_Indexer);
        NamedCommands.registerCommand("indexer ONLY", RunIndexerCommand);
        NamedCommands.registerCommand("Shooter Lock", ShooterAprilTags);


        configureBindings();

        autoChooser = AutoBuilder.buildAutoChooser(); // Default auto will be `Commands.none()`
        SmartDashboard.putData("Auto Mode", autoChooser);

        // Another option that allows you to specify the default auto by its name
        // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");


        //JoystickButton shooterButton = new JoystickButton(manipulator, XboxController.Button.kA.value);
        //shooterButton.whileTrue(new SetShooterSpeedByAprilTag(S_Shooter));
        //shooterButton.whileFalse(new InstantCommand(()-> S_Shooter.stopShooter()));


        // Register Named Commands
        //NamedCommands.registerCommand("autoBalance", s_Swerve.ExamplePath());


        s_Swerve.setDefaultCommand(

                new TeleopSwerve(
                        s_Swerve,
                        () -> -driver.getRawAxis(translationAxis),
                        () -> -driver.getRawAxis(strafeAxis),
                        () -> -driver.getRawAxis(rotationAxis),
                        () -> robotCentric.getAsBoolean(),
                        () -> aim.getAsBoolean()
                )
        );


        configureButtonBindings();
        configureDefaultCommands();


        //limelightTab = Shuffleboard.getTab("Limelight Tab");
        //L_Limelight.configLimelightTab(limelightTab);
        //L_Limelight.getShuffleboardValues();


    }

    private void configureBindings() {

        /* AUTO STUFF  */
        // SmartDashboard.putData("Blue Auto Score Twice", new PathPlannerAuto("Blue 2 Auto"));
        //SmartDashboard.putData("Test", new PathPlannerAuto("Test"));
        //SmartDashboard.putData("Example", new PathPlannerAuto("New Auto"));

        // Add a button to run the example auto to SmartDashboard, this will also be in the auto chooser built above
        // SmartDashboard.putData("Example Auto", new PathPlannerAuto("New Auto"));

    }


    // Add a button to run pathfinding commands to SmartDashboard


    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Driver BUTTons */

        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        /* Manipulator Butoons */


        /* intake */
        //intakeIn.onTrue(new InstantCommand(() -> I_Intake.runIntake()));
        //intakeOut.onTrue(new InstantCommand(() -> I_Intake.reverseIntake()));
        //intakeIn.onFalse(new InstantCommand(() -> I_Intake.stopIntake()));
        //intakeOut.onFalse(new InstantCommand(() -> I_Intake.stopIntake()));
        //intakeSlow.onTrue(new InstantCommand(() ->I_Intake.slowReverseIntake()));
        //intakeSlow.onFalse(new InstantCommand(() -> I_Intake.stopIntake()));

        /* indexer */
        //indexerIn.onTrue(new InstantCommand(() -> I_Indexer.runIndexer()));
        //indexerIn.onFalse(new InstantCommand(()-> I_Indexer.stopIndexer()));
        //indexerOut.onTrue(new InstantCommand(() -> I_Indexer.reverseIndexer()));
        //indexerOut.onFalse(new InstantCommand(()-> I_Indexer.stopIndexer()));

        /* shooter */
        //shooterRun.onTrue(new InstantCommand(() -> S_Shooter.runShooter()));
        //shooterRun.onFalse(new InstantCommand(()-> S_Shooter.stopShooter()));
        shooterReverse.onTrue(new InstantCommand(() -> S_Shooter.reverseShooter()));
        shooterReverse.onFalse(new InstantCommand(() -> S_Shooter.stopShooter()));
        slowShoot.onTrue(new InstantCommand(() -> S_Shooter.slowShoot()));
        slowShoot.onFalse(new InstantCommand(() -> S_Shooter.stopShooter()));


        //new JoystickButton(driver, XboxController.Button.kY.value)
        //.onTrue(new InstantCommand(()   -> hijackableRotation =  new AprilTagLock()))
        //.onFalse(new InstantCommand(() -> hijackableRotation = new JoystickLock()));

    }

    public void configureDefaultCommands() {
        I_Indexer.setDefaultCommand(intake_Indexer);
        I_Intake.setDefaultCommand(intake_Indexer);
        C_Climber.setDefaultCommand(climberCommand);
        S_Shooter.setDefaultCommand(ShooterAprilTags);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();

    }
}
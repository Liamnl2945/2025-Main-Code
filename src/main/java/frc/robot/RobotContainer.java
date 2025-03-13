package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.commands.autoL3;
import frc.robot.subsystems.*;

public class RobotContainer {
    //LED subsystem
    private final LEDSubsystem m_LedSubsystem = new LEDSubsystem();
    //Auto
    private static final NamedCommands NamedCommands = new NamedCommands();


    //Swerve
    private static final Swerve s_Swerve = new Swerve();

    //Elevator 
    private final Elevator e_Elevator = new Elevator();
    private final LIGHT L_leds = new LIGHT();
    private final I2CBruh I_I2C = new I2CBruh();
    private final AlgaeIntake A_AlgaeIntake = new AlgaeIntake();

    public static double[] getRobotVector() {
        return s_Swerve.getSwerveVector();
    }
    //limelight

    //AUTOS
    private final SendableChooser<Command> autoChooser;
   


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
    public final static JoystickButton heightToggle = new JoystickButton(manipulator, XboxController.Button.kLeftBumper.value);
    public final static JoystickButton L0 = new JoystickButton(manipulator, XboxController.Button.kRightBumper.value);
    public final static JoystickButton L1 = new JoystickButton(manipulator, XboxController.Button.kA.value);
    public final static JoystickButton L2 = new JoystickButton(manipulator, XboxController.Button.kX.value);
    public final static JoystickButton L3 = new JoystickButton(manipulator, XboxController.Button.kB.value);
    public final static JoystickButton L4 = new JoystickButton(manipulator, XboxController.Button.kY.value);
    public final static JoystickButton tsSoAlgaeCalibrate = new JoystickButton(manipulator, XboxController.Button.kA.value);
    public final JoystickButton elevatorButton = new JoystickButton(manipulator, translationAxis);

    public final static Trigger dpadNull = new Trigger(() -> manipulator.getPOV() == -1);
    public static final Trigger dpadUp = new Trigger(() -> manipulator.getPOV() == 0);
    public static final Trigger dpadDown = new Trigger(() -> manipulator.getPOV() == 180);



    public static final Trigger leftTriggerAxis = new Trigger(() -> manipulator.getRawAxis(XboxController.Axis.kLeftTrigger.value) > 0.1);
    public static final Trigger rightTriggerAxis = new Trigger(() -> manipulator.getRawAxis(XboxController.Axis.kRightTrigger.value) > 0.1);
    public final static DigitalInput elevatorLimitSwitch = new DigitalInput(0);
    public final static DigitalInput algaeLimitSwitch = new DigitalInput(1);
    public final static DigitalInput intakeLimitSwitch = new DigitalInput(2);


    //Elevator
    private final ElevatorCom elevatorCom = new ElevatorCom(e_Elevator, manipulator);

    
    //LEDs
    private final LEDCom LEDCom = new LEDCom(L_leds);

    //I2C
    private final I2CCom I2CCom = new I2CCom(I_I2C, manipulator);

    //AlgaeIntake
    private final AlgaeIntakeCom algaeIntakeCom = new AlgaeIntakeCom(A_AlgaeIntake, manipulator);



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
        

        s_Swerve.setDefaultCommand(

                new TeleopSwerve(
                        s_Swerve,
                        () -> -driver.getRawAxis(translationAxis),
                        () -> -driver.getRawAxis(strafeAxis),
                        () -> -driver.getRawAxis(rotationAxis),
                        () -> robotCentric.getAsBoolean(),
                        () -> false
                )
        );



        //Auto Command Builder
        Command autoL3 = new autoL3(e_Elevator);
        Command autoL3Time = autoL3.withTimeout(1.5);
        Command autoIntake = new autoIntakeCommand(e_Elevator);
        Command autoIntakeTime = autoIntake.withTimeout(.025);

        //Register named commands here
        //ie. NamedCommands.registerCommand("autoBalance", swerve.autoBalanceCommand());
        //com.pathplanner.lib.auto.NamedCommands.registerCommand("auto L3", autoL3Time);
        //com.pathplanner.lib.auto.NamedCommands.registerCommand("auto L3",H autoL3Time);
        com.pathplanner.lib.auto.NamedCommands.registerCommand("auto Intake", autoIntakeTime);
        NamedCommands.registerCommand("auto_L3", autoL3Time);
        NamedCommands.registerCommand("L3 print", Commands.print("L3 elevator"));



        //ie. new EventTrigger("run intake").whileTrue(Commands.print("running intake"));

        configureButtonBindings();
        configureDefaultCommands();


        //limelightTab = Shuffleboard.getTab("Limelight Tab");
        //L_Limelight.configLimelightTab(limelightTab);
        //L_Limelight.getShuffleboardValues();


        autoChooser = AutoBuilder.buildAutoChooser();

        SmartDashboard.putData("Auto Chooser", autoChooser);



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


       


        //new JoystickButton(driver, XboxController.Button.kY.value)
        //.onTrue(new InstantCommand(()   -> hijackableRotation =  new AprilTagLock()))
        //.onFalse(new InstantCommand(() -> hijackableRotation = new JoystickLock()));

    }

    public void configureDefaultCommands() {
        e_Elevator.setDefaultCommand(elevatorCom);
        //L_leds.setDefaultCommand(LEDCom);
        I_I2C.setDefaultCommand(I2CCom);
        A_AlgaeIntake.setDefaultCommand(algaeIntakeCom);
       
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();

    }
}
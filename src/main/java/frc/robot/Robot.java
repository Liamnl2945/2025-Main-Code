  // Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.RainbowAnimation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.LEDSubsystem;


  /**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    CANdle _candle = new CANdle(0);

  private Command m_autonomousCommand;

  limelightData aprilData = new limelightData();


    private RobotContainer m_robotContainer;

  public static CTREConfigs ctreConfigs;



  public ShuffleboardTab limelightTab;

  //Timer  rumbleTimer = new Timer();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
     ctreConfigs = new CTREConfigs();   
     m_robotContainer = new RobotContainer();
    //LEDSubsystem LEDS = new LEDSubsystem();
    RainbowAnimation animation = new RainbowAnimation();
    _candle.setLEDs(155, 155, 155);



    // Make sure you only configure port forwarding once in your robot code.
    // Do not place these function calls in any periodic functions
    for (int port = 5800; port <= 5807; port++) {
      PortForwarder.add(port, "limelight.local", port);
    }

    for (int port = 5800; port <= 5809; port++) {
      PortForwarder.add(port+10, "limelight-tag.local", port);
    }

    //m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    //m_chooser.addOption("My Auto", kCustomAuto);
    //SmartDashboard.putData("Auto choices", m_chooser);
  
    //rumbleTimer.start();

  }
  public void rumbleTask() {
    RobotContainer.manipulator.setRumble(RumbleType.kLeftRumble, 1.0);
    RobotContainer.manipulator.setRumble(RumbleType.kRightRumble, 1.0);
   }

     public void endRumbleTask() {
    RobotContainer.manipulator.setRumble(RumbleType.kLeftRumble, 0.0);
    RobotContainer.manipulator.setRumble(RumbleType.kRightRumble, 0.0);
   }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    /*if(rumbleTimer.hasElapsed(interval)){
      rumbleTask();
    }
    if(rumbleTimer.hasElapsed(interval*2)){
      endRumbleTask();
      rumbleTimer.reset();
    }*/
     CommandScheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    //m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
   // if (m_autonomousCommand != null) {
   //   m_autonomousCommand.schedule();
   // }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    aprilData.calculate();
    //switch (m_autoSelected) {
      //case kCustomAuto:
      //  // Put custom auto code here  
      //  break;
    //  case kDefaultAuto:
     // default:
        // Put default auto code here
      ///  break;
    }
  

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {

    //if (m_autonomousCommand != null) {
    //  m_autonomousCommand.cancel();
    //}

    //rumbleTimer.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    aprilData.calculate(); 
  }


  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    endRumbleTask(); 
    
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    endRumbleTask();
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
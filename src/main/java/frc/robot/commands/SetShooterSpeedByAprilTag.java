package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

public class SetShooterSpeedByAprilTag extends InstantCommand {
    private final Shooter shooter;

    public SetShooterSpeedByAprilTag(Shooter shooter) {
        this.addRequirements(shooter);
        this.shooter = shooter;
    }
    @Override
    public void execute() {

        int detectedTagID = shooter.getDetectedAprilTagID();
        double speed  = 0;
        //System.out.println("SHOOTER INITALIZED");
        if(RobotContainer.aim.getAsBoolean()){
            //System.out.println("SHOOTER SEES AIM");
            speed = switch (detectedTagID) {
                case 6 ->  // Adjust speed for BLUE AMP
                        0.175;  // Example speed for Tag 1
                //System.out.println("\n\n BLUE AMP DETECTED");
                case 7 ->  // Adjust speed for BLUE SPEAKER
                        1.0;   // Example speed for Tag 2
                //System.out.println("\n\n BLUE Speaker DETECTED");
                case 5 ->  // Adjust speed for RED AMP
                        0.175;  // Example speed for Tag 1
                //System.out.println("\n\n RED AMP DETECTED");
                case 4 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 14 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 15 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 16 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 11 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 12 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                case 13 ->  // Adjust speed for RED SPEAKER
                        1.0;   // Example speed for Tag 2\
                default -> 1.0;   // defualt speed, even if no tag is detected it will spin up when the aim is pressed
            };
            //speed = 0; //TODO only for debug so spinners dont always spin up; remove for this class to work
        }
        shooter.runShooterAtSpeed(speed);
        //shooter.runShooterAtSpeed(0);

    }
}


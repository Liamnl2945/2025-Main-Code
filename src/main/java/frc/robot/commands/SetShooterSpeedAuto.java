package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

public class SetShooterSpeedAuto extends InstantCommand {
    private final Shooter shooter;

    public SetShooterSpeedAuto(Shooter shooter) {
        this.addRequirements(shooter);
        this.shooter = shooter;
    }
    @Override
    public void execute() {

        int detectedTagID = shooter.getDetectedAprilTagID();
        double speed = switch (detectedTagID) {
            case 6 ->  // Adjust speed for BLUE AMP
                    0.175;  // Example speed for Tag 1
            //System.out.println("\n\n BLUE AMP DETECTED");
            case 7 ->  // Adjust speed for BLUE SPEAKER
                    0.8;   // Example speed for Tag 2
            //System.out.println("\n\n BLUE Speaker DETECTED");
            case 5 ->  // Adjust speed for RED AMP
                    0.175;  // Example speed for Tag 1
            //System.out.println("\n\n RED AMP DETECTED");
            case 4 ->  // Adjust speed for RED SPEAKER
                    .8;   // Example speed for Tag 2
            //System.out.println("\n\n RED Speaker DETECTED");
            default -> 1;   // defualt speed, even if no tag is detected it will spin up when the aim is pressed
        };
        //System.out.println("SHOOTER INITALIZED");
            //System.out.println("SHOOTER SEES AIM");
        shooter.runShooterAtSpeed(speed);
        //shooter.runShooterAtSpeed(0);

    }
}


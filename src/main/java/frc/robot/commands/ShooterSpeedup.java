package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class ShooterSpeedup extends InstantCommand {

    private final Shooter shooter;

    public ShooterSpeedup(Shooter shooter) {
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        shooter.shooterRevUp(); // Call the stopShooter function within the command
    }
}


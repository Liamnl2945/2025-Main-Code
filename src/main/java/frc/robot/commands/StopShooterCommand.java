package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class StopShooterCommand extends InstantCommand {

    private final Shooter shooter;

    public StopShooterCommand(Shooter shooter) {
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        shooter.stopShooter(); // Call the stopShooter function within the command
    }
}


package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class ShooterReverse extends InstantCommand {

    private final Shooter shooter;

    public ShooterReverse(Shooter shooter) {
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        shooter.reverseShooter(); 
    }
}


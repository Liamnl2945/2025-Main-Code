package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RunShooter extends Command{
    private final Shooter shooter;

    public RunShooter(Shooter shooter){
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override 
    public void execute(){
        shooter.AutorunShooter();
    }
}

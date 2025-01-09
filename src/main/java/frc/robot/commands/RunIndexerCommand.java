package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer;

public class RunIndexerCommand extends Command {

    private final indexer indexer;
    private final Timer timer = new Timer();


    public RunIndexerCommand(indexer indexer) {
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void initialize() {
        indexer.runIndexer();  // Start the indexer
        System.out.println("Indexer Auto Command Started");
    }

    
    @Override
    public void execute() {
        // Monitor limit switches or other conditions for stopping

    }

    @Override
    public void end(boolean interrupted) {
        indexer.stopIndexer();  // Stop the indexer
        System.out.println("Indexer Auto Command Ended");
    }
}

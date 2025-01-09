package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.indexer;
import frc.robot.subsystems.Intake;



public class intake_IndexerAuto extends Command {
    private final Intake intake;
    private final indexer indexer;

    public intake_IndexerAuto(Intake intake, indexer indexer){

        this.intake = intake;
        this.indexer = indexer;
        

        addRequirements(intake);
        addRequirements(indexer);
    }

    @Override
    public void execute(){
        System.out.println("INTAKE & INDEXER RUNNING");
        intake.runIntake();
        indexer.runIndexer();
        /*if(intakeStick.getPOV() == 0 || intakeStick.getPOV() == 45 || intakeStick.getPOV() == 315){
            intake.runIntake();
            indexer.runIndexer();
            System.out.println("\n INTAKE & INDEXER IN!!!");
        }
        else if(intakeStick.getPOV() == 135 || intakeStick.getPOV() == 180 || intakeStick.getPOV() == 225){
            intake.reverseIntake();
            indexer.reverseIndexer();
            System.out.println("\nt    INTAKE & INDEXER Out!!!");
        }
        else{
            intake.stopIntake();
            indexer.stopIndexer();
            //System.out.println("INTAKE & INDEXER ARE OFF");
        }
         // Check limit switches before running the indexer
         */
    }

}

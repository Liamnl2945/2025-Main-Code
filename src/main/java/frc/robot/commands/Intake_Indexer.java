package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.indexer;
import frc.robot.subsystems.Intake;
import frc.robot.RobotContainer;
import frc.robot.limelightData;
import frc.robot.RobotContainer;



public class Intake_Indexer extends Command {
    private final Intake intake;
    private final indexer indexer;
    public final Joystick intakeStick;

    public Intake_Indexer(Intake intake, indexer indexer, Joystick intakeStick){

        this.intake = intake;
        this.indexer = indexer;
        this.intakeStick = intakeStick;

        addRequirements(intake);
        addRequirements(indexer);
    }

    @Override
    public void execute(){

        if(intakeStick.getPOV() == 0 || intakeStick.getPOV() == 45 || intakeStick.getPOV() == 315 || RobotContainer.autoIntake.getAsBoolean()){
            if(frc.robot.subsystems.indexer.limitSwitchLeft.get() && frc.robot.subsystems.indexer.limitSwitchRight.get()){
                //System.out.println("RUNNING INTAKE & INDEXER");
            intake.runIntake();
            indexer.runIndexer();
            }
            else{
                //System.out.println("NOT RUNNING INTAKE & INDEXER");
                intake.stopIntake();
                indexer.stopIndexer();
            }
            //System.out.println("\n INTAKE & INDEXER IN!!!");
        }
        else if(intakeStick.getPOV() == 135 || intakeStick.getPOV() == 180 || intakeStick.getPOV() == 225){
            indexer.reverseIndexer();
           // System.out.println("\n INTAKE & INDEXER Out!!!");//unnecessary; we only need to pull the note back slightly from the shooter, not spit it all the way out
        }
        else{
            intake.stopIntake();
            indexer.stopIndexer();
            //System.out.println("INTAKE & INDEXER ARE OFF");
        }
         // Check limit switches before running the indexer
    }

}

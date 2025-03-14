package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.commands.TeleopSwerve;

import java.util.Arrays;

public class limelightData {

    NetworkTable algaeTable = NetworkTableInstance.getDefault().getTable("limelight-algae");//Tag table for april tag limelight
    NetworkTable SnakeTable = NetworkTableInstance.getDefault().getTable("limelight-snake");//TF means tensor flow, referencing image detection limelight

        public static double TagXC;
        public static double TagYC;
        public static double TagZC;
        public static double TagYaw;
        public static double snakeTagID;
        public static double algaeTagID;
        public static double tagID;
        public static double snakeXOffset;
        public static double algaeXOffset;
        public static double TagYOffset;
        public static double TagArea;
        public static boolean TagValid;
        public static boolean TagSnakeValid;
        public static boolean TagAlgaeValid;
        public static double distance2d;
        public static boolean noteValid;

        public static boolean contains(int[] arr, double index){//coding bat ahh function
            for(int i = 0; i < arr.length; i++){
                if(arr[i] == index){
                    return true;
                }
            }
            return false;
        }

    public void calculate() {

        snakeTagID = SnakeTable.getEntry("tid").getDouble(0.0);
        algaeTagID = algaeTable.getEntry("tid").getDouble(0.0);

        int[] validTags = {6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22};//Sets bool true/false based on whether a tag that is cared about is detected
        if(contains(validTags, snakeTagID) || contains(validTags, algaeTagID)){
            TagValid = true;
        }
        else{
            TagValid = false; ;
        }
        TagSnakeValid = contains(validTags, snakeTagID);
        TagAlgaeValid = contains(validTags, algaeTagID);
        //Pulls from tdclass network table variable, setting it to true if it matches the tag. This name is pulled from the file uploaded to teh limelight, not locally. To change the tag, chang ethe uploaded .txt file

        snakeXOffset = SnakeTable.getEntry("tx").getDouble(0.0);//Gets basic offset of the detected tag from network tables. This is the main source for calculating error values for pointlock PID's for the april tag aim lock
        algaeXOffset = algaeTable.getEntry("tx").getDouble(0.0);
       // System.out.println(noteValid + " " + snakeXOffset);
if(TagValid) {
    if ((TeleopSwerve.alignValue == 1) && (limelightData.TagAlgaeValid)) {
        System.out.println("Aligned Right" + " Note Valid: " + TagValid + " ID: " + algaeTagID + " Offset: " + algaeXOffset);
    } else if ((TeleopSwerve.alignValue == -1) && (limelightData.TagSnakeValid)){
        System.out.println("Aligned Left" + " Note Valid: " + TagValid + " ID: " + snakeTagID + " Offset: " + snakeXOffset);
    }
}
else{
    //System.out.println("No Note Seen");
}
    }
}

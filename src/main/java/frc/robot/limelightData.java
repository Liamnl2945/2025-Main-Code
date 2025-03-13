package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.Arrays;

public class limelightData {

    NetworkTable algaeTable = NetworkTableInstance.getDefault().getTable("limelight-algae");//Tag table for april tag limelight
    NetworkTable SnakeTable = NetworkTableInstance.getDefault().getTable("limelight-snake");//TF means tensor flow, referencing image detection limelight

        public static double TagXC;
        public static double TagYC;
        public static double TagZC;
        public static double TagYaw;
        public static double tagID;
        public static double snakeXOffset;
        public static double algaeXOffset;
        public static double TagYOffset;
        public static double TagArea;
        public static boolean TagValid;
        public static double distance2d;
        public static boolean noteValid;
        
    public void calculate() {
        

        double[] targetPose = SnakeTable.getEntry("targetpose_cameraspace").getDoubleArray(new double[6]);

        tagID = SnakeTable.getEntry("tid").getDouble(0.0);
        int[] validtags = {6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22};//Sets bool true/false based on whether a tag that is cared about is detected
        if(Arrays.asList(validtags).contains(tagID)){//todo might not work
            TagValid = true;
        }
        else{
            TagValid = false; ;
        }

        //Pulls from tdclass network table variable, setting it to true if it matches the tag. This name is pulled from the file uploaded to teh limelight, not locally. To change the tag, chang ethe uploaded .txt file
        noteValid = algaeTable.getEntry("tdclass").getString("nerd").equals("note");


        snakeXOffset = SnakeTable.getEntry("tx").getDouble(0.0);//Gets basic offset of the detected tag from network tables. This is the main source for calculating error values for pointlock PID's for the april tag aim lock
        algaeXOffset = algaeTable.getEntry("tx").getDouble(0.0);
       // System.out.println(noteValid + " " + snakeXOffset);

        TagYOffset = SnakeTable.getEntry("ty").getDouble(0.0);
        TagArea = SnakeTable.getEntry("ta").getDouble(0.0);
        TagXC = targetPose[0];
        TagYC = targetPose[1];
        TagZC = targetPose[2];
        TagYaw = targetPose[5];
    }
}

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.Arrays;

public class limelightData {

    NetworkTable TFTable = NetworkTableInstance.getDefault().getTable("limelight");//Tag table for april tag limelight
    NetworkTable TagTable = NetworkTableInstance.getDefault().getTable("limelight-tag");//TF means tensor flow, referencing image detection limelight

        public static double TagXC;
        public static double TagYC;
        public static double TagZC;
        public static double TagYaw;
        public static double tagID;
        public static double TagXOffset;
        public static double NoteXOffset;
        public static double TagYOffset;
        public static double TagArea;
        public static boolean TagValid;
        public static double distance2d;
        public static boolean noteValid;


    public void calculate() {
        

        double[] targetPose = TagTable.getEntry("targetpose_cameraspace").getDoubleArray(new double[6]);

        tagID = TagTable.getEntry("tid").getDouble(0.0);//Sets bool true/false based on whether a tag that is cared about is detected
        int[] validtags = {1, 2, 3};
        if(Arrays.asList(validtags).contains(tagID)){//todo might not work
            TagValid = true;
        }
        else{
            TagValid = false; ;
        }

        //Pulls from tdclass network table variable, setting it to true if it matches the tag. This name is pulled from the file uploaded to teh limelight, not locally. To change the tag, chang ethe uploaded .txt file
        noteValid = TFTable.getEntry("tdclass").getString("nerd").equals("note");


        TagXOffset = TagTable.getEntry("tx").getDouble(0.0);//Gets basic offset of the detected tag from network tables. This is the main source for calculating error values for pointlock PID's for the april tag aim lock
        NoteXOffset = TFTable.getEntry("tx").getDouble(0.0);
       // System.out.println(noteValid + " " + TagXOffset);

        TagYOffset = TagTable.getEntry("ty").getDouble(0.0);
        TagArea = TagTable.getEntry("ta").getDouble(0.0);
        TagXC = targetPose[0];
        TagYC = targetPose[1];
        TagZC = targetPose[2];
        TagYaw = targetPose[5];
    }
}

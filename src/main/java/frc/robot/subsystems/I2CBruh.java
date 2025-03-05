package frc.robot.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.FireAnimation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;

public class I2CBruh extends SubsystemBase {
    private static final int TCS34725_ADDRESS = 0x29;
    private static final int TCS34725_ENABLE = 0x80;
    private static final int TCS34725_ENABLE_PON = 0x01;
    private static final int TCS34725_ENABLE_AEN = 0x02;
    private static final int TCS34725_CDATAL = 0x94; // Clear channel data low byte
    private static final int TCS34725_RDATAL = 0x96; // Red channel data low byte
    private static final int TCS34725_GDATAL = 0x98; // Green channel data low byte
    private static final int TCS34725_BDATAL = 0x9A; // Blue channel data low byte

    // is datal what they say in the future instead of fatal
    //bro what
    private static I2C i2c;
    public static boolean coralDetected = false;

    public I2CBruh (){


        i2c = new I2C(I2C.Port.kOnboard, TCS34725_ADDRESS); // Use kMXP if needed

        // Enable the sensor (Power ON and Enable ADC)
        i2c.write(TCS34725_ENABLE, TCS34725_ENABLE_PON);
        try { Thread.sleep(3); } catch (InterruptedException e) {System.out.println("Eddie Smells");} // Wait for power-up
        i2c.write(TCS34725_ENABLE, TCS34725_ENABLE_PON | TCS34725_ENABLE_AEN);
         i2c.write(TCS34725_ENABLE | TCS34725_ENABLE_PON, 0xF6); // this probably sets a integration time thingy whatever


    }
// rip among us army
    public static void PrintColorData() {
        int[] colorData = getRawColors();

        coralDetected = (colorData[0] > 100);
        SmartDashboard.putBoolean("Coral Detected", coralDetected);

       // Set colors

// #GottaStackMyBread -> valid




        // System.out.println("Clear: " + colorData[0] + "  Red: " + colorData[1] + "  Green: " + colorData[2] + "  Blue: " + colorData[3] );
    }

    public static int[] getRawColors() {
        int clear = (int) Math.floor(read16(TCS34725_CDATAL)); // turn the 16 bit reading into 8 bit
        int red = (int) Math.floor(read16(TCS34725_RDATAL));
        int green = (int) Math.floor(read16(TCS34725_GDATAL));
        int blue = (int) Math.floor(read16(TCS34725_BDATAL));
        return new int[]{clear, red, green, blue};
    }

    // this is the code of
    // the robot and this is a
    // haiku in the code

    public static int read16(int register) {
        byte[] buffer = new byte[2];
        i2c.read(register, 2, buffer);
        return ((buffer[1] & 0xFF) << 8) | (buffer[0] & 0xFF);
    }

}

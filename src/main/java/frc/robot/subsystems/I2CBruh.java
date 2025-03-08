package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Elevator;

public class I2CBruh extends SubsystemBase {
    private static final int TCS34725_ADDRESS = 0x29;
    private static final int TCS34725_ENABLE = 0x80;
    private static final int TCS34725_ENABLE_PON = 0x01;
    private static final int TCS34725_ENABLE_AEN = 0x02;
    private static final int TCS34725_CDATAL = 0x94; // Clear channel data low byte
    private static final int TCS34725_RDATAL = 0x96; // Red channel data low byte
    private static final int TCS34725_GDATAL = 0x98; // Green channel data low byte
    private static final int TCS34725_BDATAL = 0x9A; // Blue channel data low byte
    private static boolean init = false;


    private static I2C i2c;
    public static boolean coralDetected = false;
    public static boolean leftLastPressed = false;

    public I2CBruh() {
        // Initialization code
    }

    public static void PrintColorData(boolean left) {

        if (left || (Elevator.intook != 0 && Elevator.intook != 4)) {
            if(!init) {
                i2c = new I2C(I2C.Port.kOnboard, TCS34725_ADDRESS); // Use kMXP if needed
                i2c.write(TCS34725_ENABLE, TCS34725_ENABLE_PON);
                i2c.write(TCS34725_ENABLE, TCS34725_ENABLE_PON | TCS34725_ENABLE_AEN);
                i2c.write(TCS34725_ENABLE | TCS34725_ENABLE_PON, 0xF6); // Set integration time or config
                init = true;
            }

            int colorData = getRawColors();
            coralDetected = (colorData > 400);
            SmartDashboard.putBoolean("Coral Detected", coralDetected);

            // Debug print the color data
            //System.out.println(colorData);
        }
        else{
            if(init) {
                i2c.close();
                CommandScheduler.getInstance().cancelAll();
            }
            init = false;
        }
    }

    public static int getRawColors() {
        return (int) (double) read16(TCS34725_CDATAL); // Convert 16-bit reading to 8-bit value
    }

    public static int read16(int register) {
        byte[] buffer = new byte[2];
        i2c.read(register, 2, buffer);
        return ((buffer[1] & 0xFF) << 8) | (buffer[0] & 0xFF);
    }
}

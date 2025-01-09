package frc.robot.PIDs;

public interface RotationSource {
    public double getR();
    public double getRd(double degrees);
}

package org.usfirst.frc.team5985.robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {
	NetworkTable visTable;
	
	double gearLiftAngle = 0;
	double gearLiftDistance = 0;
	
	public Vision() {
		visTable = NetworkTable.getTable("vision");
		visTable.addTableListener("targetAngle", updateValues, true);
		visTable.addTableListener("targetDistance", updateValues, true);
	}
	
	public void sendRequest() {
		visTable.putBoolean("request", true);
	}
	
	private void updateValues() {
		gearLiftAngle = visTable.getNumber("targetAngle", -1);
		gearLiftDistance = visTable.getNumber("targetDistance", -1);
	}
	
	private double getGearLiftAngle() {
		return gearLiftAngle;
	}
	
	private double getGearLiftDistance() {
		return gearLiftDistance;
	}
}

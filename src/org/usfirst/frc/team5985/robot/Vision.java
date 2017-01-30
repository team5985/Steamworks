package org.usfirst.frc.team5985.robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.ITable;

public class Vision implements ITableListener {
	NetworkTable visTable;
	
	double gearLiftAngle;
	double gearLiftDistance;
	
	public void sendRequest() {
		visTable.putBoolean("request", true);
	}
	
	public void valueChanged(ITable table, String string, Object o, boolean bool) {
//		System.out.println("Value Changed");
//		System.out.println("Key: " + string);
//		System.out.println("Value: " + o);
//		System.out.println("Bool: " + bool);
		if (string.contains("targetAngle")) {
			gearLiftAngle = (double) o;
//			System.out.println(gearLiftAngle);
		} else if (string.contains("targetDistance")) {
			gearLiftDistance = (double) o;
//			System.out.println(gearLiftDistance);
		}
	}
	
	public double getGearLiftAngle() {
		return gearLiftAngle;
	}
	
	public double getGearLiftDistance() {
		return gearLiftDistance;
	}
	
	public Vision() {
		visTable = NetworkTable.getTable("vision");
		visTable.addTableListener(this);
		visTable.putBoolean("request", false);
	}
	
	
}
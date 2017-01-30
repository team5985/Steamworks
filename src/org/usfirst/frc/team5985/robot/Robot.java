package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import org.usfirst.frc.team5985.subsystems.DriveTrain;
import org.usfirst.frc.team5985.subsystems.Shooter;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Shooter shooter;
	
//	CameraServer cam0;

	public void robotInit() {
		driveTrain = new DriveTrain();
		shooter = new Shooter();
		
//		cam0 = CameraServer.getInstance();
//		cam0.startAutomaticCapture();
	}
	
	public void autonomousInit() {
	}
	
	public void autonomousPeriodic() {
	}
	
	public void teleopInit() {
	}
	
    public void teleopPeriodic() {
    	driveTrain.teleopDrive();
    	shooter.runShooter();
    }
}
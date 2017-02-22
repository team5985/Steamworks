package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import org.usfirst.frc.team5985.subsystems.DriveTrain;
import org.usfirst.frc.team5985.subsystems.Shooter;
import org.usfirst.frc.team5985.subsystems.Climber;

import org.usfirst.frc.team5985.routines.Auto;
import org.usfirst.frc.team5985.routines.Auto.autoPositions;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Shooter shooter;
	Climber climber;
	
	Auto auto;
	
//	CameraServer cam0;

	public void robotInit() {
		driveTrain = new DriveTrain();
		shooter = new Shooter();
		climber = new Climber();
		auto = new Auto(autoPositions.DEFAULT);
		
//		cam0 = CameraServer.getInstance();
//		cam0.startAutomaticCapture();
	}
	
	public void autonomousInit() {
//		auto.setStartPosition(XXX); // TODO: Add smartdashboard input to this
		auto.setupAutoParameters();
	}
	
	public void autonomousPeriodic() {
		auto.runStateMachine();
	}
	
	public void teleopInit() {
	}
	
    public void teleopPeriodic() {
    	driveTrain.runTeleopDriveTrain();
//    	shooter.runShooter();
//    	driveTrain.gearAutoAim();
    	climber.runClimber();
    }
}
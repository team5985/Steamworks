package org.usfirst.frc.team5985.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;

import org.usfirst.frc.team5985.robot.Vision;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import com.kauailabs.navx.frc.AHRS;


public class DriveTrain {
	Joystick _joy;
	AHRS navx;
	
	Vision _vision;
	
	CANTalon leftDrive;
	CANTalon leftSlaveDrive;
	
	CANTalon rightDrive;
	CANTalon rightSlaveDrive;
	
	public DriveTrain() {
		_vision = new Vision();
		
		_joy = new Joystick(0);
		navx = new AHRS(SPI.Port.kMXP);
		
		
		// Left drive motors
		leftDrive = new CANTalon(2);
		leftDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		leftSlaveDrive = new CANTalon(3);
		leftSlaveDrive.changeControlMode(TalonControlMode.Follower);
		leftSlaveDrive.set(leftDrive.getDeviceID());
		
		leftDrive.setEncPosition(0);
		leftDrive.reverseSensor(false);
		
		
		// Right drive motors
		rightDrive = new CANTalon(0);
		rightDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		rightSlaveDrive = new CANTalon(1);
		rightSlaveDrive.changeControlMode(TalonControlMode.Follower);
		rightSlaveDrive.set(rightDrive.getDeviceID());
		
		rightDrive.setEncPosition(0);
	}
	
	/**
	 * Generic method that will set the power of the drive motors
	 * @param power
	 * @param steering
	 * @param throttle
	 */
	public void drive(double power, double steering, double throttle) {
		double leftPower = (-power + steering) * throttle;
    	double rightPower = (power + steering) * throttle;
    	    	
    	leftDrive.set(leftPower);
    	rightDrive.set(rightPower);
	}
	
	/**
	 * Same as drive(), but adds joystick control
	 */
	public void teleopDrive() {
		double steering = _joy.getX();
		double power = _joy.getY();
		double throttle = -(_joy.getThrottle() - 1 ) / 2;
		
		drive(power, steering, throttle);
	}
	
	public void gearAutoAim() {
		// Vision auto-aim test
    	if (_joy.getRawButton(1)) {
    		_vision.sendRequest();
//    		System.out.println("gearLiftAngle: " + _vision.getGearLiftAngle());
    		navx.reset();
    		while (_joy.getRawButton(1)) {
    		}
    	}
    	
    	double targetAngle = _vision.getGearLiftAngle();
		double robotAngle = (double) navx.getYaw();
		double angleError = targetAngle - robotAngle;
				
		double steering = 0.01 * angleError;
		System.out.println("Angle Error : " + angleError);
	}
}

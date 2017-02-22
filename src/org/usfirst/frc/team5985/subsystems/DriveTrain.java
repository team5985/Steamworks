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
	
	final int ENCODER_COUNT_PER_REVOLUTION = 1080;  // E4P Encoders
	final double DISTANCE_PER_REVOLUTION = 638.371;  // 8" Pneumatic wheels, mm
	
	private double targetHeading = 0;
	
	public DriveTrain() {
		_vision = new Vision();
		
		_joy = new Joystick(0);
		
		navx = new AHRS(SPI.Port.kMXP);
		
		
		// Right drive motors
		rightDrive = new CANTalon(13);
		rightDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		rightSlaveDrive = new CANTalon(12);
		rightSlaveDrive.changeControlMode(TalonControlMode.Follower);
		rightSlaveDrive.set(rightDrive.getDeviceID());
		
		rightDrive.setEncPosition(0);
		
		
		// Left drive motors
		leftDrive = new CANTalon(14);
		leftDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		leftSlaveDrive = new CANTalon(15);
		leftSlaveDrive.changeControlMode(TalonControlMode.Follower);
		leftSlaveDrive.set(leftDrive.getDeviceID());
		
		leftDrive.setEncPosition(0);
		leftDrive.reverseSensor(false);
		
		
	}
	
	/**
	 * Controls whether using joystick control or aiming using the gyro
	 */
	public void runTeleopDriveTrain() {
		String _state;
		if (_joy.getRawButton(1) && (_state != "GEAR_AIM")) {
			updateVisionTarget();
			_state = "GEAR_AIM";
		} else if (_joy.getRawButton(1) && (_state == "GEAR_AIM")) {
			gearAutoAim();
		} else {
			_state = "TELEOP";
			teleopDrive();
		}
	}
	
	/**
	 * Set the power of the drive motors
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
	 * Drive using the gyro sensor
	 * @param power
	 * @param throttle
	 */
	public void driveStraight(double power, double throttle) {
		double angleError = (navx.getYaw() - targetHeading) * 0.015;
		drive(power, angleError, throttle);
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
	
	/**
	 * Aim using the Vision data
	 */
	public void gearAutoAim() {
		// Vision auto-aim test
		double targetAngle = _vision.getGearLiftAngle();
		
		turnToAngle(targetAngle);
	}
	
	/**
	 * Asks the raspi for vision data
	 */
	public void updateVisionTarget() {
		_vision.sendRequest();
//		System.out.println("gearLiftAngle: " + _vision.getGearLiftAngle());
	}
	
	/**
	 * idk if use this be careful
	 */
	public void gearAutoDrive() {
		// Vision auto-drive test
		if (_joy.getRawButton(2)) {
			_vision.sendRequest();
			leftDrive.setEncPosition(0);
			rightDrive.setEncPosition(0);
			
			while (_joy.getRawButton(2)) {
			}
		}
		double targetDistance = _vision.getGearLiftDistance() / DISTANCE_PER_REVOLUTION;
		double robotDistance = getRightEncoderCount() / ENCODER_COUNT_PER_REVOLUTION;
		double distanceError = targetDistance - robotDistance;
				
		double power = 0.01 * distanceError;
		System.out.println("Distance Error : " + distanceError);
		drive(power,0,1);
	}
	
	/**
	 * Drive a certain number of rotations.
	 * @param power
	 * @param targetDistance
	 */
	public void driveDistance(double power, double targetDistance) {
		leftDrive.setEncPosition(0);
		rightDrive.setEncPosition(0);
		
		// 1 rotation = 1080
		if (leftDrive.getEncPosition() < (targetDistance * 1080)) { // 1080 counts per revolution
			driveStraight(power, 1);
		} else {
			drive(0, 0, 0);
		}
	}
	
	/**
	 * Turn to a relative angle
	 * @param targetAngle
	 */
	public void turnToAngle(double targetAngle) {
		navx.zeroAngle();
		
		double robotAngle = navx.getYaw();
		double angleError = targetAngle - robotAngle;
				
		double steering = 0.015 * angleError;
		double throttle = -(_joy.getThrottle() - 1 ) / 2;
//		System.out.println("Angle Error : " + angleError);
		drive(_joy.getY(),steering,throttle); // Only controls turning, driver still controls power
	}
	
	/**
	 * Sets a variable for driveStraight to use
	 * @param heading
	 */
	public void setTargetHeading(double heading) {
		targetHeading = heading;
	}
	
	/**
	 * Get the number of counts by the left encoder
	 * @return left encoder position
	 */
	public double getLeftEncoderCount() {
		return leftDrive.getEncPosition();
	}
	
	/**
	 * Get the number of counts by the right encoder
	 * @return right encoder position
	 */
	public double getRightEncoderCount() {
		return rightDrive.getEncPosition();
	}
	
	/**
	 * Get the rotations the left drivetrain has made
	 * @return
	 */
	public double getLeftEncoderRotations() {
		return getLeftEncoderCount() / 1080;
	}
	
	/**
	 * Get the rotations the left drivetrain has made
	 * @return
	 */
	public double getRightEncoderRotations() {
		return getRightEncoderCount() / 1080;
	}
	
	public double getYaw() {
		return navx.getYaw();
	}
}

package org.usfirst.frc.team5985.robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.CameraServer;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class Robot extends IterativeRobot {
	Vision _vision;
	
	CameraServer cam0;
	
	CANTalon frontLeftDrive;
	CANTalon backLeftDrive;
	
	CANTalon frontRightDrive;
	CANTalon backRightDrive;
  
	CANTalon _talon = new CANTalon(0);	
	Joystick _joy = new Joystick(0);	
	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	double SpeedSet = 0;
	
	public void robotInit() {
		_vision = new Vision();
		
		cam0 = CameraServer.getInstance();
		cam0.startAutomaticCapture();
		
//		
//        /* first choose the sensor */
//        _talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//        _talon.reverseSensor(true);
//        //_talon.configEncoderCodesPerRev(XXX), // if using FeedbackDevice.QuadEncoder
//        //_talon.configPotentiometerTurns(XXX), // if using FeedbackDevice.AnalogEncoder or AnalogPot
//
//        /* set the peak and nominal outputs, 12V means full */
//        _talon.configNominalOutputVoltage(+0.0f, -0.0f);
//        _talon.configPeakOutputVoltage(+12.0f, 0.0f);
//        /* set closed loop gains in slot0 */
//        _talon.setProfile(0);
//        _talon.setF(0.037);
//        _talon.setP(0.022);
//        _talon.setI(0.0); 
//        _talon.setD(0);
		
		/* Drive code */
		// Left drive motors
		frontLeftDrive = new CANTalon(2);
		frontLeftDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		backLeftDrive = new CANTalon(3);
		backLeftDrive.changeControlMode(TalonControlMode.Follower);
		backLeftDrive.set(frontLeftDrive.getDeviceID());
		
		// Right drive motors
		frontRightDrive = new CANTalon(0);
		frontRightDrive.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		backRightDrive = new CANTalon(1);
		backRightDrive.changeControlMode(TalonControlMode.Follower);
		backRightDrive.set(frontRightDrive.getDeviceID());
		
		// Auto code
		frontLeftDrive.setEncPosition(0);
		frontLeftDrive.reverseSensor(false);
		frontRightDrive.setEncPosition(0);
        
	}
	
	public void autonomousInit() {
	}
	
	public void autonomousPeriodic() {
		System.out.println(frontRightDrive.getEncPosition());
		
		if (frontRightDrive.getEncPosition() < 1696.5) { //TODO: Fix this (it's ugly)
			frontRightDrive.set(-1);
			frontLeftDrive.set(1);
		} else if (frontRightDrive.getEncPosition() < 3393) {
			frontRightDrive.set(-0.1);
			frontLeftDrive.set(0.1);
		} else {
			frontRightDrive.set(0);
			frontLeftDrive.set(0);
		}
	}
	
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	/* Shooter code
    	/* get gamepad axis 
    	double leftYstick = _joy.getAxis(AxisType.kY);
    	double motorOutput = _talon.getOutputVoltage() / _talon.getBusVoltage();
      	if ((_joy.getRawButton(5)) == true) {
    		SpeedSet = SpeedSet-0.05;
    		while ((_joy.getRawButton(5)) == true){
    		}
      	} else if ((_joy.getRawButton(6)) == true) {
    		SpeedSet = SpeedSet+0.05;
    		while ((_joy.getRawButton(6)) == true){
    		}
      	} else if ((_joy.getRawButton(3)) == true) {
    		SpeedSet = SpeedSet-0.005;
    		while ((_joy.getRawButton(3)) == true){
    		}
      	} else if ((_joy.getRawButton(4)) == true) {
    		SpeedSet = SpeedSet+0.005;
    		while ((_joy.getRawButton(4)) == true){
    		}
      	}
    	/* prepare line to print 
		_sb.append("\tout:");
		_sb.append(motorOutput);
        _sb.append("\tspd:");
        _sb.append(_talon.getSpeed());
        _sb.append(" ;Speed Set");
        _sb.append(SpeedSet);
        
        if((_joy.getRawButton(1)) == true) {
        	/* Speed mode 
        	double targetSpeed = 2293.89; /* 1500 RPM in either direction 
        	_talon.changeControlMode(TalonControlMode.Speed);
        	_talon.set(targetSpeed); /* 1500 RPM in either direction 

        	/* append more signals to print when in speed mode. 
            _sb.append("\terr:");
            _sb.append(_talon.getClosedLoopError());
            _sb.append("\ttrg:");
            _sb.append(targetSpeed);
        } else {
        	/* Percent voltage mode 
        	_talon.changeControlMode(TalonControlMode.PercentVbus);
        	_talon.set(SpeedSet);
        }

        if(++_loops >= 10) {
        	_loops = 0;
        	System.out.println(_sb.toString());
        }
        _sb.setLength(0);
        */
    	
    	//Encoder test
    	System.out.print("Left Enc: ");
    	System.out.println(frontLeftDrive.getEncPosition());
    	
    	System.out.print("Right Enc: ");
    	System.out.println(frontRightDrive.getEncPosition());
    	
    	
    	
    	// Drive code
    	double throttle = _joy.getThrottle();
    	System.out.println(throttle);
    	
    	double steering = _joy.getX();
    	double power = _joy.getY() * throttle;
    	
    	double leftPower = -power + steering;
    	double rightPower = power + steering;
    	
    	frontLeftDrive.set(leftPower);
    	frontRightDrive.set(rightPower);
    	
    }
}
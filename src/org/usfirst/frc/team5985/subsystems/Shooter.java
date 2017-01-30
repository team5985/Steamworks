package org.usfirst.frc.team5985.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	CANTalon shooterMotor;
	Joystick _joy;
	
	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	double SpeedSet = 0;
	
	public Shooter() {
		shooterMotor = new CANTalon(15);
		
		shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
	    shooterMotor.reverseSensor(true);
	
	    /* Peak and nominal outputs, 12V means full */
	    shooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
	    shooterMotor.configPeakOutputVoltage(+12.0f, 0.0f);
	    
	    /* Closed loop gains in slot0 */
	    shooterMotor.setProfile(0);
	    shooterMotor.setF(0.037);
	    shooterMotor.setP(0.022);
	    shooterMotor.setI(0.0); 
	    shooterMotor.setD(0);
	}
	
	/**
	 * Will run in teleopPeriodic
	 */
	public void runShooter() {
    	// get gamepad axis 
    	double motorOutput = shooterMotor.getOutputVoltage() / shooterMotor.getBusVoltage();
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
      	
    	/* prepare line to print */
		_sb.append("\tout:");
		_sb.append(motorOutput);
        _sb.append("\tspd:");
        _sb.append(shooterMotor.getSpeed());
        _sb.append(" ;Speed Set");
        _sb.append(SpeedSet);
        
        if((_joy.getRawButton(1)) == true) {
        	// Speed mode 
        	double targetSpeed = 2293.89; // 1500 RPM in either direction 
        	shooterMotor.changeControlMode(TalonControlMode.Speed);
        	shooterMotor.set(targetSpeed); // 1500 RPM in either direction 

        	// append more signals to print when in speed mode. 
            _sb.append("\terr:");
            _sb.append(shooterMotor.getClosedLoopError());
            _sb.append("\ttrg:");
            _sb.append(targetSpeed);
        } else {
        	// Percent voltage mode 
        	shooterMotor.changeControlMode(TalonControlMode.PercentVbus);
        	shooterMotor.set(SpeedSet);
        }

        if(++_loops >= 10) {
        	_loops = 0;
        	System.out.println(_sb.toString());
        }
        _sb.setLength(0);

	}
}

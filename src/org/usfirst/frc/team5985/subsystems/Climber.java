package org.usfirst.frc.team5985.subsystems;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;;

public class Climber {
	Victor climberMotor = new Victor(2);
	XboxController _xbox = new XboxController(1);
	
	public Climber() {
	}
	
	public void runClimber() {
		if (_xbox.getYButton()) {
			climberMotor.set(1);
		} else if (_xbox.getXButton()) {
			climberMotor.set(-1);
		} else {
			climberMotor.set(0);
		}
		
	}
}

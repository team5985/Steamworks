package org.usfirst.frc.team5985.routines;

import org.usfirst.frc.team5985.subsystems.DriveTrain;
import org.usfirst.frc.team5985.subsystems.Shooter;

public class Auto {
	DriveTrain driveTrain = new DriveTrain();
	Shooter shooter = new Shooter();
	
	private states _state;
	
	private states _finalState; // State to end Auto after completed
	
	private autoPositions _startPosition;
	private double _forwardDistance;
	private double _gearPegAngle;
	private double _reverseAngle;
	private double _reverseDistance;
	
	public enum autoPositions {
		DEFAULT,	// Drive to auto line
		
		BLUE_LEFT, 	// Gear Drop, shoot
		BLUE_MIDDLE,// Gear Drop
		BLUE_RIGHT,	// Gear Drop
		
		RED_LEFT,	// Gear Drop
		RED_MIDDLE,	// Gear Drop
		RED_RIGHT	// Gear Drop, shoot
	}
	
	private enum states {
		IDLE,			// Do nothing
		FORWARD,		// First movement
		GEAR_PEG_TURN,	// Turn roughly towards the peg
		TARGET_TRACK,	// Aim towards the peg
		MOVE_TO_PEG,	// Drive to the peg
		DROP_GEAR,		// Drop gear onto peg
		REVERSE,		// Reverse out of peg
		BOILER_TURN,	// Turn roughly to the boiler
		MOVE_TO_BOILER,	// Drive into shooting position
		SHOOT			// Shoot fuel
	}
	
	/**
	 * Constructor for Autonomous controller.
	 * @param startPosition
	 */
	public Auto(autoPositions startPosition) {
		setStartPosition(startPosition);
		setState(states.FORWARD);
	}
	
	/**
	 * Setup the parameters autonomous will use. setStartPosition must be called before this.
	 */
	public void setupAutoParameters() {
		if (_startPosition == autoPositions.DEFAULT) {
			setForwardDistance(4.5);
			setGearPegAngle(0);
			setReverseDistance(0);
			setBoilerAngle(0);
			
			setFinalState(states.FORWARD);
		} else if (_startPosition == autoPositions.BLUE_LEFT) {
			setForwardDistance(4.5);
			setGearPegAngle(60);
			setReverseDistance(1);
			setBoilerAngle(15);
			
			setFinalState(states.SHOOT);
		} else if (_startPosition == autoPositions.BLUE_MIDDLE) {
			setForwardDistance(4.5);
			setGearPegAngle(0);
			setReverseDistance(1);
			setBoilerAngle(85);
			
			setFinalState(states.REVERSE);
		} else if (_startPosition == autoPositions.BLUE_RIGHT) {
			setForwardDistance(4.5);
			setGearPegAngle(-60);
			setReverseDistance(1);
			setBoilerAngle(0);
			
			setFinalState(states.REVERSE);
		} else if (_startPosition == autoPositions.RED_LEFT) {
			setForwardDistance(4.5);
			setGearPegAngle(60);
			setReverseDistance(1);
			setBoilerAngle(0);
			
			setFinalState(states.REVERSE);
		} else if (_startPosition == autoPositions.RED_MIDDLE) {
			setForwardDistance(4.5);
			setGearPegAngle(0);
			setReverseDistance(1);
			setBoilerAngle(0);
			
			setFinalState(states.REVERSE);
		} else if (_startPosition == autoPositions.RED_RIGHT) {
			setForwardDistance(4.5);
			setGearPegAngle(-60);
			setReverseDistance(1);
			setBoilerAngle(-15);
			
			setFinalState(states.SHOOT);
		}
	}
	
	public void runStateMachine() {
		if (_state == getFinalState()) {
			setState(states.IDLE);
		}
		
		switch (_state) {
		case IDLE:
			driveTrain.drive(0, 0, 0);
			
		case FORWARD:
			driveTrain.driveDistance(0.2, getForwardDistance());
			if (driveTrain.getLeftEncoderRotations() >= getForwardDistance()) {
				driveTrain.drive(0, 0, 0);
				setState(states.GEAR_PEG_TURN);
				break;
			}
			
		case GEAR_PEG_TURN:
			driveTrain.turnToAngle(_gearPegAngle);
			if ((driveTrain.getYaw() > (getGearPegAngle() - 1)) && (driveTrain.getYaw() < (getGearPegAngle() + 1))) {
				driveTrain.drive(0,  0,  0);
				setState(states.MOVE_TO_PEG); // TODO: Switch to TARET_TRACK when that has been worked out
				break;
			}
			
		case TARGET_TRACK:
			driveTrain.gearAutoAim(); // Needs exit condition
			break;
			
		case MOVE_TO_PEG:
//			driveTrain.gearAutoDrive(); // Doesn't work yet
			driveTrain.driveDistance(0.1, 0.5);
			if (driveTrain.getLeftEncoderRotations() >= 0.5) {
				driveTrain.drive(0, 0, 0);
				setState(states.DROP_GEAR);
				break;
			}
			
		case DROP_GEAR:
			// depends whether we can make our minds up
			setState(states.REVERSE);
			break;
			
		case REVERSE:
			driveTrain.driveDistance(-0.1, -0.5);
			if (driveTrain.getLeftEncoderRotations() <= 0.5) {
				driveTrain.drive(0, 0, 0);
				setState(states.BOILER_TURN);
				break;
			}
			
		case BOILER_TURN:
			driveTrain.turnToAngle(getBoilerAngle());
			if ((driveTrain.getYaw() > (getBoilerAngle() - 1)) && (driveTrain.getYaw() < (getBoilerAngle() + 1))) {
				driveTrain.drive(0, 0, 0);
				setState(states.MOVE_TO_BOILER);
				break;
			}
			
		case MOVE_TO_BOILER:
//			driveTrain.driveDistance(0.1, XXX);
			setState(states.SHOOT);
			break;
		
		case SHOOT:
			shooter.runShooter();
		}
	}
	
	public void setState(states state) {
		_state = state;
	}
	public states getState() {
		return _state;
	}
	
	public void setFinalState(states finalState) {
		_finalState = finalState;
	}
	public states getFinalState() {
		return _finalState;
	}
	
	public void setStartPosition(autoPositions startPosition) {
		_startPosition = startPosition;
	}
	public autoPositions getStartPosition () {
		return _startPosition;
	}
	
	public void setForwardDistance (double forwardDistance) {
		_forwardDistance = forwardDistance;
	}
	public double getForwardDistance () {
		return _forwardDistance;
	}

	public void setGearPegAngle (double gearPegAngle) {
		_gearPegAngle = gearPegAngle;
	}
	public double getGearPegAngle () {
		return _gearPegAngle;
	}
	
	public void setReverseDistance (double reverseDistance) {
		_reverseDistance = reverseDistance;
	}
	public double getReverseDistance () {
		return _reverseDistance;
	}

	public void setBoilerAngle (double reverseAngle) {
		_reverseAngle = reverseAngle;
	}
	public double getBoilerAngle () {
		return _reverseAngle;
	}
}

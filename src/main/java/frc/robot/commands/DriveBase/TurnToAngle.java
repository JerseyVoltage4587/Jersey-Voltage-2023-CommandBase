/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.DriveBase;

import frc.robot.util.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.OI;
import frc.robot.subsystems.DriveBase;

public class TurnToAngle extends CommandBase {
  private double m_angle;
  private double m_finalAngle;
  private final double slowSpeed = 0.3;
  private final double fastSpeed = 0.5;
  private final double nearDelta = 10; //Threshold angle for switching between slowSpeed and fastSpeed constants
  private final DriveBase m_driveBase;
  /**
   * Creates a new TurnToAngle.
   */
  public TurnToAngle(double angle, DriveBase driveBase) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_driveBase = driveBase;
    addRequirements(driveBase);
    m_angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putString("TurnToAngle", "initialize");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double heading = Gyro.getYaw();
      m_finalAngle = heading + m_angle;

    double delta = (m_finalAngle - heading) % 360.0;
    if (delta < -180){
      delta += 360;
    }
    if (delta > 180){
      delta -= 360;
    }

    double deltaAbs = Math.abs(delta);
    double deltaSign = Math.signum(delta);

    if (deltaAbs > nearDelta) {
      m_driveBase.setLeftMotorLevel(deltaSign * fastSpeed);
      m_driveBase.setRightMotorLevel(deltaSign * -fastSpeed);
    }
    else {
      m_driveBase.setLeftMotorLevel(deltaSign * slowSpeed);
      m_driveBase.setRightMotorLevel(deltaSign * -slowSpeed);      
    }

    SmartDashboard.putString("TurnToAngle", String.format("delta: %f", delta));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveBase.setLeftMotorLevel(0);
    m_driveBase.setRightMotorLevel(0);
    SmartDashboard.putString("TurnToAngle", "end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if ((Math.abs(OI.getInstance().getDrive()) > 0) || (Math.abs(OI.getInstance().getTurn()) > 0)) {
    //   return true;
    // }

    double heading = Gyro.getYaw();
    double delta = (m_finalAngle - heading) % 360.0;
    if (delta < -180){
      delta += 360;
    }
    if (delta > 180){
      delta -= 360;
    }


    if (Math.abs(delta) <= 1){
      return true;
    }
    return false;
  }
}

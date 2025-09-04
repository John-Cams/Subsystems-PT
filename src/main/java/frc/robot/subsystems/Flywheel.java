package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.NeutralOut;


public class Flywheel extends SubsystemBase{
    private final TalonFX flywheelMotor;
    private double desiredSpeed = 0.0;
    private boolean enabled = true;
    private VelocityVoltage velocityVoltage;
    private final NeutralOut neutralRequest = new NeutralOut();

    public Flywheel() {
        flywheelMotor = new TalonFX(-1);
        velocityVoltage = new VelocityVoltage(0);
        flywheelMotor.setPosition(0.0);

        setMotorConfigs();
    }

    public void setMotorConfigs() {
        TalonFXConfiguration motorConfigs = new TalonFXConfiguration();
        motorConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        motorConfigs.Feedback.SensorToMechanismRatio = 16;
        motorConfigs.Feedback.RotorToSensorRatio = 1;

        motorConfigs.CurrentLimits.SupplyCurrentLowerLimit = 45;
        motorConfigs.CurrentLimits.SupplyCurrentLowerTime = 0.1;
        
        motorConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        motorConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        // motorConfigs.MotionMagic.MotionMagicVelocity = 
        // motorConfigs.MotionMagic.MotionMagicAcceleration =
        // motorConfigs.MotionMagic.MotionMagicJerk = 

        // motorConfigs.Slot0.kP = 
        // motorConfigs.Slot0.kG = 0;
        // motorConfigs.Slot0.kS = 0;
    }

    @Override
    public void periodic(){
        if (!enabled){
            return;
        }

        velocityVoltage.Velocity = desiredSpeed;

        flywheelMotor.setControl(velocityVoltage);
    }

    //-------------------STATE METHODS-------------------//

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        if(!enabled){
            stopMotion();
        }
    }

    public void stopMotion() {
        flywheelMotor.setControl(neutralRequest);
    }

    public void setDesiredSpeed(double speed) {
        desiredSpeed = speed;
    }

    //-------------------GET METHODS-------------------//

    public double getSpeed() {
        return flywheelMotor.get();
    }

    public boolean atSpeed() {
        return getSpeed() > getDesiredSpeed();
    }

    public double getDesiredSpeed(){
        return desiredSpeed;
    } 

    //-------------------REPORTABLE-------------------//
    // @Override
    // public void initShuffleBoard(LOG_LEVEL priority){
    //     Will be added soon
    // }
}

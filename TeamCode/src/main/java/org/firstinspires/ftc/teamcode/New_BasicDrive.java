package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;


@TeleOp(name = "JustDrive_Holo", group = "NEW")
public class New_BasicDrive extends OpMode {
    private GamepadEx driver, operator;
    private Gamepad driverCtrl, operatorCtrl;

    private Gamepad.RumbleEffect speedNormModeNotify, speedSlowModeNotify;

    private Motor fL, fR, bL, bR;

    private MecanumDrive drive;

    public void init() {
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        driverCtrl = gamepad1;
        operatorCtrl = gamepad2;

        speedNormModeNotify = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0,0.0,100)
                .addStep(0.0,0.0,50)
                .addStep(1.0,0.0,100)
                .build();
        speedSlowModeNotify = new Gamepad.RumbleEffect.Builder()
                .addStep(0.75,0.0,100)
                .addStep(0.50,0.0,100)
                .addStep(0.25,0.0,100)
                .build();

        fL = new Motor(hardwareMap, "fL");
        fR = new Motor(hardwareMap, "fR");
        bL = new Motor(hardwareMap, "bL");
        bR = new Motor(hardwareMap, "bR");

        drive = new MecanumDrive(fL, fR, bL, bR);
    }

    public void init_loop() {
        telemetry.speak("aeiou");
    }

    public void start() {
        telemetry.clearAll();
        telemetry.speak("sup bitch");
    }

    public void loop() {

        // ---------- DRIVER CONTROLS ---------- //

        telemetry.addLine("DRIVER STATUS");

        double strafe = driver.getLeftX(),
                fwd = driver.getLeftY(),
                rotate = driver.getRightX();

        // Use left stick button to toggle slow mode
        ToggleButtonReader slowModeToggle = new ToggleButtonReader(driver, GamepadKeys.Button.LEFT_STICK_BUTTON);

        if (slowModeToggle.getState()) {
            telemetry.addData("Drive Mode","Slowed");
            strafe *= 0.5;
            fwd *= 0.5;
            rotate *= 0.5;
        } else {
            telemetry.addData("Drive Mode","Normal");
        }

        if (slowModeToggle.stateJustChanged() && !driverCtrl.isRumbling()) { // Trigger rumble to notify driver of speed mode
            if (slowModeToggle.getState()) {
                driverCtrl.runRumbleEffect(speedSlowModeNotify);
                telemetry.speak("Slow Mode");
            } else {
                driverCtrl.runRumbleEffect(speedNormModeNotify);
                telemetry.speak("Normal Mode");
            }
        }

        telemetry.addData("Lateral X Speed","%s", strafe);
        telemetry.addData("Lateral Y Speed", "%s", fwd);
        telemetry.addData("Rotational Speed","%s",rotate);

        drive.driveRobotCentric(strafe, fwd, rotate);

        telemetry.update();
    }

    public void stop(){
        telemetry.speak("ah fuck, i've been shot!");
    }
}

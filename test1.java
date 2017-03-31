import lejos.nxt.Motor;
import lejos.nxt.*;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.objectdetection.*;

/**
 * 
 * @author tgibbons, mmadondo
 * https://lejosnews.wordpress.com/2015/01/17/lejos-navigation/
 *
 */
public class test1 {


	public static void hitBall(int time) throws InterruptedException
	{
		Motor.B.setSpeed(1500);
		Motor.C.setSpeed(1500);
		Motor.B.forward();
		Motor.C.forward();
		Thread.sleep(time);
		Motor.B.stop(true);
		Motor.C.stop(true);
		Thread.sleep(2000);
	}

	public static void findBall() throws InterruptedException
	{
		UltrasonicSensor usonic;

		usonic = new UltrasonicSensor(SensorPort.S1);
		int dist;
		dist = usonic.getDistance();

		Motor.B.setSpeed(200);
		Motor.C.setSpeed(200);
		while (dist>15)
		{
			Motor.B.forward();
			Motor.C.forward();
			dist = usonic.getDistance();	
		}

		Motor.B.stop(true);
		Motor.C.stop(true);
		Thread.sleep(1000);
	}

	public static void turnMotor(int angle){
		Motor.B.rotate(angle);
		Motor.B.setSpeed(1500);
		Motor.C.setSpeed(1500);
		//Motor.C.rotate(45);
	}

	public static void main(String[] options) throws InterruptedException {


		//		  Motor.B.forward();
		//		  Button.waitForAnyPress();
		//		  Motor.B.backward();
		//Button.waitForAnyPress();
		//GoForward(500);
		
	
			findBall();
			hitBall(200);
			findBall();
			//turnMotor(60);
			hitBall(200);
			findBall();
			//turnMotor(-35);
			hitBall(200);
			findBall();
			//turnMotor(60);
			hitBall(200);
			findBall();
			//turnMotor(-60);
			hitBall(200);
			findBall();
			//turnMotor(-45);
			hitBall(200);
			findBall();
			hitBall(200);
		
		
	}

	public static void featureDetected(Feature feature, FeatureDetector detector) throws Exception{
		UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S4);
		
		int maxDist = 15; // In centimeters
		int range = (int)feature.getRangeReading().getRange();
		
		RangeFeatureDetector fd = new RangeFeatureDetector(sonar, maxDist, 500);
		while(range>10){
			
			findBall();
			hitBall(200);
		}
		
		turnMotor(-90);
		if(range>10){
			findBall();
			hitBall(200);
		} else {
			turnMotor(90);
			findBall();
			hitBall(200);
		}
		
	}
}


	

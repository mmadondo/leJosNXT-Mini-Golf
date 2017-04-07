import java.util.ArrayList;
import java.util.List;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Stopwatch;
import lejos.robotics.navigation.*;

public class lineSensorTest {

	public static DifferentialPilot pilotGreen;
	public static DifferentialPilot pilot;
	
	public static void hitBall(int time) throws InterruptedException
	{

		pilotGreen.setTravelSpeed(2000);
		pilotGreen.travel(time);
		pilotGreen.stop();
		Thread.sleep(2000);
	}

	public static void findBall() throws InterruptedException
	{
		UltrasonicSensor usonic;

		usonic = new UltrasonicSensor(SensorPort.S1);
		int dist = usonic.getDistance();
		
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
		
		final int STRAIGHT = 0;               // constants for the direction  
		final int LEFT = 1;                   // used by the status
		final int RIGHT = 2;
      //  final int RED = 0;
       // final int WHITE = 6;                 // Should use Color.WHITE  

        final int TURN_AMOUNT = 160;          // amount to turn when line following
        final int SPEED_LINE_FOLLOWING = 100; // speed when following the red line
        final int SCALE_STRAIGHT = 1;		  // amount to scale going straight from red line to green hole
        final int SCALE_TURN = 3;		      // amount to scale when turning from red line to green hole
        
		ArrayList<Float> distList = new ArrayList<Float>();         // list of the distance travelled following the red line
        ArrayList<Integer> dirList = new ArrayList<Integer>();      // list of directions travelled
		ColorSensor lightLeft = new ColorSensor(SensorPort.S2);
		ColorSensor lightRight = new ColorSensor(SensorPort.S3);
		
		pilot = new DifferentialPilot(60f,60f,20f,Motor.B, Motor.C,false);
		pilotGreen = new DifferentialPilot(60f,63f,20f,Motor.B, Motor.C,false);
		
		pilot.setTravelSpeed(SPEED_LINE_FOLLOWING);
		pilot.reset();
		
		LCD.drawString("Press run to start",0,0);
		Button.ENTER.waitForPressAndRelease();
		
		pilot.forward();
		dirList.add(STRAIGHT);
		int status = STRAIGHT; 		// 0=straight, 1=right, 2=left
        
        // ====== follow the red line to determine the course ========
		while (! Button.LEFT.isDown()){
			LCD.drawString("Right%: ", 0, 1);
			LCD.drawInt(lightRight.getColorID(), 3, 9, 1);
			//LCD.drawInt(lightRight.getColor().getRed(), 3, 9, 2);
			if (lightRight.getColorID()== Color.WHITE && lightLeft.getColorID()== COLOR.RED){
				if (status != LEFT) {
					float distance = pilot.getMovement().getDistanceTraveled();
					distList.add(distance);                         // add the distance to the list
 					dirList.add(LEFT);                              // add the direction to the list                   
					LCD.drawInt((int)distance, 0, 0);
					status = LEFT;
                    pilot.steer(TURN_AMOUNT);
				}

			} else if (lightRight.getColorID()== COLOR.RED && lightLeft.getColorID()==  ){
            	if (status != RIGHT) {
    				float distance = pilot.getMovement().getDistanceTraveled();
    				distList.add(distance);                         // add the distance to the list
                    dirList.add(RIGHT);                             // add the direction to the list  
    				LCD.drawInt((int)distance, 0, 0);
    				status = RIGHT;
                    pilot.steer(-1*TURN_AMOUNT);
            	}

            } else {
				if (status != STRAIGHT){
					float distance = pilot.getMovement().getDistanceTraveled();
					distList.add(distance);                         // add the distance to the list
                    dirList.add(STRAIGHT);                          // add the direction to the list 
					LCD.drawInt((int)distance, 0, 0);
					status = STRAIGHT;
                    pilot.forward();
				}

			}
			Thread.sleep(10);       // sleep for a micro second before checking things again
		} // while
        
		float distance = pilot.getMovement().getDistanceTraveled();
		distList.add(distance);                         // add the distance to the list

        // stop for and wait until the right button is pushed
		pilot.stop();
		Button.RIGHT.waitForPressAndRelease();
		
        //======= Run the course on the actual golf hole with the ball =======
        int i = 0;
		for (float d: distList) {
			LCD.drawString("dist",0,0);
            LCD.drawInt((int)d, 6, 0);                  // display the distance on the first line 
            if (dirList.get(i)==STRAIGHT) {
                LCD.drawString("STRAIGHT",0,2);
                //pilot.travel(d * SCALE_STRAIGHT);
                while(d>0){
                       LCD.drawString("dist",0,0);
                       LCD.drawInt((int)d, 6, 0);                  // display the distance on the first line 
                	   hitBall(250);
                       findBall();
                       d = d - 150;
                }
             
            } else if (dirList.get(i)==RIGHT) {
                LCD.drawString("RIGHT   ",0,2);
    			LCD.drawString("dist",0,0);
                LCD.drawInt((int)d, 6, 0);                  // display the distance on the first line 
            	//if (d > 100) {
                pilotGreen.setTravelSpeed(1000);
                pilotGreen.travelArc(-1*TURN_AMOUNT/10, d * 2.5);
            	//}
            } else if (dirList.get(i)==LEFT) {
                LCD.drawString("LEFT    ",0,2);
    			LCD.drawString("dist",0,0);
                LCD.drawInt((int)d, 6, 0);                  // display the distance on the first line 
            	//if (d > 100) {
                pilotGreen.setTravelSpeed(1000);
                pilotGreen.travelArc(TURN_AMOUNT/10, d * 2.5);
                //pilotGreen.travelArc(+10, 100);
            	//}
            }
            i++;
            Thread.sleep(2000); 
		}
		
		
	
	}
	
}

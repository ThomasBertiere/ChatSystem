package controller;

import java.util.Timer;
import java.util.TimerTask;


public class TestTimer {
	
	Timer timer ; 
	static int timerEnd ; 
	
	public TestTimer(int sec) {
		timer=new Timer() ; 
		timer.schedule(new TestTask(),sec*1000);
	}
	
	class TestTask extends TimerTask{

		@Override
		public void run() {
			System.out.println("Time's up ! ");
			timerEnd=1 ; 
			timer.cancel();
		}
		
	}
	
	
	public static void main(String [] args) {
		timerEnd=0 ;
		long t1 = System.currentTimeMillis() ; 
		TestTimer t = new TestTimer(5) ;
		System.out.println("Test");
		t.timer.cancel();
		

	}
	

}

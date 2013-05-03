package rishi.projects;

public class Timer {
	
	long startTime, elapsed, totalTime, liveTime, currentSecond;
	boolean isRunning;
	
	long getLiveTimeMillis() {
		liveTime = totalTime - (System.currentTimeMillis() - startTime);
		currentSecond = liveTime/1000;
		return liveTime;
	}
	
	void setStop() {
		elapsed = System.currentTimeMillis() - startTime;
	}
	
	void setStart() {
		startTime = System.currentTimeMillis() - elapsed;
	}
	
	Timer() {
		this.startTime = this.elapsed =  this.liveTime = this.currentSecond = 0;
		this.totalTime = 300000;
		this.isRunning = false;
	}

}

package rishi.projects;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ChessClockActivity extends Activity {
	/** Called when the activity is first created. */
	boolean orientation_change = false, real_click = true;
	final int SETTINGS_ID = Menu.FIRST, RESET_ID = Menu.FIRST + 1;
	Runnable updateTimeTask1, updateTimeTask2;
	Handler handler;
	Button player1,player2;
	Timer timer1, timer2;
	long delay;
	
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		restartClocks();
	}

	@Override
	protected void onResume() {
		super.onResume();
		restartClocks();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		pauseClocks();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SETTINGS_ID, 0, "Settings");
        menu.add(0, RESET_ID, 0, "Reset");
        pauseClocks();
        return true;
    }
    
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	pauseClocks();
		return super.onPrepareOptionsMenu(menu);
		
	}

	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case SETTINGS_ID:
                Intent i = new Intent(this, Settings.class);
                Bundle b = new Bundle();
                b.putLong("time1", timer1.totalTime);
                b.putLong("time2", timer2.totalTime);
                i.putExtras(b);
                startActivityForResult(i,1);
                return true;
            case RESET_ID:
            	startClocks();
            	break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
        Bundle b = intent.getExtras();
    	timer1.totalTime = b.getLong("time1");
    	timer2.totalTime = b.getLong("time2");
        startClocks();
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(timer1.isRunning) timer1.setStop();
		else if (timer2.isRunning) timer2.setStop(); 
		handler.removeCallbacks(updateTimeTask1);
		handler.removeCallbacks(updateTimeTask2);
		outState.putLong("totalTime1", timer1.totalTime);
		outState.putLong("totalTime2", timer2.totalTime);
		outState.putLong("elapsed1", timer1.elapsed);
		outState.putLong("elapsed2", timer2.elapsed);
		outState.putLong("liveTime1", timer1.liveTime);
		outState.putLong("liveTime2", timer2.liveTime);
		outState.putBoolean("one_running", timer1.isRunning);
		outState.putBoolean("two_running", timer2.isRunning);
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        timer1 = new Timer();
        timer2 = new Timer();
        
        int orientation=getScreenOrientation();
        if(orientation == 0) {
        	player1 = (UpsideDownButton) findViewById(R.id.player1);
        	player2 = (Button) findViewById(R.id.player2);
        }
        else if (orientation == 1) {
        	player1 = (Button) findViewById(R.id.player1);
        	player2 = (Button) findViewById(R.id.player2);
        }
        else if (orientation == 2) {
        	player1 = (Button) findViewById(R.id.player2);
        	player2 = (Button) findViewById(R.id.player1);
        }
        else if (orientation == 3) {
        	player1 = (Button) findViewById(R.id.player2);
        	player2 = (UpsideDownButton) findViewById(R.id.player1);
        }
		
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DS-DIGII.TTF");
        player1.setTypeface(tf);
        player2.setTypeface(tf);
		delay=10;
		handler=new Handler();
        
		updateTimeTask1=new Runnable(){
			
			public void run() {
				long second = timer1.currentSecond;
				player1.setText(toTimeString(timer1.getLiveTimeMillis()));
				if(timer1.liveTime <= 20000 && timer1.currentSecond != second) {
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.beep);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
					mp.start();
				}
				if (timer1.liveTime <= 100) {
					player1.setText("TIME'S UP");
					player2.setText("YOU WIN");
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.end);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
			              
					mp.start();
			        pauseClocks();
				}
				else
				handler.postDelayed(this, delay);
			}
		};
		
		updateTimeTask2=new Runnable(){
			
			public void run() {
				long second = timer2.currentSecond;
				player2.setText(toTimeString(timer2.getLiveTimeMillis()));
				if(timer2.liveTime <= 20000 && timer2.currentSecond != second) {
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.beep);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
					mp.start();
				}
				if (timer2.liveTime <=100) {
					player2.setText("TIME'S UP");
					player1.setText("YOU WIN");
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.end);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
			      
					mp.start();
			        
					pauseClocks();
									
				}
				else
				handler.postDelayed(this, delay);
			}
		};
		
		player1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (real_click) {
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.tap);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
					mp.start();
				}
				else real_click = true;
				player1.setClickable(false);
				player2.setClickable(true);
				player1.setBackgroundColor(0xff3364c2);
				player2.setBackgroundColor(0xfff7d72b);
				player1.setTextColor(0xff44c400);
				player2.setTextColor(0xfff31900);
				handler.removeCallbacks(updateTimeTask2);
				handler.removeCallbacks(updateTimeTask1);
				if(timer1.isRunning) {
					timer1.setStop();
				}
				timer2.isRunning = true;
				timer1.isRunning = false;
				timer2.setStart();
				handler.postDelayed(updateTimeTask2, delay);
			}
		});
		
		player2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (real_click) {
					MediaPlayer mp = MediaPlayer.create(ChessClockActivity.this, R.raw.tap);
			        mp.setOnCompletionListener(new OnCompletionListener() {

			            @Override
			            public void onCompletion(MediaPlayer mp) {
			                mp.release();
			            }

			        });
					mp.start();
				}
				else real_click = true;
				player2.setClickable(false);
				player1.setClickable(true);
				player2.setBackgroundColor(0xff3364c2);
				player1.setBackgroundColor(0xfff7d72b);
				player2.setTextColor(0xff44c400);
				player1.setTextColor(0xfff31900);
				handler.removeCallbacks(updateTimeTask1);
				handler.removeCallbacks(updateTimeTask2);		
				if(timer2.isRunning) {
					timer2.setStop();
				}
				timer2.isRunning=false;
				timer1.isRunning=true;
				timer1.setStart();
				handler.postDelayed(updateTimeTask1, delay);
			}
		});
		
		if(savedInstanceState != null) {
			handler.removeCallbacks(updateTimeTask1);
			handler.removeCallbacks(updateTimeTask2);
			timer1.totalTime = (long) savedInstanceState.getLong("totalTime1");
			timer2.totalTime = (long) savedInstanceState.getLong("totalTime2");
			timer1.elapsed = (long) savedInstanceState.getLong("elapsed1");
			timer2.elapsed = (long) savedInstanceState.getLong("elapsed2");
			timer1.liveTime = (long) savedInstanceState.getLong("liveTime1");
			timer2.liveTime = (long) savedInstanceState.getLong("liveTime2");
			timer1.isRunning = (boolean) savedInstanceState.getBoolean("one_running");
			timer2.isRunning = (boolean) savedInstanceState.getBoolean("two_running");
			orientation_change = true;
		}
		else
		startClocks();
        
    }
	
	void startClocks() {
		handler.removeCallbacks(updateTimeTask1);
		handler.removeCallbacks(updateTimeTask2);
		timer1.isRunning = timer2.isRunning = false;
		timer1.liveTime = timer1.totalTime;
		timer2.liveTime = timer2.totalTime;
		player1.setText(toTimeString(timer1.totalTime));
		player2.setText(toTimeString(timer2.totalTime));
		player1.setClickable(true);
		player2.setClickable(true);
		timer1.startTime = timer2.startTime = 0;
		timer1.elapsed = timer2.elapsed = 0;
	}
	
	void restartClocks() {
		player1.setText(toTimeString(timer1.liveTime));
		player2.setText(toTimeString(timer2.liveTime));
		if (timer1.isRunning) {
			player1.setClickable(false);
			player2.setClickable(true);
			timer1.isRunning=false;
			if (orientation_change) {
				real_click = false;
				player2.performClick();
			}
		}
		else if (timer2.isRunning) {
			player2.setClickable(false);
			player1.setClickable(true);
			timer2.isRunning=false;
			if (orientation_change) {
				real_click = false;
				player1.performClick();
			}
		}
		if(orientation_change) {
			orientation_change = false;
		}
		if(player1.isClickable() == false && player2.isClickable() == false) {
			player1.setClickable(true);
			player2.setClickable(true);
		}
	}
	
	void pauseClocks() {
		handler.removeCallbacks(updateTimeTask1);
		handler.removeCallbacks(updateTimeTask2);
		player1.setClickable((false));
		player2.setClickable((false));
		if (timer1.isRunning) {
			timer1.setStop();
		}
		else if (timer2.isRunning) {
			timer2.setStop();
		}
	}
	
	String toTimeString(long timeMillis) {
		String timeString="",temp="";
		timeMillis/=1000;
		if(timeMillis/3600 > 0) {
			timeString=String.valueOf(timeMillis/3600);
			timeString=timeString+":";
		}
		timeMillis%=3600;
		temp=String.valueOf(timeMillis/60);
		if (temp.length()==1) temp="0"+temp;
		timeString=timeString+temp;
		timeString=timeString+":";
		timeMillis%=60;
		temp=String.valueOf(timeMillis);
		if (temp.length()==1) temp="0"+temp;
		timeString=timeString+temp;
		return timeString;
	}
	
	int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = getOrient.getRotation();
	    if(orientation == Surface.ROTATION_0) orientation = 0;
	    else if (orientation == Surface.ROTATION_90) orientation = 1;
	    else if (orientation == Surface.ROTATION_270) orientation = 2;
	    else if (orientation == Surface.ROTATION_180) orientation = 3;
	    else orientation = 0;
	    return orientation;
	}
    
}
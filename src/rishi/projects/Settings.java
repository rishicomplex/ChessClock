package rishi.projects;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

public class Settings extends Activity {
	
	EditText hh1, hh2, mm1, mm2, ss1, ss2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        hh1 = (EditText) findViewById(R.id.hh1);
        hh2 = (EditText) findViewById(R.id.hh2);
        mm1 = (EditText) findViewById(R.id.mm1);
        mm2 = (EditText) findViewById(R.id.mm2);
        ss1 = (EditText) findViewById(R.id.ss1);
        ss2 = (EditText) findViewById(R.id.ss2);
        Bundle extras = getIntent().getExtras();
        long totalTime1 = extras.getLong("time2")/1000;
        long totalTime2 = extras.getLong("time1")/1000;
        long temp;
        
        temp = totalTime1/3600;
        hh1.setText(getZeroString(temp));
        totalTime1%=3600;
        temp = totalTime1/60;
        mm1.setText(getZeroString(temp));
        totalTime1%=60;
        temp = totalTime1;
        ss1.setText(getZeroString(temp));
        
        temp = totalTime2/3600;
        hh2.setText(getZeroString(temp));
        totalTime2%=3600;
        temp = totalTime2/60;
        mm2.setText(getZeroString(temp));
        totalTime2%=60;
        temp = totalTime2;
        ss2.setText(getZeroString(temp));

        
	}
	
	
	@Override
	public void onBackPressed() {
		long totalTime1 = 0, totalTime2 = 0;
		Bundle bundle = new Bundle();
		String temp = "";
		
		temp = String.valueOf(hh1.getText());
		totalTime1 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		totalTime1 *= 60;
		temp = String.valueOf(mm1.getText());
		totalTime1 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		totalTime1 *= 60;
		temp = String.valueOf(ss1.getText());
		totalTime1 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		bundle.putLong("time2", totalTime1*1000);
		
		temp = String.valueOf(hh2.getText());
		totalTime2 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		totalTime2 *= 60;
		temp = String.valueOf(mm2.getText());
		totalTime2 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		totalTime2 *= 60;
		temp = String.valueOf(ss2.getText());
		totalTime2 += (temp.length() == 0) ? 0 : Integer.parseInt(temp);
		bundle.putLong("time1", totalTime2*1000);
		Intent i = new Intent();
		i.putExtras(bundle);
		setResult(RESULT_OK, i);
		super.onBackPressed();
	}

	String getZeroString(long no) {
		String temp = String.valueOf(no);
		if(temp.length() == 1) temp = "0" + temp;
		return temp;
	}
	
	
	

}

package rishi.projects;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class UpsideDownButton extends Button{

	public UpsideDownButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public UpsideDownButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UpsideDownButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.save();
		float coord_y = this.getHeight()/2.0f;
		float coord_x = this.getWidth()/2.0f;
		canvas.rotate(180,coord_x,coord_y);
		
		
		super.onDraw(canvas);
	}

}

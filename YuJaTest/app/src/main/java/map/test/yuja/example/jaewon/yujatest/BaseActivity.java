package map.test.yuja.example.jaewon.yujatest;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import static map.test.yuja.example.jaewon.yujatest.R.*;


public class BaseActivity extends Activity implements View.OnClickListener {
	
	private RelativeLayout contentView = null;
	private static Context mCtx = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.setContentView(layout.base_activity);
		mCtx = this;
		
		contentView  = (RelativeLayout)findViewById(id.contentView);
		 
		
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	
	@Override
	public void setContentView(int res)  {
		
		contentView.removeAllViews();
		
		LayoutInflater inflater;
		inflater = LayoutInflater.from(this);
		
		View item = inflater.inflate(res, null);
		contentView.addView(item, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				
	}

	
	
	@Override
	public void setContentView(View view) {
		
		contentView.removeAllViews();
		
		contentView.addView(view, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
	}
	
	
	public void addView(View v)
	{
		contentView.removeAllViews();
		contentView.addView(v, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	
	
	@Override
	public void onClick(View v) {
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}

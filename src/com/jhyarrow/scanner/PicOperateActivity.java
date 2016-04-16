package com.jhyarrow.scanner;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.jhyarrow.scanner.util.ToneLayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PicOperateActivity extends Activity implements OnSeekBarChangeListener{
	private ToneLayer toneLayer;
	private ImageView imageView;
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_operate);	
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		toneLayer = new ToneLayer(this);
		if(resultCode == RESULT_OK){
			Uri uri = data.getData();
			Log.v("uri",uri.toString());
			ContentResolver cr = this.getContentResolver();  
            try {  
            	if (bitmap != null){
            		bitmap.recycle();
            	}
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri)); 
                System.out.println(bitmap);
                imageView = (ImageView) findViewById(R.id.picOperate);  
                imageView.setImageBitmap(bitmap);
                ((LinearLayout) findViewById(R.id.tone_view)).addView(toneLayer.getParentView());
                
                ArrayList<SeekBar> seekBars = toneLayer.getSeekBars();
                for (int i = 0, size = seekBars.size(); i < size; i++){
                	seekBars.get(i).setOnSeekBarChangeListener(this);
                }

            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
    }  


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int flag = (Integer)seekBar.getTag();
		switch(flag){
		case ToneLayer.FLAG_SATURATION:
			toneLayer.setSaturation(progress);
			break;
		case ToneLayer.FLAG_LUM:
			toneLayer.setLum(progress);
			
		case ToneLayer.FLAG_HUE:
			toneLayer.setHue(progress);
			break;
		}
		imageView.setImageBitmap(toneLayer.handleImage(bitmap, flag));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}  

}

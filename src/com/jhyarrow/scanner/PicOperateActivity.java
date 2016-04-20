package com.jhyarrow.scanner;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.jhyarrow.scanner.util.PicHandle;
import com.jhyarrow.scanner.util.ToneLayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PicOperateActivity extends Activity implements OnSeekBarChangeListener{
	private ToneLayer toneLayer;
	private ImageView imageView;
	private Bitmap bitmap;
	private Button rotate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_operate);	
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 1);
		rotate = (Button) findViewById(R.id.rotate);
		rotate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth()
						,bitmap.getHeight(),matrix,true);
				bitmap = bmp;
				imageView = (ImageView)findViewById(R.id.picOperate);
				imageView.setImageBitmap(bmp);
			}
		});
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
                
                //压缩图片
                long start1 = System.currentTimeMillis();
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 780, 1040);
                long end1 = System.currentTimeMillis();
                System.out.println("压缩处理时间:" + (end1 - start1));
                System.out.println("bitmap byte count:" + bitmap.getByteCount());
                System.out.println("bitmap height" + bitmap.getHeight());
                System.out.println("bitmap width" + bitmap.getWidth());
                
                //直方图均衡化处理
                long start3 = System.currentTimeMillis();
                bitmap = PicHandle.histogramEqualization(bitmap);
                long end3 = System.currentTimeMillis();
                System.out.println("直方图均衡化处理时间:" + (end3 - start3));
                System.out.println("bitmap byte count:" + bitmap.getByteCount());
                System.out.println("bitmap height" + bitmap.getHeight());
                System.out.println("bitmap width" + bitmap.getWidth());
                

                
                LayoutParams para;
                imageView = (ImageView) findViewById(R.id.picOperate);  
                para = imageView.getLayoutParams();
                para.height = 1040;
                para.width = 780;
                imageView.setLayoutParams(para);
                imageView.setImageBitmap(bitmap);
                ((LinearLayout) findViewById(R.id.tone_view)).addView(toneLayer.getParentView());
                
                ArrayList<SeekBar> seekBars = toneLayer.getSeekBars();
                for (int i = 0, size = seekBars.size(); i < size; i++){
                	seekBars.get(i).setOnSeekBarChangeListener(this);
                }
                rotate = (Button) findViewById(R.id.rotate);
        		rotate.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				Matrix matrix = new Matrix();
        				matrix.postRotate(90);
        				bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth()
        						,bitmap.getHeight(),matrix,true);
        				imageView = (ImageView)findViewById(R.id.picOperate);
        				imageView.setImageBitmap(bitmap);
        			}
        		});
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
		bitmap = toneLayer.handleImage(bitmap, flag);
		imageView.setImageBitmap(bitmap);
		
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

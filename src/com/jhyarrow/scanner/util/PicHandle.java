package com.jhyarrow.scanner.util;

import android.graphics.Bitmap;
import android.graphics.Color;

public class PicHandle {
	/**
	 * 图片锐化（拉普拉斯变换）
	 * @param bmp
	 * @return
	 */
	public static Bitmap sharpenImageAmeliorate(Bitmap bmp)
	{
		// 拉普拉斯矩阵
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 直方图均衡化通过hsv
	 * @param bitmap
	 * @return
	 */
	public static Bitmap histogramEqualization(Bitmap bitmap){
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int[] pixels = new int[width * height];
		int[] values = new int[257];
		for(int i=0;i<values.length;i++){
			values[i] = 0;
		}
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		float[] hsv = new float[3];
		int max = 0;
		int min = 300;
		for(int i=0; i<pixels.length; i++){
			int g = Color.green(pixels[i]);
			int r = Color.red(pixels[i]);
			int b = Color.blue(pixels[i]);
			Color.RGBToHSV(r, g, b, hsv);
			int value = Math.round(hsv[2] * 256);
			if(max < value){
				max = value;
			}
			if(min > value){
				min = value;
			}
			values[value]++;
		}
		float[] p = new float[257];//v值的概率
		p[0] = values[0];
		for(int i=1;i<p.length;i++){
			p[i] = p[i-1] + values[i];
		}
		for(int i=0;i<pixels.length;i++){
			int g = Color.green(pixels[i]);
			int r = Color.red(pixels[i]);
			int b = Color.blue(pixels[i]);
			Color.RGBToHSV(r, g, b, hsv);
			int value = Math.round(hsv[2] * 256);
			hsv[2] = (p[value] * (max-min) /(width * height) + min) / 256;
			pixels[i] = Color.HSVToColor(hsv);
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}

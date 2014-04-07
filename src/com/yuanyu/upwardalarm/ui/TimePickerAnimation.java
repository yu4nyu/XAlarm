package com.yuanyu.upwardalarm.ui;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Step 1: calculate radius
 * Step 2: initialize picker
 * Step 3: apply animation
 * @author YUAN Yu
 */
public enum TimePickerAnimation {
	
	INSTANCE;
	
	private final static String TAG = "TimePickerAnimation";
	
	class PickerPositions {
		public float beforeX;
		public float beforeY;
		public float currentX;
		public float currentY;
		public float afterX;
		public float afterY;
		
		public PickerPositions() {
			beforeX = 0;
			beforeY = 0;
			currentX = 0;
			currentY = 0;
			afterX = 0;
			afterY = 0;
		}
		
		public PickerPositions(PickerPositions pp) {
			beforeX = pp.beforeX;
			beforeY = pp.beforeY;
			currentX = pp.currentX;
			currentY = pp.currentY;
			afterX = pp.afterX;
			afterY = pp.afterY;
		}
		
		public void addDeltaX(float beforeDelta, float currentDelta, float afterDelta) {
			beforeX += beforeDelta;
			currentX += currentDelta;
			afterX += afterDelta;
		}
		
		public void addDeltaY(float beforeDelta, float currentDelta, float afterDelta) {
			beforeY += beforeDelta;
			currentY += currentDelta;
			afterY += afterDelta;
		}
	}
	
	private final PickerPositions mHourPositions = new PickerPositions();
	private final PickerPositions mMinutePositions = new PickerPositions();
	private PickerPositions mCurrentPositions;
	private float mCurrentStandardX;
	private float mCurrentStandardY;
	
	private float mRadius;
	private float mImageHeight;
	
	private int mCurrentHour = 8;

	/**
	 * Suppose width of the image part is 1/2 of radius
	 * @param image
	 */
	public void calculateRadius(ImageView image) {
		mImageHeight = image.getHeight();
		mRadius = (float) (mImageHeight / Math.sqrt(3));
		
		mImageHeight /= 2;
		mRadius /= 2;
		
		Log.d(TAG, "Got radius = " + mRadius);
		Log.d(TAG, "Got image height = " + mImageHeight);
	}
	
	/**
	 * The origin is the left-top corner of image view
	 * @param x
	 * @param y
	 * @return
	 */
	private float[] getHourStandardPosition(float x, float y) {
		float[] result = new float[2];
		
		// Convert the origin to the center of circle
		float newX = x + mRadius/2;
		float newY = mImageHeight/2 - y;
		double angle = Math.atan(newY/newX);
		result[0] = (float) (mRadius * Math.cos(angle));
		result[1] = (float) (mRadius * Math.sin(angle));
		
		// Convert the origin back to the left-top corner of image view
		result[0] -= mRadius/2;
		result[1] = mImageHeight/2 - result[1];
		
		//Log.d(TAG, "Got standard positions x = " + result[0] + " y = " + result[1]);
		
		return result;
	}
	
	/**
	 * The origin is the left-top corner of image view
	 * @param x
	 * @param y
	 * @return
	 */
	private float[] getMinuteStandardPosition(float x, float y) {
		// TODO
		float[] result = new float[2];
		
		// Convert the origin to the center of circle
		float newX = x + mRadius/2;
		float newY = mImageHeight/2 - y;
		double angle = Math.atan(newY/newX);
		result[0] = (float) (mRadius * Math.cos(angle));
		result[1] = (float) (mRadius * Math.sin(angle));
		
		// Convert the origin back to the left-top corner of image view
		result[0] -= mRadius/2;
		result[1] = mImageHeight - result[1];
		
		return result;
	}
	
	public void initHourPicker(TextView before, TextView current, TextView after, float x, float y) {
		mHourPositions.beforeX = before.getX();
		mHourPositions.beforeY = before.getY();
		mHourPositions.currentX = current.getX();
		mHourPositions.currentY = current.getY();
		mHourPositions.afterX = after.getX();
		mHourPositions.afterY = after.getY();
		
		mCurrentPositions = new PickerPositions();
		float[] positions = getHourStandardPosition(x, y);
		mCurrentStandardX = positions[0];
		mCurrentStandardY = positions[1];
	}
	
	public void initMinutePicker(TextView before, TextView current, TextView after, float x, float y) {
		mMinutePositions.beforeX = before.getX();
		mMinutePositions.beforeY = before.getY();
		mMinutePositions.currentX = current.getX();
		mMinutePositions.currentY = current.getY();
		mMinutePositions.afterX = after.getX();
		mMinutePositions.afterY = after.getY();
		mCurrentPositions = new PickerPositions();
		float[] positions = getMinuteStandardPosition(x, y);
		mCurrentStandardX = positions[0];
		mCurrentStandardY = positions[1];
	}
	
	public void hourPickerAnimation(TextView before, TextView current, TextView after, float x, float y) {	
		float[] positions = getHourStandardPosition(x, y);
		float deltaX = positions[0] - mCurrentStandardX;
		float deltaY = positions[1] - mCurrentStandardY;
		mCurrentStandardX = positions[0];
		mCurrentStandardY = positions[1];
		
		//Log.d(TAG, "deltaX = " + deltaX + " deltaY = " + deltaY);
		
		hourPickerTranslateAnimation(before, current, after, deltaY);
	}
	
	private float getSlope(TextView before, TextView current) {
		float beforeX = before.getX() + before.getWidth()/2;
		float beforeY = before.getY() + before.getHeight()/2;
		float currentX = current.getX() + current.getWidth()/2;
		float currentY = current.getY() + current.getHeight()/2;
		return (currentX - beforeX) / (currentY - beforeY);
	}
	
	/**
	 * Scrolled the before hour to the current hour
	 */
	private void changeCurrentToAfter(TextView before, TextView current, TextView after) {
		before.setText("" + (mCurrentHour%24));
		current.setText("" + ((mCurrentHour+1)%24));
		after.setText("" + ((mCurrentHour+2)%24));
	}
	
	/**
	 * Scrolled the before hour to the current hour
	 */
	private void changeCurrentToBefore(TextView before, TextView current, TextView after) {
		if(mCurrentHour >= 2) {
			before.setText("" + (mCurrentHour-2));
			current.setText("" + (mCurrentHour-1));
			after.setText("" + (mCurrentHour));
		}
		else if(mCurrentHour == 1) {
			before.setText("" + 23);
			current.setText("" + (mCurrentHour-1));
			after.setText("" + (mCurrentHour));
		}
		else if(mCurrentHour == 0) {
			before.setText("" + 22);
			current.setText("" + 23);
			after.setText("" + mCurrentHour);
		}
	}
	
	private void hourPickerTranslateAnimation(TextView before, TextView current, TextView after, float deltaY) {
		float slope = getSlope(before, current);
		if(deltaY > 0) { // Scroll downwards
			Animation beforeAnimation = new TranslateAnimation(mCurrentPositions.beforeX, deltaY*slope, mCurrentPositions.beforeY, deltaY);
			before.startAnimation(beforeAnimation);
			Animation currentAnimation = new TranslateAnimation(mCurrentPositions.currentX, -deltaY*slope, mCurrentPositions.currentY, deltaY);
			current.setAnimation(currentAnimation);
			Animation afterAnimation = new TranslateAnimation(mCurrentPositions.afterX, -deltaY*slope*2, mCurrentPositions.afterY, deltaY/2);
			after.setAnimation(afterAnimation);
			
			mCurrentPositions.addDeltaX(deltaY*slope, -deltaY*slope, -deltaY*slope*2);
			mCurrentPositions.addDeltaY(deltaY, deltaY, deltaY/2);
		}
		else { // Scroll upwards
			Animation beforeAnimation = new TranslateAnimation(mCurrentPositions.beforeX, deltaY*slope*2, mCurrentPositions.beforeY, deltaY/2);
			before.startAnimation(beforeAnimation);
			Animation currentAnimation = new TranslateAnimation(mCurrentPositions.currentX, deltaY*slope, mCurrentPositions.currentY, deltaY);
			current.setAnimation(currentAnimation);
			Animation afterAnimation = new TranslateAnimation(mCurrentPositions.afterX, -deltaY*slope, mCurrentPositions.afterY, deltaY);
			after.setAnimation(afterAnimation);
			
			mCurrentPositions.addDeltaX(deltaY*slope*2, deltaY*slope, -deltaY*slope);
			mCurrentPositions.addDeltaY(deltaY/2, deltaY, deltaY);
		}
	}
	
	public void minutePickerAnimation() {
		
	}
}

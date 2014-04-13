package com.yuanyu.upwardalarm.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.yuanyu.upwardalarm.AlarmDefineStandardActivity;
import com.yuanyu.upwardalarm.MainActivity;
import com.yuanyu.upwardalarm.R;
import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Manager;
import com.yuanyu.upwardalarm.model.Utils;

public class AlarmItemsManager implements View.OnTouchListener {

	private final static float ALPHA_PRESSED = 0.5f;
	private final static float ALPHA_NORMAL = 1.0f;

	private final Context mContext;
	private final List<Alarm> mData;
	private final List<ViewHolder> mItems;
	private ViewGroup mContainer;
	
	private View mTouchedView;
	private final GestureDetector mGestureDetector;
	GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO scroll to delete
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if(mTouchedView != null) {
				mTouchedView.setAlpha(ALPHA_PRESSED);
				editAlarm((Integer)mTouchedView.getTag());
			}
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if(mTouchedView != null) {
				mTouchedView.setAlpha(ALPHA_NORMAL);
			}
			return false;
		}
	};

	class ViewHolder {
		public View layout;

		public ImageView image1; // image view at the right top corner
		public ImageView image2; // image view at the left side of image1
		public TextView repeat;
		public TextView time;
		public Switch enable;
	}

	public AlarmItemsManager(Context context, List<Alarm> data) {
		mContext = context;
		mData = data;
		mItems = new ArrayList<ViewHolder>();
		
		mGestureDetector = new GestureDetector(context, mGestureListener);
	}

	public void fillAlarmList(ViewGroup container) {
		mItems.clear();
		mContainer = container;
		for(int i = 0; i < mData.size(); i++) {
			ViewHolder holder = createView(i);
			mItems.add(holder);
			updateView(holder, i);
			container.addView(holder.layout);
			holder.layout.setOnTouchListener(this);
			// TODO apply animation
		}
	}

	/**
	 * @param position
	 * @return true if updated, false means error
	 */
	public boolean update(int position, Alarm alarm) {
		if(position < 0 || position >= mItems.size()) {
			return false;
		}
		mData.remove(position);
		mData.add(position, alarm);
		ViewHolder holder = mItems.get(position);
		updateView(holder, position);

		return true;
	}

	/**
	 * Add alarm at the front of list
	 */
	public void add(Alarm alarm) {
		mData.add(0, alarm);
		ViewHolder holder = createView(0);
		mItems.add(0, holder);
		updateView(holder, 0);
		mContainer.addView(holder.layout, 0);
		holder.layout.setOnTouchListener(this);
		updateIndexTags();
	}

	public void remove(int position) {
		mData.remove(position);
		mItems.remove(position);
		mContainer.removeViewAt(position);
		updateIndexTags();
	}

	private ViewHolder createView(int position) {
		// Inflate and find views
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View layout = inflater.inflate(R.layout.alarm_list_item, null);

		// Create view holder
		ViewHolder holder = new ViewHolder();
		holder.layout = layout;
		holder.layout.setTag(position); // Let the view know its own position.
		holder.image1 = (ImageView) layout.findViewById(R.id.alarm_list_top_right_icon);
		holder.image2 = (ImageView) layout.findViewById(R.id.alarm_list_icon_left_to_top_right_icon);
		holder.repeat = (TextView) layout.findViewById(R.id.alarm_list_item_repeat_text);
		holder.time = (TextView) layout.findViewById(R.id.alarm_list_item_time);
		holder.enable = (Switch) layout.findViewById(R.id.alarm_list_item_switch);

		return holder;
	}

	private void updateView(ViewHolder holder, int position) {
		// Set ringtone and vibrate icons
		Alarm alarm = mData.get(position);
		Ringtone ringtone = Manager.INSTANCE.getRingtone(mContext, alarm.getRingtoneUri());
		if(alarm.getVibrateEnable()) {
			if(ringtone != null) {
				setRingtoneIcon(holder.image1);
				setVibrateIcon(holder.image2);
			}
			else {
				setVibrateIcon(holder.image1);
				holder.image2.setVisibility(View.INVISIBLE);
			}
		}
		else {
			if(ringtone != null) {
				setRingtoneIcon(holder.image1);
				holder.image2.setVisibility(View.INVISIBLE);
			}
			else {
				holder.image1.setVisibility(View.INVISIBLE);
				holder.image2.setVisibility(View.INVISIBLE);
			}
		}

		// Set text
		holder.repeat.setText(Utils.getRepeatText(mContext, alarm));
		holder.time.setText(Utils.getTimeText(alarm.getHour(), alarm.getMinute()));
		holder.enable.setChecked(alarm.getEnable());
	}
	
	private void updateIndexTags() {
		for(int i = 0; i < mItems.size(); i++) {
			mItems.get(i).layout.setTag(i);
		}		
	}

	private void setVibrateIcon(ImageView imageView) {
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageResource(R.drawable.ic_vibrate);
		int margin = mContext.getResources().getDimensionPixelSize(R.dimen.item_top_icon_calibration);
		MarginLayoutParams params = (MarginLayoutParams ) imageView.getLayoutParams();
		params.topMargin = margin;
		params.rightMargin = margin;
		imageView.setLayoutParams(params);
	}

	private void setRingtoneIcon(ImageView imageView) {
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageResource(R.drawable.ic_ringtone);
		MarginLayoutParams params = (MarginLayoutParams ) imageView.getLayoutParams();
		params.topMargin = 0;
		imageView.setLayoutParams(params);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mTouchedView = v;
		mGestureDetector.onTouchEvent(event);
		return true;
	}
	
	private void editAlarm(int position) {
		Intent intent = new Intent(mContext, AlarmDefineStandardActivity.class);
		intent.putExtra(AlarmDefineStandardActivity.EXTRA_ALARM, mData.get(position));
		intent.putExtra(AlarmDefineStandardActivity.EXTRA_POSITION, position);
		((MainActivity)mContext).startActivityForResult(intent, MainActivity.ACTIVITY_ALARM_EDIT);
	}
}

package com.yuanyu.upwardalarm;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class AlarmListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Alarm> mData;
	
	public AlarmListAdapter(Context context, List<Alarm> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Alarm getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.alarm_list_item, null);
			holder = new ViewHolder();
			holder.handle = (ImageView) convertView.findViewById(R.id.alarm_list_item_handle);
			holder.image1 = (ImageView) convertView.findViewById(R.id.alarm_list_top_right_icon);
			holder.image2 = (ImageView) convertView.findViewById(R.id.alarm_list_icon_left_to_top_right_icon);
			holder.time = (TextView) convertView.findViewById(R.id.alarm_list_item_time);
			holder.enable = (Switch) convertView.findViewById(R.id.alarm_list_item_switch);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Set ringtone and vibrate icons
		Alarm alarm = mData.get(position);
		if(alarm.getVibrateEnable()) {
			if(alarm.getRingtone() != null) {
				// TODO
			}
			else {
				// TODO
			}
		}
		else {
			if(alarm.getRingtone() != null) {
				// TODO
			}
			else {
				// TODO
			}
		}
		
		holder.time.setText(alarm.getHour() + ":" + alarm.getMinute());
		holder.enable.setChecked(alarm.getEnable());
		
		return convertView;
	}

	class ViewHolder {
		public ImageView handle; // TODO delete this ?
		public ImageView image1; // image view at the right top corner
		public ImageView image2; // image view at the left side of image1
		public TextView time;
		public Switch enable;
	}
}

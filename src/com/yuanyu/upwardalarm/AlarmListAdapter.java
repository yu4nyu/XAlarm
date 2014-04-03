package com.yuanyu.upwardalarm;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		// TODO
		return null;
	}

}

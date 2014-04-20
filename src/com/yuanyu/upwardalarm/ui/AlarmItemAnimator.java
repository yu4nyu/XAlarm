package com.yuanyu.upwardalarm.ui;

import com.yuanyu.upwardalarm.R;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class AlarmItemAnimator {
	
	static void shiftFromLeft(Context context, View item) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.shift_in_from_left);
		item.startAnimation(anim);
	}
	
	static void shiftFromRight(Context context, View item) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.shift_in_from_right);
		item.startAnimation(anim);
	}
	
	static void throwUp(Context context, View item) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.throw_up);
		item.startAnimation(anim);
	}
	
	static void throwUpDelayed(Context context, final View item, long delay) {
		final Animation anim = AnimationUtils.loadAnimation(context, R.anim.throw_up);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				item.startAnimation(anim);
			}
		}, delay);
	}
}

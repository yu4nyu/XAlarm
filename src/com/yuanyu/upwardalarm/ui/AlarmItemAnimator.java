package com.yuanyu.upwardalarm.ui;

import com.yuanyu.upwardalarm.R;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
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
}

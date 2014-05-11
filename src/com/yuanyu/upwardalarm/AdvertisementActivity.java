package com.yuanyu.upwardalarm;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AdvertisementActivity extends Activity {

	private static final String AD_UNIT_ID = "ca-app-pub-3028123579469785/8682166555";

	private InterstitialAd mInterstitial;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertisement);
		setTitle(R.string.advertisement);
		
		mTextView = (TextView) findViewById(R.id.activity_advertisement_txt);

		mInterstitial = new InterstitialAd(this);
		mInterstitial.setAdUnitId(AD_UNIT_ID);
		mInterstitial.setAdListener(new AdListener(){
			@Override
			public void onAdLoaded() {
				displayInterstitial();
			}
			@Override
			public void onAdClosed() {
				finish();
				overridePendingTransition(R.anim.shift_in_from_right, R.anim.shift_out_to_left);
			}
			@Override
			public void onAdFailedToLoad(int errorCode) {
				mTextView.setText(R.string.load_failed);
			}
		});

		AdRequest adRequest = new AdRequest.Builder().build();
		mInterstitial.loadAd(adRequest);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.shift_in_from_right, R.anim.shift_out_to_left);
	}

	private void displayInterstitial() {
		if (mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}
}
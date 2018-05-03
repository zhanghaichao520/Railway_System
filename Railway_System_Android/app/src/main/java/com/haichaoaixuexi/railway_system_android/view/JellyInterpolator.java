package com.haichaoaixuexi.railway_system_android.view;

import android.view.animation.LinearInterpolator;

/**
 *
 * 登陸界面視頻播放尺寸計算
 */
public class JellyInterpolator extends LinearInterpolator {
	private float factor;

	public JellyInterpolator() {
		this.factor = 0.15f;
	}

	@Override
	public float getInterpolation(float input) {
		return (float) (Math.pow(2, -10 * input)
				* Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
	}
}

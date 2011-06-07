package com.geekyouup.android.ustopwatch.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekyouup.android.ustopwatch.StopwatchView;
import com.geekyouup.android.ustopwatch.StopwatchView.StopwatchThead;
import com.geekyouup.android.ustopwatch.UltimateStopwatch;

public class StopwatchFragment  extends Fragment {

	private StopwatchView mStopwatchView;
	private StopwatchThead mWatchThread;
	public static final int MODE_STOPWATCH=0;
	public static final int MODE_COUNTDOWN=1;
	private UltimateStopwatch mApp;
	private Handler mHandler;
	
	private static final String PREFS_NAME="USW_SWFRAG_PREFS";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("USW","StopwatchFragment: onCreateView");

		mStopwatchView = new StopwatchView(getActivity(), null);
		mWatchThread = mStopwatchView.getThread();
		return mStopwatchView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		mWatchThread.saveState(editor);
		editor.commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();

		mWatchThread = mStopwatchView.createNewThread();
		if(mApp != null) mWatchThread.setApplication(mApp);
		if(mHandler!=null) mWatchThread.setHandler(mHandler);
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Log.d("USW","Resume settings has state set to: " + settings.getInt("state", -1));
		mStopwatchView.restoreState(settings);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("USW","StopwatchFragment: onDestroyView");

	}
	
	public void startStop()
	{
		mWatchThread.startStop();
	}
	
	public void reset()
	{
		mWatchThread.reset();
	}

	public int getMode()
	{
		return mWatchThread.isStopwatchMode()?MODE_STOPWATCH:MODE_COUNTDOWN;
	}
	
	public void setMode(int mode)
	{
		mWatchThread.setIsStopwatchMode(mode==MODE_STOPWATCH);
	}
	
	public void setTime(int hour, int minute, int seconds)
	{
		mWatchThread.setTime(hour, minute, seconds);
	}
	
	public void setApplication(UltimateStopwatch app)
	{
		mApp = app;
		if(mWatchThread!= null) mWatchThread.setApplication(app);
	}
	
	public void setHandler(Handler h)
	{
		mHandler = h;
		if(mWatchThread!=null) mWatchThread.setHandler(h);
	}
	
}
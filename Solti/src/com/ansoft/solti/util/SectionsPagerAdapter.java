package com.ansoft.solti.util;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ansoft.solti.R;
import com.ansoft.solti.fragments.InboxFragment;

import com.ansoft.solti.fragments.NotificationFragment;
import com.ansoft.solti.fragments.RadarFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	protected Context mContext;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		
		switch(position) {
			case 0:
				return new InboxFragment();
			case 1:
				return new NotificationFragment();
			case 2:
				return new RadarFragment();
			
			
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

	

	public int getPageIcon(int i) {
		Locale l = Locale.getDefault();
		switch (i) {
		case 0:
			return R.drawable.newsfeed_icon;
		case 1:
			return R.drawable.ic_not;
		case 2:
			return R.drawable.radar_icon;
		
			
		}
		return (Integer) null;
		
	}
	
	
}
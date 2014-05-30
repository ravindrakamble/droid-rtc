package com.droidrtc.adapters;


import com.droidrtc.fragments.ChannelsFragment;
import com.droidrtc.fragments.ChatFragment;
import com.droidrtc.fragments.ContactsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ContactsFragment();
		case 1:
			// Games fragment activity
			return new ChatFragment();
		case 2:
			// Movies fragment activity
			return new ChannelsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}

package com.sm.sls_app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.sm.sls_app.activity.R;
import com.sm.sls_app.indicator.widget.TabPageIndicator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*资讯*/
public class InformationFragment extends Fragment {
	private final String[] TITLES = new String[] { "彩票资讯", "专家推荐", "站点公告" };
	private final int[] NEWTYPES = new int[] { 2, 3, 1 };

	private TabPageIndicator information_indicator;
	private ViewPager information_viewPager;
	private MyPagerAdapter adapter;
	private List<InformationItemFragment> fragments = new ArrayList<InformationItemFragment>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Context contextThemeWrapper = new ContextThemeWrapper(getActivity(),
				R.style.StyledIndicators);
		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View v = localInflater.inflate(R.layout.information_fragment,
				container, false);
		information_indicator = (TabPageIndicator) v
				.findViewById(R.id.information_fragment_indicator);
		information_viewPager = (ViewPager) v
				.findViewById(R.id.information_fragment_viewPager);
		adapter = new MyPagerAdapter(getChildFragmentManager());

		information_viewPager.setAdapter(adapter);
		information_indicator.setViewPager(information_viewPager, 2);
		return v;
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			// InformationItemFragment fragment = InformationItemFragment
			// .newInstance(arg0, NEWTYPES[arg0]);
			// fragments.add(fragment);
			return InformationItemFragment.newInstance(arg0, NEWTYPES[arg0]);
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TITLES[position % TITLES.length];
		}

	}
}

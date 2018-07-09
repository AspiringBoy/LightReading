package com.dreamer_yy.lightreading.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dreamer_yy.lightreading.bean.Channel;
import com.dreamer_yy.lightreading.ui.news.fragment.DetailFragment;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/5/21.
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Channel> channels;

    public ChannelPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.channels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(channels.get(position).getChannelId(),position);
    }

    @Override
    public int getCount() {
        return channels == null ? 0 :channels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channels.get(position).getChannelName();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

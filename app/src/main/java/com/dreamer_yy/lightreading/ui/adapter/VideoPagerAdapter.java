package com.dreamer_yy.lightreading.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dreamer_yy.lightreading.bean.VideoChannelBean;
import com.dreamer_yy.lightreading.ui.video.fragment.VideoDetailFragment;

/**
 * Created by Dreamer__YY on 2018/8/27.
 */

public class VideoPagerAdapter extends FragmentStatePagerAdapter {
    private VideoChannelBean videoChannelBean;

    public VideoPagerAdapter(FragmentManager fm, VideoChannelBean videoChannelBean) {
        super(fm);
        this.videoChannelBean = videoChannelBean;
    }

    @Override
    public Fragment getItem(int position) {
        return VideoDetailFragment.getInstance("clientvideo_" + videoChannelBean.getTypes().get(position).getId());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return videoChannelBean.getTypes().get(position).getName();
    }

    @Override
    public int getCount() {
        return videoChannelBean != null ? videoChannelBean.getTypes().size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

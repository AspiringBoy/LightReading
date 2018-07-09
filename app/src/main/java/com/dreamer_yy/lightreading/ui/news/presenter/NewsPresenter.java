package com.dreamer_yy.lightreading.ui.news.presenter;

import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BasePresenter;
import com.dreamer_yy.lightreading.bean.Channel;
import com.dreamer_yy.lightreading.database.ChannelDao;
import com.dreamer_yy.lightreading.ui.news.contract.NewsContract;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Dreamer__YY on 2018/5/17.
 */

public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {

    @Inject
    public NewsPresenter() {
    }

    @Override
    public void getChannel() {
        List<Channel> channels;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channels = ChannelDao.getChannels();
        if (channels == null || channels.size() < 1) {
            channels = new ArrayList<>();
            List<String> channelNames = Arrays.asList(MyApp.getContext().getResources().getStringArray(R.array.news_channel));
            List<String> channelIds = Arrays.asList(MyApp.getContext().getResources().getStringArray(R.array.news_channel_id));
            ArrayList<Channel> channelList = new ArrayList<>();
            for (int i = 0; i < channelNames.size(); i++) {
                Channel channel = new Channel();
                channel.setChannelId(channelIds.get(i));
                channel.setChannelName(channelNames.get(i));
                channel.setChannelType(i < 1 ? 1 : 0);
                channel.setChannelSelect(i < channelIds.size() - 3);
                if (i < channelIds.size() - 3) {
                    myChannels.add(channel);
                } else {
                    otherChannels.add(channel);
                }
                channelList.add(channel);
            }
            channels.addAll(channelList);
            DataSupport.saveAllAsync(channelList).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {

                }
            });
        }else {
            Iterator<Channel> iterator = channels.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                if (!channel.isChannelSelect()) {
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channels);
        }
        mView.loadData(myChannels,otherChannels);
    }
}

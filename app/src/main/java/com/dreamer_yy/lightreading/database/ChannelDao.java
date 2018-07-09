package com.dreamer_yy.lightreading.database;

import com.dreamer_yy.lightreading.bean.Channel;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/5/17.
 */

public class ChannelDao {

    /**
     * 获取所有频道
     * @return
     */
    public static List<Channel> getChannels(){
        return DataSupport.findAll(Channel.class);
    }

    /**
     * 保存所有频道
     * @param channels
     */
    public static void saveChannels(final List<Channel> channels){
        if (channels == null || channels.size() == 0) return;
        DataSupport.deleteAllAsync(Channel.class).listen(new UpdateOrDeleteCallback() {
            @Override
            public void onFinish(int rowsAffected) {
                DataSupport.markAsDeleted(channels);
                DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                    @Override
                    public void onFinish(boolean success) {

                    }
                });
            }
        });
    }

    /**
     * 清空所有频道
     */
    public static void cleanChannels(){
        DataSupport.deleteAll(Channel.class);
    }
}

package com.dreamer_yy.lightreading.ui.video.contract;

import com.dreamer_yy.lightreading.base.BaseContract;
import com.dreamer_yy.lightreading.bean.VideoChannelBean;
import com.dreamer_yy.lightreading.bean.VideoDetailBean;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/8/27.
 */

public interface VideoContract {
   interface View extends BaseContract.BaseView{
       void loadVideoChannel(List<VideoChannelBean> channelBean);

       void loadVideoDetails(List<VideoDetailBean> detailBean);

       void loadMoreVideoDetails(List<VideoDetailBean> detailBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        /**
         * 获取视频频道列表
         */
        void getVideoChannel();

        /**
         * 获取视频列表
         *
         * @param page     页码
         * @param listType 默认list
         * @param typeId   频道id
         */
        void getVideoDetails(int page, String listType, String typeId);
    }
}

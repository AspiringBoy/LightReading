package com.dreamer_yy.lightreading.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamer_yy.lightreading.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dreamer__YY on 2018/8/8.
 */

public class NewsDelPopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<Integer> selectid = new ArrayList<>();

    public NewsDelPopAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_backreason, item);
        if (selectid.contains(helper.getAdapterPosition())) {
            helper.getView(R.id.tv_backreason).setBackground(mContext.getResources().getDrawable(R.drawable.delpop_tv_selected_bg));
            helper.setTextColor(R.id.tv_backreason, mContext.getResources().getColor(android.R.color.holo_red_light));
        } else {
            helper.getView(R.id.tv_backreason).setBackground(mContext.getResources().getDrawable(R.drawable.delpop_tv_bg));
            helper.setTextColor(R.id.tv_backreason, mContext.getResources().getColor(android.R.color.black));
        }
    }

    public void updateItems(int i) {
        if (selectid.contains(i)) {
            selectid.remove((Object) i);
        } else {
            selectid.add(i);
        }
        notifyItemChanged(i);
    }

    public boolean hasItemSelected() {
        return selectid.size() > 0 ? true : false;
    }

    public void resetSelectId(){
        selectid.clear();
    }
}

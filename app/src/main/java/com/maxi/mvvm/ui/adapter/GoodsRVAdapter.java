package com.maxi.mvvm.ui.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.maxi.mvvm.R;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.databinding.ItemGoodsBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maxi on 2021/9/14.
 */
public class GoodsRVAdapter extends BaseQuickAdapter<HuoPanListBean, BaseViewHolder> {
    public GoodsRVAdapter() {
        super(R.layout.item_goods);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HuoPanListBean huoPanListBean) {

        // 获取 Binding
        ItemGoodsBinding binding = baseViewHolder.getBinding();
        if (binding != null) {
            // 设置数据
            binding.setDataBean(huoPanListBean);
            binding.executePendingBindings();
        }

        if(null!=huoPanListBean.getShipTypeJson()&&huoPanListBean.getShipTypeJson().size()>0){

            if(huoPanListBean.getShipTypeJson().size()>2){
                binding.tvDemand.setText("船型要求：不限");
            }else{
                String str = "";
                for (int i = 0 ; i < huoPanListBean.getShipTypeJson().size() ; i ++){
                    str = str + huoPanListBean.getShipTypeJson().get(i).getName() + ",";
                }

                if(str.endsWith(",")){
                    str = str.substring(0, str.length()-1);
                }

                binding.tvDemand.setText("船型要求：" + str );
            }
        }else{
            binding.tvDemand.setText("船型要求：");
        }

    }
}
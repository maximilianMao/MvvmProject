package com.maxi.mvvm.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.maxi.mvvm.R;
import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.databinding.ItemGoodsBinding;
import com.maxi.mvvm.databinding.ItemShipsBinding;
import com.maxi.mvvm.ui.activity.ship.ShipSourceActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maxi on 2021/10/12.
 */
public class ShipRVAdapter extends BaseQuickAdapter<FindShipBean, BaseViewHolder> implements OnItemChildClickListener{
    public ShipRVAdapter() {
        super(R.layout.item_ships);
        setOnItemChildClickListener(this);
        addChildClickViewIds(R.id.tv_ship_info);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FindShipBean findShipBean) {

        // 获取 Binding
        ItemShipsBinding binding = baseViewHolder.getBinding();
        if (binding != null) {
            // 设置数据
            binding.setDataBean(findShipBean);
            binding.executePendingBindings();
        }
    }

    @Override
    public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
        switch (view.getId()) {
            case R.id.tv_ship_info:
                ShipSourceActivity.open(getContext(), (FindShipBean) adapter.getData().get(position));
                break;
        }
    }
}
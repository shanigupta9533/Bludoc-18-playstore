package com.likesby.bludoc.utils.NoPaginate.paginate;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.utils.NoPaginate.callback.OnAdapterChangeListener;

final class WrapperAdapterObserver extends RecyclerView.AdapterDataObserver {

    private WrapperAdapter wrapperAdapter;
    private OnAdapterChangeListener adapterChangeListener;


    WrapperAdapterObserver(OnAdapterChangeListener adapterChangeListener, WrapperAdapter wrapperAdapter) {
        this.wrapperAdapter = wrapperAdapter;
        this.adapterChangeListener = adapterChangeListener;
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
        adapterChangeListener.onAdapterChange();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        adapterChangeListener.onAdapterChange();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
        adapterChangeListener.onAdapterChange();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        adapterChangeListener.onAdapterChange();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
        adapterChangeListener.onAdapterChange();
    }

    @Override
    public void onChanged() {
        wrapperAdapter.notifyDataSetChanged();
        adapterChangeListener.onAdapterChange();
    }


}

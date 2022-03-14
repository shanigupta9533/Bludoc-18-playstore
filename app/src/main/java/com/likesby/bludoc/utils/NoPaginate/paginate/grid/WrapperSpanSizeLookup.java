package com.likesby.bludoc.utils.NoPaginate.paginate.grid;

import androidx.recyclerview.widget.GridLayoutManager;

import com.likesby.bludoc.utils.NoPaginate.item.BaseGridLayoutManagerItem;
import com.likesby.bludoc.utils.NoPaginate.paginate.WrapperAdapter;


public final class WrapperSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final GridLayoutManager.SpanSizeLookup wrappedSpanSizeLookup;
    private final BaseGridLayoutManagerItem loadingListItemSpanLookup;
    private final WrapperAdapter wrapperAdapter;

    public WrapperSpanSizeLookup(GridLayoutManager.SpanSizeLookup gridSpanSizeLookup,
                                 BaseGridLayoutManagerItem loadingListItemSpanLookup,
                                 WrapperAdapter wrapperAdapter) {
        this.wrappedSpanSizeLookup = gridSpanSizeLookup;
        this.loadingListItemSpanLookup = loadingListItemSpanLookup;
        this.wrapperAdapter = wrapperAdapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (wrapperAdapter.isLoadingItem(position) || wrapperAdapter.isErrorItem(position)) {
            return loadingListItemSpanLookup.getSpanSize();
        } else {
            return wrappedSpanSizeLookup.getSpanSize(position);
        }
    }

    public GridLayoutManager.SpanSizeLookup getWrappedSpanSizeLookup() {
        return wrappedSpanSizeLookup;
    }
}

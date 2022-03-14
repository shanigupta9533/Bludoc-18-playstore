package com.likesby.bludoc.utils.NoPaginate.callback;

public interface PaginateView {

    void showPaginateLoading(boolean show);

    void showPaginateError(boolean show);

    void setPaginateNoMoreData(boolean show);
}

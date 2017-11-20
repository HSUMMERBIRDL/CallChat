package com.hl.foundation.frame.ui.list;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * des:
 * Created by HL
 * on 2017/2/13 0013.
 */

// TODO: 2017/2/13 0013  

public abstract class BaseListModel<T> implements BaseListContract.Model<T> {

    public static final String START_PAGE = "page";
    public static final String PAGE_SIZE = "rows";

    protected int startPage;
    protected int pageSize;

    @Override
    public Observable<List<T>> getDatasList(Map<String, Object> parameters) {

        //  统一获取基本的参数
        startPage = (int) parameters.get(START_PAGE);
        pageSize = (int) parameters.get(PAGE_SIZE);

        return getList(parameters);
    }

    protected abstract Observable<List<T>> getList(Map<String, Object> parameters);

    @Override
    public Observable getOtherDatas() {
        return null;
    }
}

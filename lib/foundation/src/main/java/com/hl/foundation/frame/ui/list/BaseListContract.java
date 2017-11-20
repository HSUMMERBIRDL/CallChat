package com.hl.foundation.frame.ui.list;

import com.hl.foundation.frame.ui.BaseLoadingView;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * des:
 * Created by HL
 * on 2017/2/13 0013.
 */

public class BaseListContract {


    /**
     * @param <T> 返回的数据是json里面的数据结构定义
     */
    public interface Model<T> extends BaseModel {

        //        Observable<List<T>> getDatasList(int startPage,int pageSize);
        Observable<List<T>> getDatasList(Map<String, Object> parameters);

        Observable getOtherDatas();  // 该方法一定执行在 getDatasList之后

    }

    /**
     * @param <T> view里面的数据 是界面view真正需要的数据结构
     */
    public interface View<T> extends BaseLoadingView {

        //返回数据列表
        void returnDataListData(List<T> dataList);

        //返回顶部
        void scrolltoTop();

    }

    /**
     * Present 里面做两种数据结构的转化
     *
     * @param <V>
     * @param <M>
     */
    public static abstract class Presenter<M extends Model, V extends View> extends
            BasePresenter<M, V> {

        //发起获取数据请求
//        public abstract void sendGetDataListRequest(int startPage,int pageSize);
        public abstract void sendGetDataListRequest(Map<String, Object> parameters);
        public abstract void sendSearchDeviceListRequest(Map<String, Object> parameters);
    }
}

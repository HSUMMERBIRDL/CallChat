package com.hl.foundation.frame.ui.list;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hl.foundation.R;
import com.hl.foundation.frame.ui.base.BaseLoadingActivity;
import com.hl.foundation.library.utils.CollectionUtils;
import com.hl.foundation.library.utils.LogUtils;
import com.hl.foundation.library.utils.ResourcesUtils;
import com.hl.foundation.library.utils.StringUtils;
import com.hl.foundation.library.widget.CTopBar;
import com.hl.foundation.library.widget.LoadingTip;
import com.hl.foundation.library.widget.ireyclerview.IRecyclerView;
import com.hl.foundation.library.widget.ireyclerview.OnLoadMoreListener;
import com.hl.foundation.library.widget.ireyclerview.OnRefreshListener;
import com.hl.foundation.library.widget.ireyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.hl.foundation.library.widget.ireyclerview.widget.LoadMoreFooterView;
import com.hl.foundation.library.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * des:列表加载界面
 * Created by HL
 * on 2017/2/13 0013.
 *
 * @param <P>
 * @param <M>
 * @param <D> 界面真正需要的数据结构
 */

public abstract class BaseListActivity<M extends BaseListModel, P extends BaseListContract
        .Presenter, D>
        extends BaseLoadingActivity<M, P>
        implements BaseListContract.View<D>, OnRefreshListener, OnLoadMoreListener {

    private static int DEFAULT_EVERY_REQUET_DATE_SIZE = 10;  // 每次请求的数据条数

    protected IRecyclerView irc;
    LoadingTip loadedTip;
    CTopBar topbar;


    protected Map<String, Object> parameters = new HashMap<>();
    protected List<D> datas = new ArrayList<>();
    protected CommonRecycleViewAdapter adapter;
    protected int mStartPage = 1;
    private int everyPageSize = DEFAULT_EVERY_REQUET_DATE_SIZE;

    protected abstract CommonRecycleViewAdapter getAdapter();

    //  重写该方法  可定制一次请求返回的数据条数
    protected int getPageSize() {
        return DEFAULT_EVERY_REQUET_DATE_SIZE;
    }


    /**
     * 添加除 pageIndex pageSize 之外的参数  如果有的话
     *
     * @return
     */
    protected Map<String, Object> getAddParams() {
        return null;
    }

    protected boolean isNeedTitle() {
        return true;
    }

    protected String getTheTitle() {
        return "";
    }


    @Override
    protected void initDatas() {
        setParams();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initViews() {


        irc = (IRecyclerView) findViewById(R.id.irc);
        loadedTip = (LoadingTip) findViewById(R.id.loadedTip);
        topbar = (CTopBar) findViewById(R.id.topbar);
        initTopBar();

        adapter = getAdapter();
        irc.setLayoutManager(new LinearLayoutManager(mContext));
        datas.clear();
//        adapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(adapter);
//        irc.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//  头部尾部都加上了分割线  所以不要了

        if (isSetPullRefresh()) {
            irc.setOnRefreshListener(this);
            irc.setOnLoadMoreListener(this);
        } else {
            //禁止下拉上拉  只有列表功能
            irc.setLoadMoreEnabled(false);
            irc.setRefreshEnabled(false);
        }

        //数据为空才重新发起请求 一旦进来就在下拉onRefresh 里面执行请求发送
        if (isGetListRightNow()) {
            irc.setRefreshing(true);
        }
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, R.color.colorTheme);
    }

    protected boolean isSetPullRefresh() {
        return true;
    }

    protected boolean isGetListRightNow() {
        return true;
    }


    @Override
    protected void initEvents() {

    }

    /**
     * 初始化标题栏的一些东西
     */
    private void initTopBar() {

        if (isNeedTitle()) {
            topbar.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(getTheTitle())) {
                topbar.setTitle(getTheTitle());
            }
        } else {
            topbar.setVisibility(View.GONE);
        }

    }

    protected void sendRequest() {
        changeStarPageParmaters(mStartPage);
        mPresenter.sendGetDataListRequest(parameters);
    }


    @Override
    public void returnDataListData(List<D> dataList) {
        if (!CollectionUtils.isNullOrEmpty(dataList)) {
            mStartPage++;
            if (adapter.getPageBean().isRefresh()) {

                datas.clear();
                datas.addAll(dataList);

                irc.setRefreshing(false);
                adapter.replaceAll(datas);
                irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);  // 下拉的就不能让底部loadmoreview显示
            } else {
                if (dataList.size() > 0) {
                    datas.addAll(dataList);
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    adapter.addAll(datas);
                } else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END); //  没有数据了
                }
            }
        } else {  //  没有数据了
            LogUtils.logi(TAG, "没有数据");
//            irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);

            if (adapter.getPageBean().isRefresh()) {
                datas.clear();
                adapter.clear();
                showErrorTip("没有数据");
            } else {
                irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
            }
        }
    }

    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {

        LogUtils.logi(TAG, "-------onRefresh----------");
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        adapter.getPageBean().setRefresh(true);
        //发起请求
        mStartPage = 1;
        irc.setRefreshing(true);
        sendRequest();
    }


    @Override
    public void onLoadMore(View loadMoreView) {
        LogUtils.logi(TAG, "-------onLoadMore----------");

        if (CollectionUtils.isNullOrEmpty(datas)) {  // 没有数据的情况下 禁止上拉
            return;
        }

        adapter.getPageBean().setRefresh(false);
        //发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        sendRequest();
    }


    @Override
    public void showLoading(String title) {

//        if( adapter.getPageBean().isRefresh()) {
//            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
//        }
    }

    @Override
    public void stopLoading() {

        if (null != loadedTip) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        }

    }

    @Override
    public void showErrorTip(String msg) {

        if (adapter.getPageBean().isRefresh()) {

            if (CollectionUtils.isNullOrEmpty(datas)) { //  在页面有数据的时候不显示中间错误页面
                if ((StringUtils.equals(msg, ResourcesUtils.getResourceString(R.string.net_error)))
                        || StringUtils.equals(msg, ResourcesUtils.getResourceString(R.string.time_out))
                        || StringUtils.equals(msg, ResourcesUtils.getResourceString(R.string.no_net))) {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.netError);
                } else if (StringUtils.equals(msg, ResourcesUtils.getResourceString(R.string.no_data))) {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                } else if ((StringUtils.equals(msg, ResourcesUtils.getResourceString(R.string.unknow_error)))) {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.unknowError);
                }
            }
            irc.setRefreshing(false);
        } else {
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }


    // 改变起始页的参数
    private void changeStarPageParmaters(int startPage) {
        parameters.put(BaseListModel.START_PAGE, startPage);

    }


    /**
     * 重置page参数
     */
    protected void resetPageParameter() {

        mStartPage = 1;
        parameters.put(BaseListModel.START_PAGE, mStartPage);
    }

    private void setParams() {
        everyPageSize = getPageSize(); // 重置 pageSize

        parameters.put(BaseListModel.START_PAGE, mStartPage);
        parameters.put(BaseListModel.PAGE_SIZE, everyPageSize);

        // 如果有多余的参数 加入进去
        if (null != getAddParams()) {
            addParams(getAddParams());
        }
    }

    /**
     * 如果需要除pageIndex 和 pageSize之外的更多的参数 可以重写此方法加入(可进一步分装此方法  子类只需要提供key 和 value)
     *
     * @param params
     */
    private void addParams(Map<String, Object> params) {

        for (String key : params.keySet()) {
            this.parameters.put(key, params.get(key));
        }
    }

}

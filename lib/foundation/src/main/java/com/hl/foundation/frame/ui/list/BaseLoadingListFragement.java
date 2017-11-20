package com.hl.foundation.frame.ui.list;//package com.hl.foundation.frame.ui.list;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.View;
//
//import com.nesun.driver.R;
//import com.nesun.driver.app.widget.CTopBar;
//import com.nesun.driver.lib.commom.baseui.BaseFragment;
//import com.nesun.driver.lib.commom.utils.CollectionUtils;
//import com.nesun.driver.lib.commom.utils.LogUtils;
//import com.nesun.driver.lib.commom.utils.ResoucesUtils;
//import com.nesun.driver.lib.commom.utils.StringUtil;
//import com.nesun.driver.lib.commom.widget.LoadingTip;
//import com.nesun.driver.lib.ireyclerview.IRecyclerView;
//import com.nesun.driver.lib.ireyclerview.OnLoadMoreListener;
//import com.nesun.driver.lib.ireyclerview.OnRefreshListener;
//import com.nesun.driver.lib.ireyclerview.animation.ScaleInAnimation;
//import com.nesun.driver.lib.ireyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
//import com.nesun.driver.lib.ireyclerview.widget.LoadMoreFooterView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.Bind;
//
///**
// * des:
// * Created by HL
// * on 2017/2/15 0015.
// */
//
//public abstract class BaseLoadingListFragement<P extends BaseListPresenter, M extends BaseListModel,D>
//        extends BaseFragment<P,M>
//        implements BaseListContract.View<D>, OnRefreshListener, OnLoadMoreListener {
//
//    public static final String TAG = BaseLoadingListFragement.class.getSimpleName();
//
//    private static int DEFAULT_EVERY_REQUET_DATE_SIZE = 10;  // 每次请求的数据条数
//
//    @Bind(R.id.irc)
//    IRecyclerView irc;
//    @Bind(R.id.loadedTip)
//    LoadingTip loadedTip;
//    @Bind(R.id.topbar)
//    CTopBar topbar;
//
//    private Map<String,Object> parameters = new HashMap<>(); //  参数
//    protected List<D> datas = new ArrayList<>();
//    private CommonRecycleViewAdapter adapter;
//    private int mStartPage = 0;
//    private int everyPageSize = DEFAULT_EVERY_REQUET_DATE_SIZE;
//
//    protected abstract CommonRecycleViewAdapter getAdapter();
//
//    //  重写该方法  可定制一次请求返回的数据条数
//    protected int getPageSize() {
//        return DEFAULT_EVERY_REQUET_DATE_SIZE;
//    }
//    protected boolean isNeedTitle() {
//        return true;
//    }
//    /**
//     * 添加除 pageIndex pageSize 之外的参数  如果有的话
//     * @return
//     */
//    protected Map<String,Object> getAddParams() {
//        return null;
//    }
//
//    protected String getTheTitle() {
//        return "";
//    }
//
//
//
//    @Override
//    protected int getLayoutResource() {
//        return  R.layout.activity_base_list;
//    }
//
//    @Override
//    protected void initDatas() {
//        super.initDatas();
//
//        setParams();
//    }
//
//    @Override
//    protected void initView() {
//
//        initTopBar();
//
//        adapter = getAdapter();
//        irc.setLayoutManager(new LinearLayoutManager(mContext));
//        datas.clear();
//        adapter.openLoadAnimation(new ScaleInAnimation());
//        irc.setAdapter(adapter);
////        irc.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
//        irc.setOnRefreshListener(this);
//        irc.setOnLoadMoreListener(this);
//
//        irc.setRefreshing(true);
//    }
//
//    @Override
//    public void returnDataListData(List<D> dataList) {
//        LogUtils.logi(TAG,"-------returnDataListData-----1-----");
//        if (!CollectionUtils.isNullOrEmpty(dataList)) {
//            LogUtils.logi("dataList","dataList:" + dataList);
//            mStartPage++;
//            LogUtils.logi(TAG,"-------returnDataListData-----2-----");
//            if (adapter.getPageBean().isRefresh()) {
//                LogUtils.logi(TAG,"-------returnDataListData-----3-----");
//                LogUtils.logd(TAG + "irc是否为空: " + (irc == null));
//                irc.setRefreshing(false);
//                adapter.replaceAll(dataList);
//            } else {
//                LogUtils.logi(TAG,"-------returnDataListData------4----");
//                if (dataList.size() > 0) {
//                    LogUtils.logi(TAG,"-------returnDataListData---5-------");
//                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
//                    adapter.addAll(dataList);
//                } else {
//                    LogUtils.logi(TAG,"-------returnDataListData----6------");
//                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END); //  没有数据了
//                }
//            }
//        }else{  //  没有数据了
//            LogUtils.logi(TAG,"没有数据");
////            irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
//
//            if (adapter.getPageBean().isRefresh()) {
//                showErrorTip("没有数据");
//            }else {
//                irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
//            }
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
//        adapter.getPageBean().setRefresh(true);
//        mStartPage = 0;
//        irc.setRefreshing(true);
//        sendRequest();
//    }
//
//    @Override
//    public void onLoadMore(View loadMoreView) {
//
//        adapter.getPageBean().setRefresh(false);
//        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
//        sendRequest();
//    }
//
//    @Override
//    public void showLoading(String title) {
////        if( adapter.getPageBean().isRefresh()) {
////            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
////        }
//    }
//
//    @Override
//    public void stopLoading() {
//        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
//    }
//
//    @Override
//    public void showErrorTip(String msg) {
//        if( adapter.getPageBean().isRefresh()) {
//            loadedTip.setTips(msg);
//            if((StringUtil.equals(msg, ResoucesUtils.getResourceString(R.string.net_error)))
//                    || StringUtil.equals(msg, ResoucesUtils.getResourceString(R.string.time_out))){
//                loadedTip.setLoadingTip(LoadingTip.LoadStatus.netError);
//            }else if(StringUtil.equals(msg, ResoucesUtils.getResourceString(R.string.no_data))){
//                loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
//            }else{
//                loadedTip.setLoadingTip(LoadingTip.LoadStatus.unknowError);
//            }
//            irc.setRefreshing(false);
//        }else{
//            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
//        }
//    }
//
//
//    @Override
//    public void scrolltoTop() {
//        irc.smoothScrollToPosition(0);
//    }
//
//    /**
//     * 发送请求  发送之前要重先设置开始页
//     */
//    private void sendRequest(){
//        changeStarPageParmaters(mStartPage);
//        mPresenter.sendGetDataListRequest(parameters);
//    }
//
//
//    // 改变起始页的参数
//    private void changeStarPageParmaters(int startPage){
//        parameters.put(BaseListModel.START_PAGE,startPage);
//    }
//
//    private void setParams() {
//        everyPageSize = getPageSize(); // 重置 pageSize
//
//        parameters.put(BaseListModel.START_PAGE,mStartPage);
//        parameters.put(BaseListModel.PAGE_SIZE,everyPageSize);
//
//        // 如果有多余的参数 加入进去
//        if(null != getAddParams()){
//            addParams(getAddParams());
//        }
//
//    }
//
//    /**
//     * 如果需要除pageIndex 和 pageSize之外的更多的参数 可以重写此方法加入(可进一步分装此方法  子类只需要提供key 和 value)
//     * @param params
//     */
//    private  void addParams(Map<String,Object> params) {
//
//        for (String key : params.keySet()) {
//            this.parameters.put(key, params.get(key));
//        }
//    }
//
//    /**
//     * 初始化标题栏的一些东西
//     */
//    private void initTopBar() {
//
//        if(isNeedTitle()){
//            topbar.setVisibility(View.VISIBLE);
//            if(StringUtil.isNotEmpty(getTheTitle())){
//                topbar.setTitle(getTheTitle());
//            }
//        }else{
//            topbar.setVisibility(View.GONE);
//        }
//
//    }
//
//}

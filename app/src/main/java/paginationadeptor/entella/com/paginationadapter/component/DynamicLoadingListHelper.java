package paginationadeptor.entella.com.paginationadapter.component;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by Kunal.Mahajan on 6/25/2018.
 */

public abstract class DynamicLoadingListHelper {

    private final PaginationAdapter adapter;
    private final Context context;
    private int requestLimit;
    private LinearLayout containerLayout;
    private RecyclerView rv;
    private int totalRecords = -1;

    /**
     * @param context
     * @param containerLayout container layout may be any layout, linear, relative etc.
     * @param adapter         reference for the adapter of your class which extends  PaginationAdapter
     * @param requestLimit    number of elements you want to request through server
     */
    public DynamicLoadingListHelper(Context context, LinearLayout containerLayout, PaginationAdapter adapter, int requestLimit) {
        this.adapter = adapter;
        this.context = context;
        this.containerLayout = containerLayout;
        this.requestLimit = requestLimit;
        init();
    }

    public void init() {
        final LinearLayoutManager lm = new LinearLayoutManager(context, adapter.isVertical() ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL, false);
        rv = new RecyclerView(context);
        rv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        containerLayout.addView(rv);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastLoadCountReq = Integer.MIN_VALUE;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lm.findLastVisibleItemPosition() == adapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemLoadedCount() < totalRecords && adapter.getItemLoadedCount() > lastLoadCountReq) {
                    lastLoadCountReq = adapter.getItemLoadedCount();
                    loadData(adapter.getItemLoadedCount(), requestLimit);
                }
            }
        });

        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();

            @Override
            public void onChildViewAttachedToWindow(View view) {

                boolean isScrolled = layoutManager.findViewByPosition(0) != layoutManager.findViewByPosition(layoutManager.findFirstCompletelyVisibleItemPosition());
                if (isScrolled)
                    return;

                if (!(view instanceof ProgressBar))
                    return;

                if (isRecyclerScrollable()) {
                    loadData(adapter.getItemLoadedCount(), requestLimit);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });

        loadData(0, requestLimit);
    }

    private boolean isRecyclerScrollable() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        RecyclerView.Adapter adapter = rv.getAdapter();

        if (layoutManager == null || adapter == null) return false;

        return layoutManager.findLastCompletelyVisibleItemPosition() < totalRecords;
    }

    /**
     * implement this method, it will get called when scroll reached to ProgressView i.e. loading view
     *
     * @param offset - starting index
     * @param limit  - how many data need to download based upon the
     */
    protected abstract void loadData(int offset, int limit);

    /**
     * Call this method once the data is loaded @requestLimit you passed in constructor.
     *
     * @param list         an array list of elements  which is recentely loaded and need to add in RecyclerView
     * @param totalRecords what are the total numbers of records available, based upon this variable it will automatically call loadData(int offset, int limit);
     */
    public void dataLoaded(List list, int totalRecords) {
        this.totalRecords = totalRecords;
        adapter.add(list);
        if (adapter.getItemLoadedCount() >= totalRecords)
            adapter.setLoadingFinished();
    }
}

package paginationadeptor.entella.com.paginationadeptor.component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal.Mahajan on 6/25/2018.
 */

public abstract class PaginationAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int ITEM = 3;
    private static final int LOADING = 4;
    private static final int NO_DATA = 5;
    private final boolean isVertical;

    protected List records;
    protected Context context;
    private LoadingObj loadingObj = new LoadingObj();
    private NoDataAvailableObj noDataAvailableObj = new NoDataAvailableObj();

    /**
     * @param context
     * @param isVertical true for if you need Vertical RecyclerView, false for horizontal RecyclerView
     */
    public PaginationAdapter(Context context, boolean isVertical) {
        this.isVertical = isVertical;
        this.context = context;
        records = new ArrayList<>();
        records.add(loadingObj);
    }

    private int getIntToDP(int px) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                viewHolder = new PaginationAdapter.LoadingVH(getProgressBar());
                break;
            case NO_DATA:
                viewHolder = new ViewHolder(getNoDataTextView()) {
                };
        }
        return viewHolder;
    }

    protected abstract String getEmptyText();

    @NonNull
    protected abstract ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM)
            setValuesOnBind(holder, position);
    }

    /**
     * implement this method to bind the view and manage the UI Values
     *
     * @param holder
     * @param position
     */
    protected abstract void setValuesOnBind(ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        Object o = records.get(position);
        if (o instanceof LoadingObj)
            return LOADING;

        if (o instanceof NoDataAvailableObj)
            return NO_DATA;

        return ITEM;
    }

    void setLoadingFinished() {
        records.remove(records.size() - 1);
        if (records.size() == 0) records.add(new NoDataAvailableObj());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    int getItemLoadedCount() {
        int l = records.size();
        if (records.contains(loadingObj))
            l--;

        if (records.contains(noDataAvailableObj))
            l--;

        return l;
    }

    void add(List list) {
        for (int i = 0; i < list.size(); i++) {
            records.add(records.size() - 1, list.get(i));
        }

        notifyDataSetChanged();
    }

    boolean isVertical() {
        return isVertical;
    }

    private ProgressBar getProgressBar() {
        ProgressBar progressBar = new ProgressBar(context);
        int h = isVertical ? getIntToDP(30) : ViewGroup.LayoutParams.MATCH_PARENT;
        int w = isVertical ? ViewGroup.LayoutParams.MATCH_PARENT : getIntToDP(30);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(w, h);
        int margin = getIntToDP(5);
        progressParams.setMargins(margin, margin, margin, margin);
        progressParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(progressParams);
        return progressBar;
    }

    private TextView getNoDataTextView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(context);
        textView.setText(getEmptyText());
        textView.setTypeface(null, Typeface.ITALIC);
        params.setMargins(0, getIntToDP(7), 0, getIntToDP(7));
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private static class LoadingObj {
    }

    private class LoadingVH extends ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    private class NoDataAvailableObj {
    }
}

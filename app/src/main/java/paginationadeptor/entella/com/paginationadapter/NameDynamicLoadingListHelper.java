package paginationadeptor.entella.com.paginationadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import paginationadeptor.entella.com.paginationadapter.component.PaginationAdapter;

/**
 * Created by Kunal.Mahajan on 7/6/2018.
 */

public class NameDynamicLoadingListHelper extends PaginationAdapter {

    public NameDynamicLoadingListHelper(Context applicationContext) {
        super(applicationContext, false);
    }

    @Override
    protected String getEmptyText() {
        return "No data available";
    }


    @NonNull
    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.layout_name, parent, false);
        final RecyclerView.ViewHolder viewHolder = new ShortJobInfoVH(v);
        return viewHolder;
    }

    @Override
    protected void setValuesOnBind(RecyclerView.ViewHolder holder, int position) {
        String r = (String) records.get(position);
        ShortJobInfoVH rvh = (ShortJobInfoVH) holder;
        rvh.tvRate.setText(r);
    }

    private class ShortJobInfoVH extends RecyclerView.ViewHolder {

        TextView tvRate;

        public ShortJobInfoVH(View jobView) {
            super(jobView);
            tvRate = ((TextView) jobView.findViewById(R.id.layout_home_job_tv_title));
        }
    }
}

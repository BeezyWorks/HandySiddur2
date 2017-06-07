package com.mattaniahbeezy.handysiddur.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattaniahbeezy.handysiddur.R;
import com.mattaniahbeezy.handysiddur.utilities.DateFormatter;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.Zman;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.ZmanimCalculator;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Beezy Works Studios on 6/2/2017.
 */

public class ZmanRecyclerAdapter extends RecyclerView.Adapter<ZmanRecyclerAdapter.ZmanViewHolder> {
    private ArrayList<Zman> zmanim = new ArrayList<>();

    private final int NormalZman = 0;
    private final int SpecialZman = 1;

    public ZmanRecyclerAdapter() {
        for (Zman zman : Zman.values()) {
            if (zman.shouldShow(null)) {
                zmanim.add(zman);
            }
        }
    }

    @Override
    public ZmanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case NormalZman:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zman_list_item, parent, false);
                break;
            case SpecialZman:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_zman_list_item, parent, false);
                break;
        }

        return new ZmanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ZmanViewHolder holder, int position) {
        Zman zman = zmanim.get(position);
        holder.setZmanTitle(zman.hebrewName());
        Date zmanTime = ZmanimCalculator.getInstance().getZmanTime(zman);
        String displayTime = zmanTime == null ?
                "Loading..." :
                DateFormatter.getInstance().formatTime(zmanTime);
        holder.setZmanTime(displayTime);
    }

    @Override
    public int getItemViewType(int position) {
        return zmanim.get(position).isSpecial() ? SpecialZman : NormalZman;
    }

    @Override
    public int getItemCount() {
        return zmanim.size();
    }

    class ZmanViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timeTextView;

        ZmanViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.zmanTitle);
            timeTextView = (TextView) itemView.findViewById(R.id.zmanTime);
        }

        void setZmanTitle(String title) {
            titleTextView.setText(title);
        }

        void setZmanTime(String time) {
            timeTextView.setText(time);
        }
    }

    class SpecialZmanHolder extends ZmanViewHolder {
        SpecialZmanHolder(View itemView) {
            super(itemView);
        }
    }
}

package com.mattaniahbeezy.handysiddur.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattaniahbeezy.handysiddur.R;
import com.mattaniahbeezy.handysiddur.activities.DaveningActivity;
import com.mattaniahbeezy.handysiddur.activities.ZmanimActivity;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class NavigationRecyclerAdapter extends RecyclerView.Adapter<NavigationRecyclerAdapter.NavigationViewHolder> {

    ShabbosAlarmStart shabbosAlarmStart;

    public void setShabbosAlarmStart(ShabbosAlarmStart shabbosAlarmStart) {
        this.shabbosAlarmStart = shabbosAlarmStart;
    }

    enum NavigationItem {
        ZMANIM, BENTCHING, MEIN_SHALOSH, START_SHABBOS_ALARM;

        String getHebrewTitle() {
            switch (this) {

                case ZMANIM:
                    return "זמני היום";
                case BENTCHING:
                    return "ברכת המזון";
                case MEIN_SHALOSH:
                    return "מעין שלוש";
                case START_SHABBOS_ALARM:
                    return "Shabbos Mode";
            }
            return null;
        }

        Intent getIntent(Context context) {
            Intent daveningIntent = getDaveningActivityIntent(context);
            switch (this) {

                case ZMANIM:
                    return new Intent(context, ZmanimActivity.class);
                case BENTCHING:
                    daveningIntent.putExtra(DaveningActivity.DAVENING_KEY, "bentching");
                    return daveningIntent;
                case MEIN_SHALOSH:
                    daveningIntent.putExtra(DaveningActivity.DAVENING_KEY, "mein_shalosh");
                    return daveningIntent;
            }
            return null;
        }

        private Intent getDaveningActivityIntent(Context context) {
            return new Intent(context, DaveningActivity.class);
        }
    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavigationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, int position) {
        holder.setNavigationItem(NavigationItem.values()[position]);
    }

    @Override
    public int getItemCount() {
        return NavigationItem.values().length;
    }

    class NavigationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private NavigationItem navigationItem;

        private NavigationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        }

        void setNavigationItem(NavigationItem navigationItem) {
            this.navigationItem = navigationItem;
            titleTextView.setText(navigationItem.getHebrewTitle());
        }

        @Override
        public void onClick(View view) {
            switch (navigationItem) {

                case ZMANIM:
                    view.getContext().startActivity(navigationItem.getIntent(view.getContext()));
                    break;
                case BENTCHING:
                    view.getContext().startActivity(navigationItem.getIntent(view.getContext()));
                    break;
                case MEIN_SHALOSH:
                    view.getContext().startActivity(navigationItem.getIntent(view.getContext()));
                    break;
                case START_SHABBOS_ALARM:
                    if (shabbosAlarmStart != null) {
                        shabbosAlarmStart.startShabbosAlarm();
                    }
                    break;
            }

        }
    }

    public interface ShabbosAlarmStart {
        void startShabbosAlarm();
    }
}

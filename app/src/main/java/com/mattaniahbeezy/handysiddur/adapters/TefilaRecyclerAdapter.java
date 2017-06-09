package com.mattaniahbeezy.handysiddur.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattaniahbeezy.handysiddur.R;

import java.util.List;

import firebaseconnector.database.TefilaAssembler;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Translation;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */


public class TefilaRecyclerAdapter extends RecyclerView.Adapter<TefilaRecyclerAdapter.TefilaViewHolder> implements TefilaAssembler.TefilaCallback {

    List<TefilaAssembler.ResolvedSection> data;

    public TefilaRecyclerAdapter(String tefilaKey) {
        TefilaAssembler assembler = new TefilaAssembler(Nusach.ASHKENAZ, this, null);
        assembler.assemble(tefilaKey);
    }

    @Override
    public TefilaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TefilaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tefila_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(TefilaViewHolder holder, int position) {
        holder.setText(data.get(position).getResolvedTranslations().get(Translation.HEBREW));
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    @Override
    public void tefilaReady(List<TefilaAssembler.ResolvedSection> resolvedSections) {
        data = resolvedSections;
        notifyDataSetChanged();
    }

    class TefilaViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public TefilaViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/frank.ttf");
            mTextView.setTypeface(type);
        }

        public void setText(Spanned span) {
            mTextView.setText(span);
        }
    }
}

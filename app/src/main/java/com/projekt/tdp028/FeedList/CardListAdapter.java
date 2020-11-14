package com.projekt.tdp028.FeedList;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.projekt.tdp028.models.firebase.PollOverview;

import java.util.ArrayList;
import java.util.List;


public class CardListAdapter<T extends CardItem> extends RecyclerView.Adapter<CardListAdapter.CardListViewHolder> {
    private List<PollOverview> pollOverviews = new ArrayList<PollOverview>();
    private OnCardSelectListener cardSelectListener;

    Class<T> classname;
    public CardListAdapter(OnCardSelectListener listener, Class<T> classname) {
        this.cardSelectListener = listener;
        this.classname = classname;
    }

    private <D> D getInstance(Class<D> _class, Context context)
    {
        try
        {
            return _class.getDeclaredConstructor(Context.class).newInstance(context);
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();
        }
        return null;
    }

    @Override
    public CardListAdapter.CardListViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context context = parent.getContext();
        CardItem card =  getInstance(classname, context);
        return new CardListViewHolder(card, cardSelectListener);
    }

    @Override
    public void onBindViewHolder(CardListViewHolder viewHolder, int i) {
        viewHolder.item.setData(pollOverviews.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return pollOverviews.size();
    }

    public void updateWithData(List<PollOverview> data) {
        this.pollOverviews = data;
    }

    public static class CardListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardItem item;
        private OnCardSelectListener cardSelectListener;

        public CardListViewHolder(CardItem item, OnCardSelectListener listener) {
            super(item);
            this.item = item;

            this.cardSelectListener = listener;
            item.setLongClickable(true);
            item.setOnClickListener(this);
            item.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            cardSelectListener.onCardSelect(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            cardSelectListener.onLocationSelect(getAdapterPosition());
            return true;
        }
    }

    public interface OnCardSelectListener {
        public void onCardSelect(int poisition);
        void onLocationSelect(int adapterPosition);
    }

}

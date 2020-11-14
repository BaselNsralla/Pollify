package com.projekt.tdp028.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projekt.tdp028.FeedList.CardListAdapter;
import com.projekt.tdp028.FeedList.FeedCardItem;
import com.projekt.tdp028.Interfaces.OnPollCallListener;
import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PollOverview;
import com.projekt.tdp028.viewmodels.FeedViewModel;
import com.projekt.tdp028.viewmodels.ProfileViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements ProfileViewModel.OnDataUpdateListener, CardListAdapter.OnCardSelectListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String NAME = "FEED_FRAGMENT";

    private FeedViewModel feedViewModel;
    private RecyclerView recyclerView;
    private CardListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPollCallListener onPollCallListener;
    private OnFragmentInteractionListener onInteractionListener;

    //private Firebase dataFetcher = new Firebase(this);


    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        feedViewModel = new FeedViewModel(this, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_feed, container, false);
        view.setHapticFeedbackEnabled(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInteractionListener.onCreatePollClicked();
            }
        });

        listAdapter = new CardListAdapter<FeedCardItem>(this, FeedCardItem.class);
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) { // TODO: andra implementation bör kollas
            onInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if(context instanceof OnPollCallListener) {
            onPollCallListener = (OnPollCallListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPollCallListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPollCallListener = null;
        onInteractionListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        feedViewModel.fetchData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_earth_image_item) {
            feedViewModel.fetchData();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCardSelect(int poisition) {
        this.onPollCallListener.onPollDetailCall(feedViewModel.getPollId(poisition)); // this or

    }

    @Override
    public void onLocationSelect(int adapterPosition) {
        getView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        this.onPollCallListener.onPollLocationCall(feedViewModel.getPollId(adapterPosition));
    }


    @Override
    public void onPollOverviewDataReceived(List<PollOverview> data) {
        listAdapter.updateWithData(data);
        listAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onCreatePollClicked();
    }

}

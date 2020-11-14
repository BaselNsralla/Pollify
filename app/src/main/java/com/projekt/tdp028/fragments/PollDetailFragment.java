package com.projekt.tdp028.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PollDetail;
import com.projekt.tdp028.viewmodels.PollDialogViewModel;
import com.projekt.tdp028.views.PollOptionRadioView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PollDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PollDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PollDetailFragment extends FullScreenBottomSheetDialogFragment implements PollDialogViewModel.OnDataUpdateListener, PollOptionRadioView.OnPollOptionRadioViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POLL_ID = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private PollDialogViewModel pollDialogViewModel;
    private PollOptionRadioView pollOptionRadioView;

    public PollDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pollId Parameter 1.
     * @return A new instance of fragment PollDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PollDetailFragment newInstance(String pollId) {
        PollDetailFragment fragment = new PollDetailFragment();
        Bundle args = new Bundle();
        args.putString(POLL_ID, pollId); // TODO: gör saker här ned modelview
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(POLL_ID); // TODO: istället för det här ta en viewModel som åker injectas
        }

        SharedPreferences sp = getActivity().getSharedPreferences("Login", MODE_PRIVATE); // TODO: kanske skicka med userid som parametr
        String userId        = sp.getString("USER_ID", null);
        pollDialogViewModel  = new PollDialogViewModel(mParam1, userId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poll_detail, container, false);
        pollOptionRadioView = view.findViewById(R.id.poll_option_radio_group);
        ImageView imageView = view.findViewById(R.id.poll_dialog_pull_down_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pollDialogViewModel.fetchData();
    }

    // TODO: Rename method, fetchData argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            // TODO: Fixa det här;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void didReceivePostData(PollDetail pollDetail) {
        View view = this.getView();;
        TextView textView = (TextView) view.findViewById(R.id.poll_dialog_text); // TODO: Fix null
        textView.setText(pollDetail.getText());

        pollOptionRadioView.onPollOptionListener = this;
        pollOptionRadioView.setPollOptions(pollDetail.getPollOptions());
        pollDialogViewModel.findUserChoice();
    }

    @Override
    public void onSetChoiceCompleted() {
        Log.d("LOADING FINISHED ", "Implement me");
        Toast.makeText(getContext(), "CHOICE WAS MADE", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserChoiceFound(@Nullable String pollOptionId) {
        if(pollOptionId == null) {
            pollOptionRadioView.activate();
        } else {
            pollOptionRadioView.setPick(pollOptionId);
        }
    }

    @Override
    public void onPollOptionClicked(String optionId) {
        this.pollDialogViewModel.setUserChoice(optionId);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

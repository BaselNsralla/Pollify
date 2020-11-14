package com.projekt.tdp028.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PLatLng;
import com.projekt.tdp028.viewmodels.PublishViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 */
public class PublishFragment extends Fragment implements View.OnClickListener, PublishViewModel.OnPublishPollViewModelListener {

    private String pollText;
    private List<String> pollOptionsText;
    private PLatLng latlng;
    private PublishViewModel viewModel;


    public PublishFragment() {}

    public static PublishFragment newInstance(Bundle bundle) {
        PublishFragment publishFragment =  new PublishFragment();
        publishFragment.pollText = bundle.getString("pollText");
        publishFragment.pollOptionsText = (ArrayList<String>)bundle.getSerializable("pollOptionsText");
        publishFragment.latlng = (PLatLng) bundle.getSerializable("latlng");
        return publishFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        Button publishBtn = view.findViewById(R.id.publish_poll_button);
        publishBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new PublishViewModel(context, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onClick(View v) {
        viewModel.publishPoll(pollText, pollOptionsText, latlng);
    }

    @Override
    public void done() {
        Toast.makeText(getContext(), "Publishing done!", Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
    }

    ScaleGestureDetector mScaleDetector;


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
/*    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPublishPoll();
    }*/
}

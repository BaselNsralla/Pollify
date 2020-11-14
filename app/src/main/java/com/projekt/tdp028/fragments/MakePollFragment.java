package com.projekt.tdp028.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PLatLng;
import com.projekt.tdp028.viewmodels.MakePollViewModel;
import com.projekt.tdp028.views.PollOptionsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakePollFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakePollFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    MakePollViewModel viewModel;
    public static String NAME = "CREATE_POLL_FRAGMENT";
    public static int MAX_TEXT_LENGTH = 20;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermission = false;
    private EditText pollDescEdit;
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();


    OnFragmentInteractionListener fragmentInteractionListener;

    public MakePollFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MakePollFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakePollFragment newInstance() {
        MakePollFragment fragment = new MakePollFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new MakePollViewModel(getActivity().getApplication());


        // REMOTE CONFIG
        remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("MAX_LENGTH", MAX_TEXT_LENGTH);
        remoteConfig.setDefaultsAsync(defaults);
        remoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) { setMaximuLength(); }
        });


        if (getArguments() != null) { }
        setHasOptionsMenu(true);
    }


    @Override
    public void onDestroyView() {
        hideKeyboard();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        hideKeyboard();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_make_poll, container, false);
        view.setHapticFeedbackEnabled(true);

        setupToolbar(view);


        pollDescEdit = (EditText) view.findViewById(R.id.poll_description_edit);
        pollDescEdit.requestFocus();
        //pollDescEdit.addTextChangedListener(this);
        setMaximuLength();




        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(pollDescEdit, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return view;
    }

    private void setMaximuLength() {
        if(pollDescEdit == null) {return;}
        int maxTextLength = (int) remoteConfig.getLong("MAX_LENGTH");
        pollDescEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxTextLength)});
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.make_poll_toolbar);
        toolbar.inflateMenu(R.menu.make_poll_toolbar_menu);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_left_thick));
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                getFragmentManager().popBackStack();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            this.fragmentInteractionListener = (OnFragmentInteractionListener) context;

        } else {
            throw new Error("BOOM");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermission = false;
        } else {
            locationPermission = true;
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(R.id.publish_action_item == item.getItemId()) {
            View view = getView();

            String pollText = ((EditText) view.findViewById(R.id.poll_description_edit)).getText().toString();
            List<String> pollOptionsText = ((PollOptionsView) view.findViewById(R.id.poll_options_container)).getOptionsTexts();// getOptionsTexts(view);
            hideKeyboard();


            findLocation(latlng ->  fragmentInteractionListener.onPublish(pollText, pollOptionsText, latlng)); // TODO: IF LOCATION IS ALLOWED
        }
        return false;
    }


    private void makePoll(String pollText, List<String> pollOptionsText, LatLng latlng) {
        viewModel.createPoll(pollText, pollOptionsText, latlng, new MakePollViewModel.OnMakePollViewModelListener() {
            @Override
            public void done() {
                getView().performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                Toast.makeText(getContext(), "PUBLISHING DONE", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findLocation(LocationReceivedListener listener) {
        if (!locationPermission) {listener.onLatLngReceived(null); return;}
        fusedLocationClient.getLastLocation().addOnSuccessListener(this.getActivity(),  new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d("LOCATION", "GOT LOCATION");
                    // Logic to handle location object
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    LatLng latLng = new LatLng(lat, lng);
                    listener.onLatLngReceived(new PLatLng(latLng.latitude, latLng.longitude));
                } else {
                    listener.onLatLngReceived(null);
                }

            }
        });
    }


    private List<String> getOptionsTexts(View view) {
        List<String> list = new ArrayList<String>();

        for(int i = 1; i < 5; ++i) {
            int id = getResources().getIdentifier("poll_option"+i, "id", getContext().getPackageName());
            if (id == 0) { break; }
            String str = ((EditText)view.findViewById(id)).getText().toString();
            list.add(str);
        }

        return list;
    }


    private void hideKeyboard() {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    public interface OnFragmentInteractionListener {
        void onPublish(String pollText, List<String> pollOptionsText, PLatLng latlng);
    }

    interface LocationReceivedListener {
        void onLatLngReceived(PLatLng latlng);
    }

}

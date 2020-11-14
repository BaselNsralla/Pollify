package com.projekt.tdp028.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.io.ByteStreams;
import com.google.firebase.firestore.util.FileUtil;
import com.projekt.tdp028.FeedList.CardListAdapter;
import com.projekt.tdp028.FeedList.OwnerCardItem;
import com.projekt.tdp028.Interfaces.OnPollCallListener;
import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PollOverview;
import com.projekt.tdp028.utility.LocalStore;
import com.projekt.tdp028.viewmodels.PollViewModel;
import com.projekt.tdp028.viewmodels.ProfileViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements CardListAdapter.OnCardSelectListener, PollViewModel.OnDataUpdateListener, ProfileViewModel.OnProfileDataListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CardListAdapter listAdapter;
    private ProfileViewModel profileViewModel;
    private OnPollCallListener mListener;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        profileViewModel = new ProfileViewModel(this, this,getContext());
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        view.setHapticFeedbackEnabled(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.created_polls_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        listAdapter = new CardListAdapter<OwnerCardItem>(this, OwnerCardItem.class);
        recyclerView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPollCallListener && context instanceof OnFragmentInteractionListener) { // TODO: andra implementation bör kollas
            mListener = (OnPollCallListener) context;
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        profileViewModel.fetchData(LocalStore.getSavedUserId(getContext()));
    }

    @Override
    public void onCardSelect(int poisition) {
        getView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        this.mListener.onPollDetailCall(profileViewModel.getPollId(poisition));
    }

    @Override
    public void onLocationSelect(int adapterPosition) { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_pick_image) {
            onFragmentInteractionListener.pickImage();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPollOverviewDataReceived(List<PollOverview> data) {
        listAdapter.updateWithData(data);
        listAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    public void onImageReceived(String imageUrlString) {
        View view = getView();

        if (view == null || imageUrlString == null) { return; }

        ImageView imageView = view.findViewById(R.id.profile_image);

        Uri imageUri = Uri.parse(imageUrlString);
        String realPath = "";

        String wholeID = DocumentsContract.getDocumentId(imageUri);
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);
        int columnIndex = 0;
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                realPath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(realPath);
        imageView.setImageBitmap(bitmap);
    }


    public void setNewProfileImage(Uri uri) {
        profileViewModel.updateImage(uri, LocalStore.getSavedUserId(getContext()));
        ImageView imageView = getView().findViewById(R.id.profile_image);
        imageView.setImageURI(uri);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onPollDetail(String pollId);
        void pickImage();
        void pickSetReadyImage(Uri imageUri);
    }
}

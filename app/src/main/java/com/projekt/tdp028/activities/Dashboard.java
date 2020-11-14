package com.projekt.tdp028.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.projekt.tdp028.Interfaces.OnPollCallListener;
import com.projekt.tdp028.fragments.MakePollFragment;
import com.projekt.tdp028.fragments.PollDetailFragment;
import com.projekt.tdp028.R;
import com.projekt.tdp028.fragments.FeedFragment;
import com.projekt.tdp028.fragments.ProfileFragment;
import com.projekt.tdp028.fragments.PublishFragment;
import com.projekt.tdp028.fragments.SettingsFragment;
import com.projekt.tdp028.models.Post;
import com.projekt.tdp028.models.firebase.PLatLng;
import com.projekt.tdp028.utility.LocalStore;
import com.projekt.tdp028.viewmodels.DashboardViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Dashboard extends BaseActivity implements DashboardViewModel.OnDataUpdateListener, FeedFragment.OnFragmentInteractionListener, SettingsFragment.OnSettingsInteractionListener, MakePollFragment.OnFragmentInteractionListener, OnPollCallListener, ProfileFragment.OnFragmentInteractionListener {

    String POLL_DETAIL_STACK_ID = "poll_detail_stack";
    String CURRENT_FRAGMENT_TAB = "current_tab";
    public static final int PICK_IMAGE = 1;
    BottomNavigationView bottomNavigationView;
    ActionBar actionBar;
    HashMap<String, Fragment> fragmentHashMap = new HashMap<>();
    Toolbar toolbar;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fragmentHashMap.put("FEED",     null);
        fragmentHashMap.put("PROFILE",  null);
        fragmentHashMap.put("SETTINGS", null);

        toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.getItemId()  == bottomNavigationView.getSelectedItemId()) {
                    return false;
                }
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        toolbar.setTitle(R.string.profile_title);
                        ProfileFragment profileFragment = (ProfileFragment) fragmentHashMap.get("PROFILE");
                        if( profileFragment == null) {
                            profileFragment = new ProfileFragment();
                            fragmentHashMap.put("PROFILE", profileFragment);
                        }
                        FragmentTransaction profileFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        profileFragmentTransaction.replace(R.id.fragment_holder, profileFragment).commit();
                        break;
                    case R.id.action_feed:
                        toolbar.setTitle(R.string.feed_title);
                        actionBar.show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FeedFragment feedFragment = new FeedFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_holder, feedFragment).commit();
                        break;
                    case R.id.action_settings:
                        toolbar.setTitle(R.string.settings_title);
                        actionBar.show();
                        SettingsFragment settingsFragment = new SettingsFragment();
                        FragmentManager afragmentManager = getSupportFragmentManager();
                        FragmentTransaction afragmentTransaction = afragmentManager.beginTransaction();
                        afragmentTransaction.replace(R.id.fragment_holder, settingsFragment, SettingsFragment.DEFAULT_TAG).commit();
                        break;
                }
                return true;
            }
        });
        setDefaultNavigationItem(savedInstanceState);
        prepareNotifications();
        Bundle b = getIntent().getExtras();
        String str = b.getString("name");

    }

    private void prepareNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("poll");
    }

    private void setDefaultNavigationItem(@Nullable Bundle savedInstanceState) {
        int currentId = savedInstanceState == null ? 0 : savedInstanceState.getInt(CURRENT_FRAGMENT_TAB);
        int selectionId = currentId == 0 ? R.id.action_feed : currentId;
        bottomNavigationView.setSelectedItemId(selectionId); // TODO: HERE IS A HACK
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int fragId = bottomNavigationView.getSelectedItemId();
        outState.putInt(CURRENT_FRAGMENT_TAB, fragId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            super.onBackPressed();
            Toast.makeText(this, "OnBackPressed Works", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }


    @Override
    public void didReceivePostData(List<Post> data) { }

    @Override
    public void onCreatePollClicked() {
        MakePollFragment makePollFragment = MakePollFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.full_dashboard, makePollFragment);
        fragmentTransaction.addToBackStack(MakePollFragment.NAME);
        fragmentTransaction.commit();
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLangChange(String lang) {

        if(LocalStore.getSavedLocale(getBaseContext()).equals(lang)) {
            return;
        }
        this.setLocale(lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    public void onPublish(String pollText, List<String> pollOptionsText, PLatLng latlng) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        ArrayList<String> pollOptionsTextArray = new ArrayList<>(pollOptionsText.size());
        pollOptionsTextArray.addAll(pollOptionsText);

        bundle.putSerializable("pollOptionsText", pollOptionsTextArray);
        bundle.putString("pollText", pollText);
        bundle.putSerializable("latlng", latlng);

        PublishFragment publishFragment = PublishFragment.newInstance(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.full_dashboard, publishFragment);
        fragmentTransaction.addToBackStack(MakePollFragment.NAME);
        fragmentTransaction.commit();
    }

    @Override
    public void onPollDetailCall(String pollId) {
        PollDetailFragment pollDetailFragment = PollDetailFragment.newInstance(pollId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        pollDetailFragment.show(fragmentManager, POLL_DETAIL_STACK_ID);
    }

    @Override
    public void onPollLocationCall(String pollId) {
        Intent intent = new Intent(this, FindLocation.class);
        intent.putExtra("POLL_ID", pollId);
        startActivity(intent);
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void pickSetReadyImage(Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setData(imageUri);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(resultCode != Activity.RESULT_OK) {
                Log.d("FEL", "NOT OK IMAGE");
                return;
            } else {
                Uri uri = data.getData();
                setImageForProfile(uri);
            }
        }
    }

    private void setImageForProfile(Uri uri) {
        Log.d("IMAGE COME", uri.toString());
        Fragment frag = fragmentHashMap.get("PROFILE");
        if(frag == null) { return; }
        ProfileFragment profileFragment = (ProfileFragment) frag;
        profileFragment.setNewProfileImage(uri);
    }

}



package com.example.bookfinderapp.viewmodels;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookfinderapp.R;
import com.example.bookfinderapp.models.VolumeBooks;
import com.example.bookfinderapp.viewmodels.SearchResultsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment{

    private EditText et_searchQuery;
    private ImageView ivAmico;

    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        et_searchQuery = view.findViewById(R.id.et_searchQuery);
        ivAmico = view.findViewById(R.id.iv_amico);

        getActivity().setTitle("Bookify");

        Animator translateAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.translate);
        translateAnimator.setTarget(ivAmico);
        translateAnimator.start();

        et_searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    try {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken()
                                ,InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getVolumeResponse();
                    return true;
                }

                return false;
            }
        });

        return view;
    }


    private void getVolumeResponse() {

        String queryString = et_searchQuery.getText().toString();

        //check if the user is connected to the internet
        if (isInternetAvailable(getActivity())) {
            //get the queryString data and pass it to SearchResultsActivity
            Intent queryIntent = new Intent(getActivity(), SearchResultsActivity.class);
            queryIntent.putExtra("query_string", queryString);
            startActivity(queryIntent);
        }


    }

    private static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("No Internet Connection");
            builder.setMessage("Unable to connect to Bookify. Please check your internet connection and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
            return false;
        }
        
        return true;
    }



}

package com.example.covid19updates;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class State extends Fragment {
    private RecyclerView recyclerView;
    private TableViewAdapter tableViewAdapter;
    private ArrayList<String> deaths, cases, active, recovered, state;
    private ArrayList<DataModel> mData;
    private ProgressDialog progress;
    private String totalActive, totalCases, totalDeaths, totalRecovered;
    private CoordinatorLayout container;

    public State() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_state, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        container = view.findViewById(R.id.container);
        recyclerView = view.findViewById(R.id.recyclerView);
        mData = new ArrayList<>();
        deaths = new ArrayList<>();
        active = new ArrayList<>();
        cases = new ArrayList<>();
        recovered = new ArrayList<>();
        state = new ArrayList<>();
        progress = new ProgressDialog(getContext());
        progress.setMessage("Fetching data...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        check();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void addData() {
        for (int i = 0; i < state.size(); i++) {
            mData.add(new DataModel("ok", state.get(i), cases.get(i), active.get(i), deaths.get(i), recovered.get(i)));
        }
        mData.add(new DataModel("ok", "Total", totalCases, totalActive, totalDeaths, totalRecovered));

    }

    private void getData() {
        progress.show();
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println(obj);
                    JSONObject obj2 = new JSONObject(obj.getString("data"));
                    JSONObject array2 = new JSONObject(obj2.getString("total"));
                    JSONArray array = new JSONArray(obj2.getString("statewise"));

                    totalCases = array2.getString("confirmed");
                    totalRecovered = array2.getString("recovered");
                    totalDeaths = array2.getString("deaths");
                    totalActive = array2.getString("active");
                    String refresh = obj2.getString("lastRefreshed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj3 = new JSONObject(array.getString(i));
                        state.add(obj3.getString("state"));
                        cases.add(obj3.getString("confirmed"));
                        recovered.add(obj3.getString("recovered"));
                        deaths.add(obj3.getString("deaths"));
                        active.add(obj3.getString("active"));
                    }

                    addData();
                    tableViewAdapter = new TableViewAdapter(mData, getContext());
                    recyclerView.setAdapter(tableViewAdapter);
                    progress.dismiss();
                    Toast.makeText(getContext(), "Last Updated : \t" + refresh, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
        queue.start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void check() {
        if (internetIsConnected() && isNetworkConnected()) {
            getData();
        } else {
            Snackbar snackbar = Snackbar.make(container, "Check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check();

                }
            });
        }
    }
}

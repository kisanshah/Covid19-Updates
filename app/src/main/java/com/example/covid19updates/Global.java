package com.example.covid19updates;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

public class Global extends Fragment {

    public Global() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false);
    }

    private ProgressDialog progress;
    private TextView totalCases;
    private TextView totalDeaths;
    private TextView totalRecovered;
    private TextView closedCases;
    private TextView activeCases;
    private TextView deaths;
    private TextView recovered;
    private int deathNumber, recoveredNumber;
    private CoordinatorLayout container;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        container = view.findViewById(R.id.container);
        deaths = view.findViewById(R.id.deaths);
        recovered = view.findViewById(R.id.recovered);
        closedCases = view.findViewById(R.id.totalClosed);
        activeCases = view.findViewById(R.id.totalActive);
        totalCases = view.findViewById(R.id.totalCases);
        totalDeaths = view.findViewById(R.id.totalDeaths);
        totalRecovered = view.findViewById(R.id.totalRecovered);


        progress = new ProgressDialog(getContext());
        progress.setMessage("Fetching data...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        check();
    }

    private void getGlobalUpdate() {
        progress.show();
        String url = "https://corona.lmao.ninja/all";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    DecimalFormat df = new DecimalFormat("#,###");
                    JSONObject obj = new JSONObject(response);
                    totalCases.setText(df.format(Integer.parseInt(obj.getString("cases"))));
                    totalDeaths.setText(df.format(Integer.parseInt(obj.getString("deaths"))));
                    totalRecovered.setText(df.format(Integer.parseInt(obj.getString("recovered"))));
                    activeCases.setText(df.format(Integer.parseInt(obj.getString("active"))));
                    recoveredNumber = Integer.parseInt(obj.getString("recovered"));
                    deathNumber = Integer.parseInt(obj.getString("deaths"));
                    deaths.setText(df.format(deathNumber));
                    recovered.setText(df.format(recoveredNumber));
                    closedCases.setText(df.format(recoveredNumber + deathNumber));
                    progress.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
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
            getGlobalUpdate();
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

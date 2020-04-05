package com.example.covid19updates;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

public class Country extends Fragment {

    public Country() {
    }

    private ProgressDialog progress;
    private TextView totalCases, totalDeaths, totalRecovered, countryName, closedCases, activeCases, deaths, recovered, critical, mild;
    private ImageView countryFlag;
    private FloatingActionButton searchFab;
    private int deathNumber, recoveredNumber;
    private TextInputEditText countryInput;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_country, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        coordinatorLayout = view.findViewById(R.id.coordinator);
        critical = view.findViewById(R.id.critical);
        mild = view.findViewById(R.id.mild);
        deaths = view.findViewById(R.id.deaths);
        recovered = view.findViewById(R.id.recovered);
        closedCases = view.findViewById(R.id.totalClosed);
        activeCases = view.findViewById(R.id.totalActive);
        searchFab = view.findViewById(R.id.searchFab);
        countryFlag = view.findViewById(R.id.flagImage);
        totalCases = view.findViewById(R.id.totalCases);
        countryName = view.findViewById(R.id.countryName);
        totalDeaths = view.findViewById(R.id.totalDeaths);
        totalRecovered = view.findViewById(R.id.totalRecovered);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Fetching data...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        check();
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetIsConnected() && isNetworkConnected()) {
                    OpenDialog();
                    searchFab.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.search));
                } else {
                    check();
                    searchFab.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.refresh));
                }
            }
        });
    }

    private void OpenDialog() {
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dailog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        countryInput = view.findViewById(R.id.countryInput);

        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String country = Objects.requireNonNull(countryInput.getText()).toString();
                getCountryUpdate2(country);
            }
        });

        builder.show();
    }


    private void getCountryUpdate2(final String country) {
        progress.show();
        String url = "https://corona.lmao.ninja/countries";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = new JSONObject(String.valueOf(array.getJSONObject(i)));
                        JSONObject obj3 = obj.getJSONObject("countryInfo");

                        if (obj.get("country").toString().equalsIgnoreCase(country) ||
                                obj3.get("iso2").toString().equalsIgnoreCase(country) || obj3.get("iso3").toString().equalsIgnoreCase(country)) {
                            JSONObject obj2 = new JSONObject(String.valueOf(array.getJSONObject(i)));
                            countryName.setText(obj2.toString());
                            DecimalFormat df = new DecimalFormat("#,###");

                            critical.setText(df.format(Integer.parseInt(obj2.getString("critical"))));
                            mild.setText(df.format(Integer.parseInt(obj2.getString("active")) - Integer.parseInt(obj2.getString("critical"))));
                            countryName.setText(obj2.getString("country"));
                            countryName.setAllCaps(true);
                            totalCases.setText(df.format(Integer.parseInt(obj2.getString("cases"))));
                            totalDeaths.setText(df.format(Integer.parseInt(obj2.getString("deaths"))));
                            totalRecovered.setText(df.format(Integer.parseInt(obj2.getString("recovered"))));
                            activeCases.setText(df.format(Integer.parseInt(obj2.getString("active"))));
                            recoveredNumber = Integer.parseInt(obj2.getString("recovered"));
                            deathNumber = Integer.parseInt(obj2.getString("deaths"));
                            deaths.setText(df.format(deathNumber));
                            recovered.setText(df.format(recoveredNumber));
                            closedCases.setText(df.format(recoveredNumber + deathNumber));

                            String flagUrl = obj3.getString("flag");
                            Picasso.with(getActivity()).load(flagUrl).into(countryFlag);
                        }
                    }

                    progress.dismiss();
                } catch (JSONException e) {
                    progress.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
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
            getCountryUpdate2("India");
            searchFab.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.search));
        } else {
            searchFab.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.refresh));
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check();

                }
            });
            snackbar.setDuration(20);
        }
    }
}

package com.example.covid19updates;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AllCountry extends Fragment {
    private ArrayList<String> country, flag;
    private ArrayList<Integer> totalCases, todayCases, totalDeaths, todayDeaths, totalRecovered, totalActive, totalCritical;
    private ArrayList<DataModel> mData;
    private RecyclerView recyclerView;
    private CoordinatorLayout container;
    private TableViewAdapter tableViewAdapter;

    public AllCountry() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_country, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        container = view.findViewById(R.id.container);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        country = new ArrayList<>();
        flag = new ArrayList<>();
        totalCases = new ArrayList<>();
        totalRecovered = new ArrayList<>();
        todayCases = new ArrayList<>();
        todayDeaths = new ArrayList<>();
        totalDeaths = new ArrayList<>();
        totalActive = new ArrayList<>();
        totalCritical = new ArrayList<>();
        mData = new ArrayList<>();
        check();
    }

    private void addData() {
        mData.add(new DataModel("COUNTRY_ITEM", country.get(0), flag.get(0), totalCases.get(0), todayCases.get(0),
                totalDeaths.get(0), todayDeaths.get(0), totalRecovered.get(0), totalActive.get(0), totalCritical.get(0), getContext()));
        for (int i = 1; i < country.size(); i++) {
            mData.add(new DataModel("COUNTRY_ITEM", country.get(i), flag.get(i), totalCases.get(i), todayCases.get(i),
                    totalDeaths.get(i), todayDeaths.get(i), totalRecovered.get(i), totalActive.get(i), totalCritical.get(i), getContext()));
        }
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        StringRequest request = new StringRequest(Request.Method.GET, "https://corona.lmao.ninja/countries/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = new JSONObject(String.valueOf(array.getJSONObject(i)));
                        country.add(temp.getString("country"));
                        totalCases.add(Integer.valueOf(temp.getString("cases")));
                        todayCases.add(Integer.valueOf(temp.getString("todayCases")));
                        totalDeaths.add(Integer.valueOf(temp.getString("deaths")));
                        todayDeaths.add(Integer.valueOf(temp.getString("todayDeaths")));
                        totalRecovered.add(Integer.valueOf(temp.getString("recovered")));
                        totalActive.add(Integer.valueOf(temp.getString("active")));
                        totalCritical.add(Integer.valueOf(temp.getString("critical")));
                        JSONObject temp2 = new JSONObject(temp.getString("countryInfo"));
                        flag.add(temp2.getString("flag"));
                        sort(totalCases);
                    }
                    addData();
                    tableViewAdapter = new TableViewAdapter(mData, getActivity());
                    recyclerView.setAdapter(tableViewAdapter);
                    sort(totalCases);

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void sort(ArrayList<Integer> arrayList) {
        int temp;
        String stringTemp;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (arrayList.get(i) > arrayList.get(j)) {
                    temp = arrayList.get(j);
                    arrayList.set(j, arrayList.get(i));
                    arrayList.set(i, temp);

                    stringTemp = flag.get(j);
                    flag.set(j, flag.get(i));
                    flag.set(i, stringTemp);

                    stringTemp = country.get(j);
                    country.set(j, country.get(i));
                    country.set(i, stringTemp);

                    temp = todayCases.get(j);
                    todayCases.set(j, todayCases.get(i));
                    todayCases.set(i, temp);

                    temp = totalDeaths.get(j);
                    totalDeaths.set(j, totalDeaths.get(i));
                    totalDeaths.set(i, temp);

                    temp = totalRecovered.get(j);
                    totalRecovered.set(j, totalRecovered.get(i));
                    totalRecovered.set(i, temp);

                    temp = totalActive.get(j);
                    totalActive.set(j, totalActive.get(i));
                    totalActive.set(i, temp);

                    temp = totalCritical.get(j);
                    totalCritical.set(j, totalCritical.get(i));
                    totalCritical.set(i, temp);

                    temp = todayDeaths.get(j);
                    todayDeaths.set(j, todayDeaths.get(i));
                    todayDeaths.set(i, temp);


                }
            }
        }
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
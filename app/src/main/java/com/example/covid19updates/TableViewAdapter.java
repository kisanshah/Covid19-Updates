package com.example.covid19updates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TableViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DataModel> mData;
    private Context context;

    private static final int COUNTRY_ITEM = 1;
    private static final int STATE_ITEM = 3;

    TableViewAdapter(ArrayList<DataModel> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        DataModel model = mData.get(position);
        if (model.getHeader().equals("COUNTRY_ITEM")) {
            return COUNTRY_ITEM;
        } else {
            return STATE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == COUNTRY_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.global_item_list, parent, false);
            return new CountryRowViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item_list, parent, false);
            return new StateRowViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataModel model = mData.get(position);
        DecimalFormat df = new DecimalFormat("#,##,###");
        int a = getItemViewType(position);
        if (a == COUNTRY_ITEM) {
            if (!model.getFlag().isEmpty()) {
                Picasso.with(context).load(model.getFlag()).into(((CountryRowViewHolder) holder).flag);
            }
            ((CountryRowViewHolder) holder).totalCases.setText(df.format(model.getTotalCases()) + "");
            ((CountryRowViewHolder) holder).todayCases.setText("+" + df.format(model.getTodayCases()));
            ((CountryRowViewHolder) holder).totalDeaths.setText(df.format(model.getTotalDeaths()) + "");
            ((CountryRowViewHolder) holder).todayDeaths.setText("+" + df.format(model.getTodayDeaths()) + "");
            ((CountryRowViewHolder) holder).totalRecovered.setText(df.format(model.getTotalRecovered()) + "");
            ((CountryRowViewHolder) holder).totalRecovered.setTextColor(context.getResources().getColor(R.color.green));
            ((CountryRowViewHolder) holder).totalDeaths.setTextColor(context.getResources().getColor(R.color.red));
            ((CountryRowViewHolder) holder).totalActive.setText(df.format(model.getTotalActive()) + "");
            ((CountryRowViewHolder) holder).totalCritical.setText(df.format(model.getTotalCritical()) + "");
            ((CountryRowViewHolder) holder).country.setText(model.getCountry());
        } else {
            ((StateRowViewHolder) holder).layout.setPadding(5, 5, 5, 5);
            ((StateRowViewHolder) holder).active.setText(model.getActive());
            ((StateRowViewHolder) holder).recovered.setText(model.getRecovered());
            ((StateRowViewHolder) holder).recovered.setTextColor(context.getResources().getColor(R.color.green));
            ((StateRowViewHolder) holder).deaths.setTextColor(context.getResources().getColor(R.color.red));
            ((StateRowViewHolder) holder).cases.setTextColor(context.getResources().getColor(R.color.grey));
            ((StateRowViewHolder) holder).active.setTextColor(context.getResources().getColor(R.color.grey));

            ((StateRowViewHolder) holder).cases.setText(model.getCases());
            ((StateRowViewHolder) holder).deaths.setText(model.getDeaths());
            ((StateRowViewHolder) holder).state.setText(model.getState());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class StateRowViewHolder extends RecyclerView.ViewHolder {
        TextView state, cases, deaths, recovered, active;
        LinearLayout layout;

        StateRowViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.container);
            state = itemView.findViewById(R.id.state);
            cases = itemView.findViewById(R.id.cases);
            deaths = itemView.findViewById(R.id.deaths);
            recovered = itemView.findViewById(R.id.recovered);
            active = itemView.findViewById(R.id.active);
        }
    }

    static class CountryRowViewHolder extends RecyclerView.ViewHolder {
        TextView totalCases, todayCases, totalDeaths, todayDeaths, totalRecovered, totalActive, totalCritical, country;
        ImageView flag;
        LinearLayout layout;

        CountryRowViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.container);
            flag = itemView.findViewById(R.id.flag);
            country = itemView.findViewById(R.id.country);
            totalCases = itemView.findViewById(R.id.totalCases);
            todayCases = itemView.findViewById(R.id.newCases);
            totalDeaths = itemView.findViewById(R.id.totalDeaths);
            todayDeaths = itemView.findViewById(R.id.newDeaths);
            totalRecovered = itemView.findViewById(R.id.totalRecovered);
            totalActive = itemView.findViewById(R.id.totalActive);
            totalCritical = itemView.findViewById(R.id.totalCritical);

        }
    }
}

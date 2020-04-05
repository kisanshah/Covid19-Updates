package com.example.covid19updates;

import android.content.Context;

public class DataModel {
    private String header;
    private String country;
    private String flag;
    private int totalCases;
    private int todayCases;
    private int totalDeaths;
    private int todayDeaths;
    private int totalRecovered;
    private int totalActive;
    private int totalCritical;
    private Context context;
    private String state;
    private String Cases;
    private String Active;
    private String Deaths;
    private String Recovered;

    DataModel(String header, String country, String flag, int totalCases, int todayCases, int totalDeaths, int todayDeaths, int totalRecovered, int totalActive, int totalCritical, Context context) {
        this.country = country;
        this.header = header;
        this.flag = flag;
        this.totalCases = totalCases;
        this.todayCases = todayCases;
        this.totalDeaths = totalDeaths;
        this.todayDeaths = todayDeaths;
        this.totalRecovered = totalRecovered;
        this.totalActive = totalActive;
        this.totalCritical = totalCritical;
        this.context = context;
    }

    DataModel(String header, String state, String cases, String active, String deaths, String recovered) {
        this.state = state;
        this.header = header;
        this.Cases = cases;
        this.Active = active;
        this.Deaths = deaths;
        this.Recovered = recovered;
    }

    public Context getContext() {
        return context;
    }

    String getState() {
        return state;
    }

    String getCases() {
        return Cases;
    }

    String getActive() {
        return Active;
    }

    public String getDeaths() {
        return Deaths;
    }

    public String getRecovered() {
        return Recovered;
    }


    String getHeader() {
        return header;
    }

    String getCountry() {
        return country;
    }

    String getFlag() {
        return flag;
    }

    int getTotalCases() {
        return totalCases;
    }

    int getTodayCases() {
        return todayCases;
    }

    int getTotalDeaths() {
        return totalDeaths;
    }

    int getTodayDeaths() {
        return todayDeaths;
    }

    int getTotalRecovered() {
        return totalRecovered;
    }

    int getTotalActive() {
        return totalActive;
    }

    int getTotalCritical() {
        return totalCritical;
    }
}

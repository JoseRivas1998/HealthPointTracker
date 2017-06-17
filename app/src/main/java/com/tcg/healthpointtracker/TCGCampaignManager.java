package com.tcg.healthpointtracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoseR on 6/11/2017.
 */
public class TCGCampaignManager {

    List<TCGCampaign> campaigns;

    public TCGCampaignManager(Activity activity) {
        campaigns = new ArrayList<>();
    }

    private static String defaultJsonString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            jsonObject.put("campaigns", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String jsonString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        for(int i = 0; i < campaigns.size(); i++) {
            array.put(campaigns.get(i).toJSONObject());
        }
        try {
            jsonObject.put("campaigns", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public void loadCampaigns(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(activity.getString(R.string.saved_campaigns), defaultJsonString());
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("campaigns");
            for(int i = 0; i < jsonArray.length(); i++) {
                campaigns.add(new TCGCampaign(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveCampaigns(Activity activity) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(activity.getString(R.string.saved_campaigns), jsonString());
        editor.commit();
    }

    public void addCampaign(String campaignTitle, int baseHP) {
        campaigns.add(new TCGCampaign(campaignTitle, baseHP));
    }

    public void addCampaign(Activity activity, String campaignTitle, int baseHP) {
        campaigns.add(new TCGCampaign(campaignTitle, baseHP));
        saveCampaigns(activity);
    }

    public void removeCampaign(int i) {
        campaigns.remove(i);
    }

    public int size() {
        return campaigns.size();
    }

    public int getBaseHP(int i) {
        return campaigns.get(i).getBaseHP();
    }

    public void setBaseHP(int i, int baseHP) {
        campaigns.get(i).setBaseHP(baseHP);
    }

    public String getCampaignTitle(int i) {
        return campaigns.get(i).getCampaignTitle();
    }

    public void setCampaignTitle(int i, String campaignTitle) {
        campaigns.get(i).setCampaignTitle(campaignTitle);
    }

    public int getCurrentHP(int i) {
        return campaigns.get(i).getCurrentHP();
    }

    public void setCurrentHP(int i, int currentHP) {
        campaigns.get(i).setCurrentHP(currentHP);
    }

}

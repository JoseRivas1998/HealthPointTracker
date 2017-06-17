package com.tcg.healthpointtracker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by JoseR on 6/11/2017.
 */
public class AddCampaignFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_campaign_layout, container, false);

        Button addButton = (Button) view.findViewById(R.id.add_campaign_button);
        addButton.setOnClickListener(v -> {
            String campaignTitle = ((EditText) view.findViewById(R.id.add_campaign_title_edit)).getText().toString();
            String baseHPString = ((EditText) view.findViewById(R.id.add_base_hp_edit)).getText().toString();
            if(campaignTitle.length() > 0 && baseHPString.length() > 0) {
                int baseHP = new Integer(baseHPString);
                new AlertDialog.Builder(getActivity()).setMessage(String.format("Add Campaign, \"%s\", with base HP of %d?", campaignTitle, baseHP))
                        .setTitle(getActivity().getString(R.string.add_campaign))
                        .setPositiveButton("Yes", (arg0, arg1) -> {
                            MainActivity.campaignManager.addCampaign(getActivity(), campaignTitle, baseHP);
                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

                            final Menu menu = navigationView.getMenu();
                            menu.add(R.id.main_group, MainActivity.campaignManager.size() - 1, MainActivity.campaignManager.size(), campaignTitle);
                            TCGHelperMethods.showSimpleAlertMessage(getActivity(), getActivity().getString(R.string.add_campaign), "Added campaign");
                        })
                        .setNegativeButton("No", (arg0, arg1) -> {})
                        .show();
            } else {
                TCGHelperMethods.showSimpleAlertMessage(getActivity(), getActivity().getString(R.string.add_campaign), "Please enter a title and base HP");
            }
        });

        return view;
    }

}

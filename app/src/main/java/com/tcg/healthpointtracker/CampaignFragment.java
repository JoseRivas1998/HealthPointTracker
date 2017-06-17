package com.tcg.healthpointtracker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by JoseR on 6/11/2017.
 */
public class CampaignFragment extends Fragment {

    private View view;
    private int id;
    private TextView campaign_fragment_title;
    private TextView campaign_fragment_basehp;
    private TextView campaign_fragment_currenthp;
    private EditText campaign_fragment_settings_update_title_text;
    private EditText campaign_fragment_settings_update_base_hp_text;
    private UpdateNavigationListener updateNavigationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        id = getArguments().getInt("id");

        view = inflater.inflate(R.layout.campaign_fragment_layout, container, false);

        FontManager.markAsIconContainer(view.findViewById(R.id.campaign_fragment_minus_button), FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));
        FontManager.markAsIconContainer(view.findViewById(R.id.campaign_fragment_plus_button), FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));
        FontManager.markAsIconContainer(view.findViewById(R.id.campaign_fragment_settings_update_title_save), FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));
        FontManager.markAsIconContainer(view.findViewById(R.id.campaign_fragment_settings_update_base_hp_save), FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));

        campaign_fragment_title = (TextView) view.findViewById(R.id.campaign_fragment_title);
        campaign_fragment_title.setText(MainActivity.campaignManager.getCampaignTitle(id));

        campaign_fragment_basehp = (TextView) view.findViewById(R.id.campaign_fragment_basehp);
        campaign_fragment_basehp.setText(String.valueOf(MainActivity.campaignManager.getBaseHP(id)));

        campaign_fragment_currenthp = (TextView) view.findViewById(R.id.campaign_fragment_currenthp);
        campaign_fragment_currenthp.setText(String.valueOf(MainActivity.campaignManager.getCurrentHP(id)));

        Button campaign_fragment_minus_button = (Button) view.findViewById(R.id.campaign_fragment_minus_button);

        campaign_fragment_minus_button.setOnClickListener(v -> {
            String modString = ((EditText) view.findViewById(R.id.campaign_fragment_mod)).getText().toString();
            if(modString.length() > 0) {
                int currentHP = MainActivity.campaignManager.getCurrentHP(id);
                int mod = Integer.valueOf(modString);
                if(currentHP - mod < 0) {
                    new AlertDialog.Builder(getActivity()).setMessage(getText(R.string.campaign_fragment_subconf_text))
                            .setTitle(getText(R.string.campaign_fragment_subconf_title))
                            .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                                updateHP(TCGHelperMethods.clamp(currentHP - mod, 0, Integer.MAX_VALUE));
                            })
                            .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {
                                updateHP(currentHP - mod);
                            })
                            .show();
                } else {
                    updateHP(currentHP - mod);
                }

            } else {
                TCGHelperMethods.showSimpleAlertMessage(getActivity(), getString(R.string.campaign_fragment_nomod_title), getString(R.string.campaign_fragment_nomod_text));
            }
        });

        Button campaign_fragment_plus_button = (Button) view.findViewById(R.id.campaign_fragment_plus_button);

        campaign_fragment_plus_button.setOnClickListener(v -> {
            String modString = ((EditText) view.findViewById(R.id.campaign_fragment_mod)).getText().toString();
            if(modString.length() > 0) {
                int currentHP = MainActivity.campaignManager.getCurrentHP(id);
                int mod = Integer.valueOf(modString);
                int baseHP = MainActivity.campaignManager.getBaseHP(id);
                if(currentHP + mod > baseHP) {
                    new AlertDialog.Builder(getActivity()).setMessage(getText(R.string.campaign_fragment_addconf_text))
                            .setTitle(getText(R.string.campaign_fragment_addconf_title))
                            .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                                updateHP(TCGHelperMethods.clamp(currentHP + mod, 0, baseHP));
                            })
                            .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {
                                updateHP(currentHP + mod);
                            })
                            .show();
                } else {
                    updateHP(currentHP + mod);
                }

            } else {
                TCGHelperMethods.showSimpleAlertMessage(getActivity(), getString(R.string.campaign_fragment_nomod_title), getString(R.string.campaign_fragment_nomod_text));
            }
        });

        Button campaign_fragment_full_heal_button = (Button) view.findViewById(R.id.campaign_fragment_full_heal_button);

        campaign_fragment_full_heal_button.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity()).setMessage(getText(R.string.campaign_fragment_full_heal_conf_text))
                    .setTitle(getText(R.string.campaign_fragment_full_heal_conf_title))
                    .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                        int baseHP = MainActivity.campaignManager.getBaseHP(id);
                        updateHP(baseHP);
                    })
                    .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {})
                    .show();
        });

        Button campaign_fragment_die_button = (Button) view.findViewById(R.id.campaign_fragment_die_button);
        campaign_fragment_die_button.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity()).setMessage(getText(R.string.campaign_fragment_die_conf_text))
                    .setTitle(getText(R.string.campaign_fragment_die_conf_title))
                    .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                        updateHP(0);
                    })
                    .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {})
                    .show();
        });

        campaign_fragment_settings_update_title_text = (EditText) view.findViewById(R.id.campaign_fragment_settings_update_title_text);
        campaign_fragment_settings_update_title_text.setText(MainActivity.campaignManager.getCampaignTitle(id));

        Button campaign_fragment_settings_update_title_save = (Button) view.findViewById(R.id.campaign_fragment_settings_update_title_save);
        campaign_fragment_settings_update_title_save.setOnClickListener(v -> {
            String currentTitle = MainActivity.campaignManager.getCampaignTitle(id);
            String newTitle =  campaign_fragment_settings_update_title_text.getText().toString();
            new AlertDialog.Builder(getActivity()).setMessage(String.format(getString(R.string.campaign_fragment_update_title_conf_text), currentTitle, newTitle))
                    .setTitle(getText(R.string.campaign_fragment_update_title_conf_title))
                    .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                        if(newTitle.length() > 0) {
                            MainActivity.campaignManager.setCampaignTitle(id, newTitle);
                            MainActivity.campaignManager.saveCampaigns(getActivity());
                            campaign_fragment_title.setText(MainActivity.campaignManager.getCampaignTitle(id));
                            updateNavigationListener.updateNavigation(id);
                        } else {
                            TCGHelperMethods.showSimpleAlertMessage(getActivity(), getString(R.string.campaign_fragment_update_title_err_title), getString(R.string.campaign_fragment_update_title_err_text));
                        }
                    })
                    .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {})
                    .show();
        });

        campaign_fragment_settings_update_base_hp_text = (EditText) view.findViewById(R.id.campaign_fragment_settings_update_base_hp_text);
        campaign_fragment_settings_update_base_hp_text.setText(String.valueOf(MainActivity.campaignManager.getBaseHP(id)));
        Button campaign_fragment_settings_update_base_hp_save = (Button) view.findViewById(R.id.campaign_fragment_settings_update_base_hp_save);
        campaign_fragment_settings_update_base_hp_save.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity()).setTitle(getText(R.string.campaign_fragment_update_base_hp_conf_title))
                    .setItems(getResources().getStringArray(R.array.update_base_hp_conf_buttons), (dialog, which) -> {
                        String newBaseHPString = campaign_fragment_settings_update_base_hp_text.getText().toString();
                        boolean canceled = false;
                        if(newBaseHPString.length() > 0) {
                            int currentBaseHP = MainActivity.campaignManager.getBaseHP(id);
                            int newBaseHP = Integer.valueOf(newBaseHPString);
                            if (which == getResources().getInteger(R.integer.option_keep_diff)) {
                                Log.i(getClass().getName(), "Keep Difference");
                                int difference = currentBaseHP - MainActivity.campaignManager.getCurrentHP(id);
                                int newCurrent = newBaseHP - difference;
                                MainActivity.campaignManager.setCurrentHP(id, newCurrent);
                                MainActivity.campaignManager.setBaseHP(id, newBaseHP);
                            } else if (which == getResources().getInteger(R.integer.option_keep_current)) {
                                MainActivity.campaignManager.setBaseHP(id, newBaseHP);
                            } else if (which == getResources().getInteger(R.integer.option_restore_health)) {
                                MainActivity.campaignManager.setCurrentHP(id, newBaseHP);
                                MainActivity.campaignManager.setBaseHP(id, newBaseHP);
                            } else if (which == getResources().getInteger(R.integer.option_cancel)) {
                                canceled = true;
                                dialog.cancel();
                            }
                            if(!canceled) {
                                MainActivity.campaignManager.saveCampaigns(getActivity());
                                campaign_fragment_currenthp.setText(String.valueOf(MainActivity.campaignManager.getCurrentHP(id)));
                                campaign_fragment_basehp.setText(String.valueOf(MainActivity.campaignManager.getBaseHP(id)));
                            }
                        } else {
                            TCGHelperMethods.showSimpleAlertMessage(getActivity(), getString(R.string.campaign_fragment_update_base_hp_err_title), getString(R.string.campaign_fragment_update_base_hp_err_text));
                        }
                    })
                    .show();
        });

        Button campaign_fragment_settings_delete_button = (Button) view.findViewById(R.id.campaign_fragment_settings_delete_button);
        campaign_fragment_settings_delete_button.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.campaign_fragment_delete_conf_text))
                    .setTitle(getText(R.string.campaign_fragment_delete_conf_title))
                    .setPositiveButton(getText(R.string.option_yes), (arg0, arg1) -> {
                        MainActivity.campaignManager.removeCampaign(id);
                        MainActivity.campaignManager.saveCampaigns(getActivity());
                        updateNavigationListener.updateNavigation(0);
                    })
                    .setNegativeButton(getText(R.string.option_no), (arg0, arg1) -> {})
                    .show();
        });

        return view;
    }

    private void updateHP(int newHP) {
        MainActivity.campaignManager.setCurrentHP(id, newHP);
        MainActivity.campaignManager.saveCampaigns(getActivity());
        campaign_fragment_currenthp.setText(String.valueOf(MainActivity.campaignManager.getCurrentHP(id)));
    }

    public void setUpdateNavigationListener(UpdateNavigationListener updateNavigationListener) {
        this.updateNavigationListener = updateNavigationListener;
    }
}

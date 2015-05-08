package com.alieeen.snitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.alieeen.snitch.fragments.AboutFragment;
import com.alieeen.snitch.fragments.CamerasFragment;
import com.alieeen.snitch.fragments.EventsFragment;
import com.alieeen.snitch.fragments.LiveVideoFragment;
import com.alieeen.snitch.fragments.MyAccountFragment;
import com.alieeen.snitch.fragments.SettingsFragment;
import com.alieeen.snitch.loginfragments.Login01Fragment;
import com.alieeen.snitch.rest.SnitchHttpClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;

@EActivity
public class MainActivity extends MaterialNavigationDrawer implements MaterialAccountListener {



    @Override
    public void init(Bundle savedInstanceState) {

        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(), "Casa", "", null, R.drawable.header_background);
        this.addAccount(account);

        MaterialAccount account2 = new MaterialAccount(this.getResources(), "Empresa", "", null, R.drawable.header_background);
        this.addAccount(account2);

        // set listener
        this.setAccountListener(this);

        // create sections
        this.addSection(newSection("Eventos", R.drawable.ic_eventos, new EventsFragment()));
        this.addSection(newSection("Câmeras", R.drawable.ic_camera, new CamerasFragment()));
        this.addSection(newSection("Ao vivo", R.drawable.ic_livevideo, new LiveVideoFragment()));
        this.addSection(newSection("Minha Conta", R.drawable.ic_account, new MyAccountFragment()));
        //this.addSection(newSection("Section 2",new FragmentIndex()));
        //this.addSection(newSection("Section 3",R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        //this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        this.addBottomSection(newSection("Configurações", R.drawable.ic_settings, new SettingsFragment()));
        this.addBottomSection(newSection("Sobre", R.drawable.ic_menu_info, new AboutFragment()));

    }



    @Override
    public void onAccountOpening(MaterialAccount account) {

    }

    @Override
    public void onChangeAccount(MaterialAccount newAccount) {

    }

}

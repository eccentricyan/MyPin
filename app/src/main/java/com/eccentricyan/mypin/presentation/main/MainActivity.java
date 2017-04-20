package com.eccentricyan.mypin.presentation.main;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import com.eccentricyan.mypin.R;
import com.eccentricyan.mypin.presentation.base.BaseActivity;
import com.eccentricyan.mypin.presentation.card.CardListFragment;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import com.pinterest.android.pdk.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.eccentricyan.mypin.common.defines.Defines.ACCOUNT_TYPE;
import static com.eccentricyan.mypin.common.defines.Defines.AUTH_TOKEN_TYPE;
import static com.eccentricyan.mypin.common.defines.Defines.USER_FIELDS;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String access_token;
    private PDKUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pinterest();
    }

    private void pinterest() {
        Log.e("pinterest", token);
        if (!Utils.isEmpty(token)) {
            Log.e("pinterest", token);
            getMe(false);
        } else {
            login();
        }
    }

    private void  getMe(Boolean addAccount) {
        pdkClient.getMe(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                user = response.getUser();
                Log.e("login success", user.getUsername() + " " + access_token + addAccount.toString());
                if (addAccount) {
                    addAccount(user.getUsername(), access_token);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, CardListFragment.newInstance())
                        .commit();
            }
            @Override
            public void onFailure(PDKException exception) {
                Log.e("login failed", exception.getMessage());
                login();
            }
        });
    }

    private void login() {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);

        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                getMe(true);
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    private void setAccount() {
//        removeAccount();
//        if (addAccount(session.username, key.accessToken)) {
//            result = new Bundle();
//            result.putString(AccountManager.KEY_ACCOUNT_NAME, session.username);
//            result.putString(AccountManager.KEY_ACCOUNT_TYPE, Defines.ACCOUNT_TYPE);
//        } else {
//            Toast.makeText(context, "failure login", Toast.LENGTH_SHORT).show();
//        }
    }

    private void removeAccount() {
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        for (final Account account : accounts) {
            accountManager.removeAccount(account, future -> {
                try {
                    boolean result1 = future.getResult();
                    Log.d("remove account result:", result1 + " account:" + account);
                } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                    e.printStackTrace();
                }
            }, null);
        }
    }

    private boolean addAccount(@NonNull String name, @NonNull String token) {
        removeAccount();
        Account newAccount = new Account(name, ACCOUNT_TYPE);
        try {
            boolean success = accountManager.addAccountExplicitly(newAccount, null, null);

            if (success) {
                // TODO encrypt token
                accountManager.setAuthToken(newAccount, AUTH_TOKEN_TYPE, token);
            } else {
                Log.e("errorAccount", "errorAccount");
            }
            return success;
        } catch (Exception e) {
            Log.e("errorAccount", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String response = data.getStringExtra("PDKCLIENT_EXTRA_RESULT");
        Uri parsedResponse = Uri.parse(response);
        access_token = parsedResponse.getQueryParameter("access_token");
        pdkClient.onOauthResponse(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

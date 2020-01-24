package com.example.lenovo.emptypro.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.lenovo.emptypro.Adapters.ViewPageAdapter;
import com.example.lenovo.emptypro.Fragments.AddForSaleFragKotlin;
import com.example.lenovo.emptypro.Fragments.ChatFrag;
import com.example.lenovo.emptypro.Fragments.HomeFragKotlin;
import com.example.lenovo.emptypro.Fragments.ProfileFragKotlin;
import com.example.lenovo.emptypro.Listeners.OnFragmentInteractionListener;
import com.example.lenovo.emptypro.R;
import com.example.lenovo.emptypro.Utilities.Utilities;
import com.example.lenovo.emptypro.Utils.CustomViewPager;
import com.example.lenovo.emptypro.Utils.SelectViewPager;
import com.example.lenovo.emptypro.Utils.SharedPrefUtil;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener, SelectViewPager , OnFragmentInteractionListener , FragmentManager.OnBackStackChangedListener{
    public CustomViewPager viewPager;
    //ViewPagerAdapter adapter;
    public AHBottomNavigation bottomNavigation;
    HomeFragKotlin homeFragment;
    ChatFrag chatFragment;
    AddForSaleFragKotlin addForSaleFragment;
    ProfileFragKotlin profileFragment;
    ViewPageAdapter adapter;
    ActionBarDrawerToggle toggle;
    Utilities utilities=new Utilities();

    private TextView mTextMessage;
    View.OnClickListener navigationBackPressListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigation=(AHBottomNavigation)findViewById(R.id.bottom_navigation );
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                Log.d("position", "position " + position);
                viewPager.setCurrentItem(position);
                return true;
            }
        });

        setBOttomBarData();
        viewPager.setPagingEnabled(false);


        navigationBackPressListener = new View.OnClickListener(){
            public void onClick(View v)
            {
                onBackPressed();
            }
        };
    }

    private void setBOttomBarData() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("HOME", R.drawable.ic_home_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("PROFILE", R.drawable.ic_profile, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("CHAT", R.drawable.ic_profile, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("ADD For Sale", R.drawable.ic_history, R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        ////// addin notification badge

        AHNotification notification = new AHNotification.Builder()
                .setText("1")
                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                .build();
        bottomNavigation.setNotification(notification, 3);


         bottomNavigation.setAccentColor(Color.parseColor("#cc3b3b"));
        bottomNavigation.setInactiveColor(Color.parseColor("#000000"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

        adapter = new ViewPageAdapter(getSupportFragmentManager());
         homeFragment= new HomeFragKotlin();

        profileFragment= new ProfileFragKotlin();
        chatFragment= new ChatFrag();
        addForSaleFragment= new AddForSaleFragKotlin();
        adapter.addFragment(homeFragment);
        adapter.addFragment(profileFragment);
        adapter.addFragment(chatFragment);
        adapter.addFragment(addForSaleFragment);

        viewPager.setAdapter(adapter);
        // bottomNavigation.setNotification("", 4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e( "onPageScrolled ",""+position);

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setAccentColor(Color.parseColor("#cc3b3b"));
                bottomNavigation.setInactiveColor(Color.parseColor("#000000"));
                bottomNavigation.setCurrentItem(position);
                Log.e( "onPageSelected ",""+position);
                if(position==3)
                {
                    AHNotification notification = new AHNotification.Builder()
                            .setText("")
                            .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.design_default_color_primary_dark))
                            .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                            .build();
                    bottomNavigation.setNotification(notification, 3);
                }   }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e( "onPageScrollStateCh ",""+state);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(getApplication(), FavouritePets.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
         //   finish();
        } else if (id == R.id.nav_sold) {
            Intent intent = new Intent(getApplication(), SoldPets.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
           // finish();
        }
        else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(getApplication(), FeedbackScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
           // finish();
        } else if (id == R.id.nav_termCondition) {
            Intent intent = new Intent(getApplication(), WebUrlActivity.class);
             overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
intent.putExtra("from","terms");
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(getApplication(), WebUrlActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            intent.putExtra("from","privacy");
            startActivity(intent);
        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(getApplication(), WebUrlActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            intent.putExtra("from","about");
            startActivity(intent);

        } else if (id == R.id.nav_uploaded_petes) {
            Intent intent = new Intent(getApplication(), UploadedPets.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
           // finish();
        } else if (id == R.id.nav_share) {
            shareTextUrl();
        } else if (id == R.id.nav_logout) {
        utilities.alertLogoutDialog(MainActivity.this,"Want to Logout ?");

          /*  SharedPrefUtil.setUserId(MainActivity.this,"");
            Intent intent = new Intent(getApplication(), LoginSignUp.class);
            //       intent.putExtra("id", "" + id);
            //       intent.putExtra("firstName", "" + first_name);
            //      intent.putExtra("lastName", "" + last_name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
           // finish();*/
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "VLovePetss");
        share.putExtra(Intent.EXTRA_TEXT, "Coming Soon..");

        startActivity(Intent.createChooser(share, "Share To Friends !"));
    }




    private void initComponent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(SharedPrefUtil.getUserId(this)!="") {
             TextView tv_loggedEmail = navigationView.getHeaderView(0).findViewById(R.id.textView);
                    tv_loggedEmail.setText("Logged Mobile :- "+ SharedPrefUtil.getUserMobile(this));
         }

    }

    @Override
    public void currentItem(int pageNum) {
        viewPager.setCurrentItem(pageNum);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackStackChanged() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toggle.setToolbarNavigationClickListener(navigationBackPressListener);
        }
        else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);

            toggle.setToolbarNavigationClickListener(toggle.getToolbarNavigationClickListener());
        }
    }
}
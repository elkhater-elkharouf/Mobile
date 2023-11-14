package com.example.erecrutement;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.erecrutement.Controller.ViewBlogsActivity;
import com.example.erecrutement.Quiz.HomeQuiz;
import com.example.erecrutement.candidature.ui.admin.CandidatureListAdminFragment;
import com.example.erecrutement.candidature.ui.user.CandidatureListUserFragment;
import com.example.erecrutement.candidature.ui.user.ManageCandidatureUserActivity;
import com.example.erecrutement.offres.HomeFragment;
import com.example.erecrutement.offres.uiCandidat.OffresFragment;
import com.example.erecrutement.offres.uiRecruteur.ListOffresFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout;
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;


        setContentView(R.layout.activity_main);
        //  setContentView(R.layout.fragment_job_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar ,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_offres){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new OffresFragment()).commit();
        }
        if (id == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }
        if (id == R.id.nav_espace_recruteur){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new ListOffresFragment()).commit();
        }
        if (id == R.id.nav_candidature_admin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CandidatureListAdminFragment()).commit();
        }
        if (id == R.id.nav_candidature_user) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CandidatureListUserFragment()).commit();
        }
        if (id == R.id.nav_forum) {
            Intent i = new Intent( MainActivity.this, ViewBlogsActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_quiz) {
            Intent i = new Intent( MainActivity.this, HomeQuiz.class);
            startActivity(i);
        }

     drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void navigateToCandidatureUser() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CandidatureListUserFragment()).commit();
    }
}
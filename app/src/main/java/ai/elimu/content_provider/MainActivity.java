package ai.elimu.content_provider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ai.elimu.content_provider.language.SelectLanguageActivity;
import ai.elimu.content_provider.language.SharedPreferencesHelper;
import ai.elimu.model.v2.enums.Language;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Language language = SharedPreferencesHelper.getLanguage(getApplicationContext());
        Log.i(getClass().getName(), "language: " + language);
        if (language == null) {
            // Redirect to language selection

            Intent selectLanguageIntent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(selectLanguageIntent);
            finish();
        } else {
            // Redirect to content

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            appBarConfiguration = new AppBarConfiguration
                    .Builder(
                            // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
                            R.id.nav_home,
                            R.id.nav_letters,
                            R.id.nav_sounds,
                            R.id.nav_words,
                            R.id.nav_numbers,
                            R.id.nav_emojis,
                            R.id.nav_images,
                            R.id.nav_audios,
                            R.id.nav_storybooks,
                            R.id.nav_videos
                    )
                    .setDrawerLayout(drawer)
                    .build();

            NavigationView navigationView = findViewById(R.id.nav_view);
            View navigationViewHeaderLayoutView = navigationView.getHeaderView(0);
            TextView navHeaderSubtitleTextView = navigationViewHeaderLayoutView.findViewById(R.id.navHeaderSubtitle);
            BaseApplication baseApplication = (BaseApplication) getApplication();
            navHeaderSubtitleTextView.setText(baseApplication.getBaseUrl());

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(getClass().getName(), "onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i(getClass().getName(), "onSupportNavigateUp");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}

package ai.elimu.content_provider

import ai.elimu.content_provider.language.SelectLanguageActivity
import ai.elimu.content_provider.util.SharedPreferencesHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(javaClass.name, "onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val language = SharedPreferencesHelper.getLanguage(
            applicationContext
        )
        Log.i(javaClass.name, "language: ${language}")
        if (language == null) {
            // Redirect to language selection

            val selectLanguageIntent = Intent(
                applicationContext,
                SelectLanguageActivity::class.java
            )
            startActivity(selectLanguageIntent)
            finish()
        } else {
            // Redirect to content

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            appBarConfiguration =
                AppBarConfiguration.Builder( // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
                    R.id.nav_home,
                    R.id.nav_letters,
                    R.id.nav_sounds,
                    R.id.nav_letter_sounds,
                    R.id.nav_words,
                    R.id.nav_numbers,
                    R.id.nav_emojis,
                    R.id.nav_images,
                    R.id.nav_storybooks,
                    R.id.nav_videos
                )
                    .setOpenableLayout(drawer)
                    .build()

            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            val navigationViewHeaderLayoutView = navigationView.getHeaderView(0)
            val navHeaderSubtitleTextView =
                navigationViewHeaderLayoutView.findViewById<TextView>(R.id.navHeaderSubtitle)
            val baseApplication = application as BaseApplication
            navHeaderSubtitleTextView.text = baseApplication.baseUrl

            val navController = findNavController(this, R.id.nav_host_fragment)

            setupActionBarWithNavController(
                this, navController,
                appBarConfiguration!!
            )
            setupWithNavController(navigationView, navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i(javaClass.name, "onCreateOptionsMenu")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.i(javaClass.name, "onSupportNavigateUp")

        val navController = findNavController(this, R.id.nav_host_fragment)
        return navigateUp(navController, appBarConfiguration!!) || super.onSupportNavigateUp()
    }
}

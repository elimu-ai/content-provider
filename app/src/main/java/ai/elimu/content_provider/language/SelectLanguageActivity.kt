package ai.elimu.content_provider.language

import ai.elimu.content_provider.R
import ai.elimu.content_provider.language.LanguageListDialogFragment.Companion.newInstance
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SelectLanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(javaClass.name, "onCreate")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_select_language)
    }

    override fun onStart() {
        Log.i(javaClass.name, "onStart")
        super.onStart()

        newInstance().show(supportFragmentManager, "dialog")
    }
}

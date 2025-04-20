package ai.elimu.content_provider.ui.storybook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoryBooksViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "StoryBooksViewModel"
    }

    fun getText(): LiveData<String> {
        Log.i(javaClass.name, "getText")

        return text
    }
}

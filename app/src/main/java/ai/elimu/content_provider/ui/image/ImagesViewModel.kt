package ai.elimu.content_provider.ui.image

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImagesViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "ImagesViewModel"
    }

    fun getText(): LiveData<String> {
        Log.i(javaClass.name, "getText")

        return text
    }
}
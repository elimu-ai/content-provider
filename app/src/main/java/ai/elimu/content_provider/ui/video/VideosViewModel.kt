package ai.elimu.content_provider.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideosViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "VideosViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
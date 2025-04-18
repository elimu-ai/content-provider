package ai.elimu.content_provider.ui.emoji

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmojisViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "EmojisViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
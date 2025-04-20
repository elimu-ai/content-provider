package ai.elimu.content_provider.ui.sound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundsViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "SoundsViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
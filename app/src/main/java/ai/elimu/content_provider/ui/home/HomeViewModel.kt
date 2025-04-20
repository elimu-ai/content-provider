package ai.elimu.content_provider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "HomeViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
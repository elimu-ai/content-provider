package ai.elimu.content_provider.ui.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordsViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "WordsViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
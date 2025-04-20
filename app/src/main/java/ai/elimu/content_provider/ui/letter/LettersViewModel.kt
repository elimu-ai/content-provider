package ai.elimu.content_provider.ui.letter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LettersViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "LettersViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
package ai.elimu.content_provider.ui.letter_sound

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LetterSoundsViewModel : ViewModel() {
    val text = MutableLiveData<String>()

    init {
        text.value = "LetterSoundsViewModel"
    }
}
package ai.elimu.content_provider.ui.number

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NumbersViewModel : ViewModel() {
    private val text = MutableLiveData<String>()

    init {
        text.value = "NumbersViewModel"
    }

    fun getText(): LiveData<String> {
        return text
    }
}
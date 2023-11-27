package ai.elimu.content_provider.ui.letter_sound;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LetterSoundsViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public LetterSoundsViewModel() {
        text = new MutableLiveData<>();
        text.setValue("LetterSoundsViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
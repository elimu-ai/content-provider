package ai.elimu.content_provider.ui.letter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LettersViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public LettersViewModel() {
        text = new MutableLiveData<>();
        text.setValue("LettersViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
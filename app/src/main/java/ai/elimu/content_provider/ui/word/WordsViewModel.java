package ai.elimu.content_provider.ui.word;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WordsViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public WordsViewModel() {
        text = new MutableLiveData<>();
        text.setValue("WordsViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
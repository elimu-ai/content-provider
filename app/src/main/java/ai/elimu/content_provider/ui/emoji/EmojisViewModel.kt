package ai.elimu.content_provider.ui.emoji;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmojisViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public EmojisViewModel() {
        text = new MutableLiveData<>();
        text.setValue("EmojisViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
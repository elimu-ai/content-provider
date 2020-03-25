package ai.elimu.content_provider.ui.audio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AudiosViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public AudiosViewModel() {
        text = new MutableLiveData<>();
        text.setValue("AudiosViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
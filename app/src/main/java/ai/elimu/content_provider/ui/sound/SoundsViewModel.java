package ai.elimu.content_provider.ui.sound;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SoundsViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public SoundsViewModel() {
        text = new MutableLiveData<>();
        text.setValue("SoundsViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
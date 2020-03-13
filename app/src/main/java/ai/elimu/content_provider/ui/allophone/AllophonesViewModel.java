package ai.elimu.content_provider.ui.allophone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllophonesViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public AllophonesViewModel() {
        text = new MutableLiveData<>();
        text.setValue("AllophonesViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
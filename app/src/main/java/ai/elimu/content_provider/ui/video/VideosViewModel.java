package ai.elimu.content_provider.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideosViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public VideosViewModel() {
        text = new MutableLiveData<>();
        text.setValue("VideosViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
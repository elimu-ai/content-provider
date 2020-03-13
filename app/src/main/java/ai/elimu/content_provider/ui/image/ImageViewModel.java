package ai.elimu.content_provider.ui.image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public ImageViewModel() {
        text = new MutableLiveData<>();
        text.setValue("ImageViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
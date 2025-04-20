package ai.elimu.content_provider.ui.image;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImagesViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public ImagesViewModel() {
        text = new MutableLiveData<>();
        text.setValue("ImagesViewModel");
    }

    public LiveData<String> getText() {
        Log.i(getClass().getName(), "getText");

        return text;
    }
}
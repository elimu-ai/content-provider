package ai.elimu.content_provider.ui.storybook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoryBooksViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public StoryBooksViewModel() {
        text = new MutableLiveData<>();
        text.setValue("StoryBooksViewModel");
    }

    public LiveData<String> getText() {
        Log.i(getClass().getName(), "getText");

        return text;
    }
}
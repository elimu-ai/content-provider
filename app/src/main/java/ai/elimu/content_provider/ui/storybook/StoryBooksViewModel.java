package ai.elimu.content_provider.ui.storybook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoryBooksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StoryBooksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("StoryBooksViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
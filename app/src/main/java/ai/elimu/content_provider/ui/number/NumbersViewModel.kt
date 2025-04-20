package ai.elimu.content_provider.ui.number;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NumbersViewModel extends ViewModel {

    private MutableLiveData<String> text;

    public NumbersViewModel() {
        text = new MutableLiveData<>();
        text.setValue("NumbersViewModel");
    }

    public LiveData<String> getText() {
        return text;
    }
}
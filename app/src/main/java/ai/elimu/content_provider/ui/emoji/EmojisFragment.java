package ai.elimu.content_provider.ui.emoji;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ai.elimu.content_provider.R;

public class EmojisFragment extends Fragment {

    private EmojisViewModel emojisViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        emojisViewModel = new ViewModelProvider(this).get(EmojisViewModel.class);
        View root = inflater.inflate(R.layout.fragment_emojis, container, false);
        final TextView textView = root.findViewById(R.id.text_emojis);
        emojisViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i(getClass().getName(), "onChanged");
                textView.setText(s);
            }
        });
        return root;
    }
}
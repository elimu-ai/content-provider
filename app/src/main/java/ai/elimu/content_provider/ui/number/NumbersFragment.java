package ai.elimu.content_provider.ui.number;

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

public class NumbersFragment extends Fragment {

    private NumbersViewModel numbersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        numbersViewModel = new ViewModelProvider(this).get(NumbersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_numbers, container, false);
        final TextView textView = root.findViewById(R.id.text_numbers);
        numbersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i(getClass().getName(), "onChanged");
                textView.setText(s);
            }
        });
        return root;
    }
}
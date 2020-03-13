package ai.elimu.content_provider.ui.image;

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
import androidx.lifecycle.ViewModelProviders;

import ai.elimu.content_provider.R;

public class ImageFragment extends Fragment {

    private ImageViewModel imageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_letters, container, false);
        final TextView textView = root.findViewById(R.id.text_letters);
        imageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i(getClass().getName(), "onChanged");
                textView.setText(s);
            }
        });
        return root;
    }
}
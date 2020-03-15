package ai.elimu.content_provider.ui.storybook;

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

import java.util.List;

import ai.elimu.content_provider.R;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.StoryBook;

public class StoryBooksFragment extends Fragment {

    private StoryBooksViewModel storyBooksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        storyBooksViewModel = ViewModelProviders.of(this).get(StoryBooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_storybooks, container, false);
        final TextView textView = root.findViewById(R.id.text_storybooks);
        storyBooksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i(getClass().getName(), "onChanged");
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        // Store dummy values in DB
        // TODO: download from REST API
        RoomDb roomDb = RoomDb.getDatabase(getContext());
        StoryBookDao storyBookDao = roomDb.storyBookDao();

        StoryBook storyBook1 = new StoryBook();
        storyBook1.setId(20L);
        storyBook1.setTitle("7 Colours of a Rainbow");
        storyBook1.setDescription("Look around! Do you see the seven colours of rainbow around you?");
        RoomDb.databaseWriteExecutor.execute(() -> {
            storyBookDao.insert(storyBook1);
        });

        StoryBook storyBook2 = new StoryBook();
        storyBook2.setId(21L);
        storyBook2.setTitle("A Day at the Carnival");
        storyBook2.setDescription("They ride toy cars, go on the Ferris wheel, and visit the balloon shop.");
        RoomDb.databaseWriteExecutor.execute(() -> {
            storyBookDao.insert(storyBook2);
        });

        // Update the UI
        RoomDb.databaseWriteExecutor.execute(() -> {
            List<StoryBook> storyBooks = storyBookDao.loadAll();
            Log.i(getClass().getName(), "storyBooks.size(): " + storyBooks.size());
            // TODO
        });
    }
}

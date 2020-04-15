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
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.BaseApplication;
import ai.elimu.content_provider.R;
import ai.elimu.content_provider.rest.StoryBooksService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.model.gson.v2.content.StoryBookGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoryBooksFragment extends Fragment {

    private StoryBooksViewModel storyBooksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        storyBooksViewModel = new ViewModelProvider(this).get(StoryBooksViewModel.class);
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

        // Download StoryBooks from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        StoryBooksService storyBooksService = retrofit.create(StoryBooksService.class);
        Call<List<StoryBookGson>> call = storyBooksService.listStoryBooks();
        Log.i(getClass().getName(), "call.request(): " + call.request());
        call.enqueue(new Callback<List<StoryBookGson>>() {

            @Override
            public void onResponse(Call<List<StoryBookGson>> call, Response<List<StoryBookGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<StoryBookGson> storyBookGsons = response.body();
                Log.i(getClass().getName(), "storyBookGsons.size(): " + storyBookGsons.size());

                if (storyBookGsons.size() > 1) {
                    processResponseBody(storyBookGsons);
                }
            }

            @Override
            public void onFailure(Call<List<StoryBookGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());
            }
        });
    }

    private void processResponseBody(List<StoryBookGson> storyBookGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                StoryBookDao storyBookDao = roomDb.storyBookDao();

                for (StoryBookGson storyBookGson : storyBookGsons) {
                    Log.i(getClass().getName(), "storyBookGson.getId(): " + storyBookGson.getId());

                    // Check if the StoryBook has already been stored in the database
                    StoryBook storyBook = storyBookDao.load(storyBookGson.getId());
                    Log.i(getClass().getName(), "storyBook: " + storyBook);
                    if (storyBook == null) {
                        // Store the new StoryBook in the database
                        storyBook = GsonToRoomConverter.getStoryBook(storyBookGson);
                        storyBookDao.insert(storyBook);
                        Log.i(getClass().getName(), "Stored StoryBook in database with ID " + storyBook.getId());
                    } else {
                        // Update the existing StoryBook in the database
                        storyBook = GsonToRoomConverter.getStoryBook(storyBookGson);
                        storyBookDao.update(storyBook);
                        Log.i(getClass().getName(), "Updated StoryBook in database with ID " + storyBook.getId());
                    }
                }

                // Update the UI
                List<StoryBook> storyBooks = storyBookDao.loadAll();
                Log.i(getClass().getName(), "storyBooks.size(): " + storyBooks.size());
//                storyBooksViewModel.getText().postValue("storyBooks.size(): " + storyBooks.size());
            }
        });
    }
}

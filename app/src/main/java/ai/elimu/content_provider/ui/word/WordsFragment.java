package ai.elimu.content_provider.ui.word;

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
import ai.elimu.content_provider.rest.WordsService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.WordDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Word;
import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WordsFragment extends Fragment {

    private WordsViewModel wordsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        wordsViewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_words, container, false);
        final TextView textView = root.findViewById(R.id.text_words);
        wordsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Words from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        WordsService wordsService = retrofit.create(WordsService.class);
        Call<List<WordGson>> wordGsonsCall = wordsService.listWords();
        Log.i(getClass().getName(), "wordGsonsCall.request(): " + wordGsonsCall.request());
        wordGsonsCall.enqueue(new Callback<List<WordGson>>() {

            @Override
            public void onResponse(Call<List<WordGson>> call, Response<List<WordGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<WordGson> wordGsons = response.body();
                Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());

                if (wordGsons.size() > 1) {
                    processResponseBody(wordGsons);
                }
            }

            @Override
            public void onFailure(Call<List<WordGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());
            }
        });
    }

    private void processResponseBody(List<WordGson> wordGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                WordDao wordDao = roomDb.wordDao();

                for (WordGson wordGson : wordGsons) {
                    Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());

                    // Check if the Word has already been stored in the database
                    Word word = wordDao.load(wordGson.getId());
                    Log.i(getClass().getName(), "word: " + word);
                    if (word == null) {
                        // Store the new Word in the database
                        word = GsonToRoomConverter.getWord(wordGson);
                        wordDao.insert(word);
                        Log.i(getClass().getName(), "Stored Word in database with ID " + word.getId());
                    } else if (word.getRevisionNumber() < wordGson.getRevisionNumber()) {
                        // Update the existing Word in the database
                        word = GsonToRoomConverter.getWord(wordGson);
                        wordDao.update(word);
                        Log.i(getClass().getName(), "Updated Word in database with ID " + word.getId());
                    }
                }

                // Update the UI
                List<Word> words = wordDao.loadAll();
                Log.i(getClass().getName(), "words.size(): " + words.size());
//                wordsViewModel.getText().postValue("words.size(): " + words.size());
            }
        });
    }
}
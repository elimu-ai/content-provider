package ai.elimu.content_provider.ui.emoji;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.BaseApplication;
import ai.elimu.content_provider.R;
import ai.elimu.content_provider.rest.EmojisService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.EmojiDao;
import ai.elimu.content_provider.room.dao.Emoji_WordDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Emoji;
import ai.elimu.content_provider.room.entity.Emoji_Word;
import ai.elimu.model.v2.gson.content.EmojiGson;
import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmojisFragment extends Fragment {

    private EmojisViewModel emojisViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        emojisViewModel = new ViewModelProvider(this).get(EmojisViewModel.class);
        View root = inflater.inflate(R.layout.fragment_emojis, container, false);
        progressBar = root.findViewById(R.id.progress_bar_emojis);
        textView = root.findViewById(R.id.text_emojis);
        emojisViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Emojis from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        EmojisService emojisService = retrofit.create(EmojisService.class);
        Call<List<EmojiGson>> emojiGsonsCall = emojisService.listEmojis();
        Log.i(getClass().getName(), "emojiGsonsCall.request(): " + emojiGsonsCall.request());
        emojiGsonsCall.enqueue(new Callback<List<EmojiGson>>() {

            @Override
            public void onResponse(Call<List<EmojiGson>> call, Response<List<EmojiGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<EmojiGson> emojiGsons = response.body();
                Log.i(getClass().getName(), "emojiGsons.size(): " + emojiGsons.size());

                if (emojiGsons.size() > 0) {
                    processResponseBody(emojiGsons);
                }
            }

            @Override
            public void onFailure(Call<List<EmojiGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());

                // Handle error
                Snackbar.make(textView, t.getCause().toString(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void processResponseBody(List<EmojiGson> emojiGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                EmojiDao emojiDao = roomDb.emojiDao();
                Emoji_WordDao emoji_WordDao = roomDb.emoji_WordDao();

                // Empty the database table before downloading up-to-date content
                emojiDao.deleteAll();
                emoji_WordDao.deleteAll();

                for (EmojiGson emojiGson : emojiGsons) {
                    Log.i(getClass().getName(), "emojiGson.getId(): " + emojiGson.getId());

                    // Store the Emoji in the database
                    Emoji emoji = GsonToRoomConverter.getEmoji(emojiGson);
                    emojiDao.insert(emoji);
                    Log.i(getClass().getName(), "Stored Emoji in database with ID " + emoji.getId());

                    // Store all the Emoji's Word labels in the database
                    Set<WordGson> wordGsons = emojiGson.getWords();
                    Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());
                    for (WordGson wordGson : wordGsons) {
                        Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());
                        Emoji_Word emoji_Word = new Emoji_Word();
                        emoji_Word.setEmoji_id(emojiGson.getId());
                        emoji_Word.setWords_id(wordGson.getId());
                        emoji_WordDao.insert(emoji_Word);
                        Log.i(getClass().getName(), "Stored Emoji_Word in database. Emoji_id: " + emoji_Word.getEmoji_id() + ", words_id: " + emoji_Word.getWords_id());
                    }
                }

                // Update the UI
                List<Emoji> emojis = emojiDao.loadAll();
                Log.i(getClass().getName(), "emojis.size(): " + emojis.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("emojis.size(): " + emojis.size());
                    Snackbar.make(textView, "emojis.size(): " + emojis.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

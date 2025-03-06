package ai.elimu.content_provider.ui.letter;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.BaseApplication;
import ai.elimu.content_provider.R;
import ai.elimu.content_provider.rest.LettersService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.LetterDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Letter;
import ai.elimu.model.v2.gson.content.LetterGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LettersFragment extends Fragment {

    private LettersViewModel lettersViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        lettersViewModel = new ViewModelProvider(this).get(LettersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_letters, container, false);
        progressBar = root.findViewById(R.id.progress_bar_letters);
        textView = root.findViewById(R.id.text_letters);
        lettersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Letters from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        LettersService lettersService = retrofit.create(LettersService.class);
        Call<List<LetterGson>> letterGsonsCall = lettersService.listLetters();
        Log.i(getClass().getName(), "letterGsonsCall.request(): " + letterGsonsCall.request());
        letterGsonsCall.enqueue(new Callback<List<LetterGson>>() {

            @Override
            public void onResponse(Call<List<LetterGson>> call, Response<List<LetterGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<LetterGson> letterGsons = response.body();
                    Log.i(getClass().getName(), "letterGsons.size(): " + letterGsons.size());

                    if (letterGsons.size() > 0) {
                        processResponseBody(letterGsons);
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView, response.toString(), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.deep_orange_darken_4))
                            .show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<LetterGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());

                // Handle error
                Snackbar.make(textView, t.getCause().toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.deep_orange_darken_4))
                        .show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void processResponseBody(List<LetterGson> letterGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                LetterDao letterDao = roomDb.letterDao();

                // Empty the database table before downloading up-to-date content
                letterDao.deleteAll();

                for (LetterGson letterGson : letterGsons) {
                    Log.i(getClass().getName(), "letterGson.getId(): " + letterGson.getId());

                    // Store the Letter in the database
                    Letter letter = GsonToRoomConverter.getLetter(letterGson);
                    letterDao.insert(letter);
                    Log.i(getClass().getName(), "Stored Letter in database with ID " + letter.getId());
                }

                // Update the UI
                List<Letter> letters = letterDao.loadAllOrderedByUsageCount();
                Log.i(getClass().getName(), "letters.size(): " + letters.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("letters.size(): " + letters.size());
                    Snackbar.make(textView, "letters.size(): " + letters.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

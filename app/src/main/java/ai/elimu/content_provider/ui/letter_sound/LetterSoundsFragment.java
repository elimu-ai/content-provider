package ai.elimu.content_provider.ui.letter_sound;

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
import ai.elimu.content_provider.rest.LetterSoundsService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.LetterSoundDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.LetterSound;
import ai.elimu.model.v2.gson.content.LetterSoundGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LetterSoundsFragment extends Fragment {

    private LetterSoundsViewModel letterSoundsViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        letterSoundsViewModel = new ViewModelProvider(this).get(LetterSoundsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_letter_sounds, container, false);
        progressBar = root.findViewById(R.id.progress_bar_letter_sounds);
        textView = root.findViewById(R.id.text_letter_sounds);
        letterSoundsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download LetterSounds from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        LetterSoundsService letterSoundsService = retrofit.create(LetterSoundsService.class);
        Call<List<LetterSoundGson>> letterSoundGsonsCall = letterSoundsService.listLetterSounds();
        Log.i(getClass().getName(), "letterSoundGsonsCall.request(): " + letterSoundGsonsCall.request());
        letterSoundGsonsCall.enqueue(new Callback<List<LetterSoundGson>>() {

            @Override
            public void onResponse(Call<List<LetterSoundGson>> call, Response<List<LetterSoundGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<LetterSoundGson> letterSoundGsons = response.body();
                    Log.i(getClass().getName(), "letterSoundGsons.size(): " + letterSoundGsons.size());

                    if (letterSoundGsons.size() > 0) {
                        processResponseBody(letterSoundGsons);
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
            public void onFailure(Call<List<LetterSoundGson>> call, Throwable t) {
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

    private void processResponseBody(List<LetterSoundGson> letterSoundGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                LetterSoundDao letterSoundDao = roomDb.letterSoundDao();

                // Empty the database table before downloading up-to-date content
                letterSoundDao.deleteAll();

                for (LetterSoundGson letterSoundGson : letterSoundGsons) {
                    Log.i(getClass().getName(), "letterSoundGson.getId(): " + letterSoundGson.getId());

                    // Store the LetterSound in the database
                    LetterSound letterSound = GsonToRoomConverter.getLetterSound(letterSoundGson);
                    letterSoundDao.insert(letterSound);
                    Log.i(getClass().getName(), "Stored LetterSound in database with ID " + letterSound.getId());
                }

                // Update the UI
                List<LetterSound> letterSounds = letterSoundDao.loadAll();
                Log.i(getClass().getName(), "letterSounds.size(): " + letterSounds.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("letterSounds.size(): " + letterSounds.size());
                    Snackbar.make(textView, "letterSounds.size(): " + letterSounds.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

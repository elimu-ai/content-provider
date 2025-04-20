package ai.elimu.content_provider.ui.sound;

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
import ai.elimu.content_provider.rest.SoundsService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.SoundDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Sound;
import ai.elimu.model.v2.gson.content.SoundGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SoundsFragment extends Fragment {

    private SoundsViewModel soundsViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        soundsViewModel = new ViewModelProvider(this).get(SoundsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sounds, container, false);
        progressBar = root.findViewById(R.id.progress_bar_sounds);
        textView = root.findViewById(R.id.text_sounds);
        soundsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Sounds from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        SoundsService soundsService = retrofit.create(SoundsService.class);
        Call<List<SoundGson>> soundGsonsCall = soundsService.listSounds();
        Log.i(getClass().getName(), "soundGsonsCall.request(): " + soundGsonsCall.request());
        soundGsonsCall.enqueue(new Callback<List<SoundGson>>() {

            @Override
            public void onResponse(Call<List<SoundGson>> call, Response<List<SoundGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<SoundGson> soundGsons = response.body();
                    Log.i(getClass().getName(), "soundGsons.size(): " + soundGsons.size());

                    if (soundGsons.size() > 0) {
                        processResponseBody(soundGsons);
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
            public void onFailure(Call<List<SoundGson>> call, Throwable t) {
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

    private void processResponseBody(List<SoundGson> soundGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                SoundDao soundDao = roomDb.soundDao();

                // Empty the database table before downloading up-to-date content
                soundDao.deleteAll();

                for (SoundGson soundGson : soundGsons) {
                    Log.i(getClass().getName(), "soundGson.getId(): " + soundGson.getId());

                    // Store the Sound in the database
                    Sound sound = GsonToRoomConverter.getSound(soundGson);
                    soundDao.insert(sound);
                    Log.i(getClass().getName(), "Stored Sound in database with ID " + sound.getId());
                }

                // Update the UI
                List<Sound> sounds = soundDao.loadAllOrderedByUsageCount();
                Log.i(getClass().getName(), "sounds.size(): " + sounds.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("sounds.size(): " + sounds.size());
                    Snackbar.make(textView, "sounds.size(): " + sounds.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

package ai.elimu.content_provider.ui.audio;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.BaseApplication;
import ai.elimu.content_provider.R;
import ai.elimu.content_provider.rest.AudiosService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.AudioDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Audio;
import ai.elimu.content_provider.util.FileHelper;
import ai.elimu.content_provider.util.MultimediaDownloader;
import ai.elimu.model.v2.gson.content.AudioGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AudiosFragment extends Fragment {

    private AudiosViewModel audiosViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        audiosViewModel = new ViewModelProvider(this).get(AudiosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_audios, container, false);
        progressBar = root.findViewById(R.id.progress_bar_audios);
        textView = root.findViewById(R.id.text_audios);
        audiosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Audios from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        AudiosService audiosService = retrofit.create(AudiosService.class);
        Call<List<AudioGson>> audioGsonsCall = audiosService.listAudios();
        Log.i(getClass().getName(), "audioGsonsCall.request(): " + audioGsonsCall.request());
        audioGsonsCall.enqueue(new Callback<List<AudioGson>>() {

            @Override
            public void onResponse(Call<List<AudioGson>> call, Response<List<AudioGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<AudioGson> audioGsons = response.body();
                    Log.i(getClass().getName(), "audioGsons.size(): " + audioGsons.size());

                    if (audioGsons.size() > 0) {
                        processResponseBody(audioGsons);
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
            public void onFailure(Call<List<AudioGson>> call, Throwable t) {
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

    private void processResponseBody(List<AudioGson> audioGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                AudioDao audioDao = roomDb.audioDao();

                // Empty the database table before downloading up-to-date content
                audioDao.deleteAll();
                // TODO: also delete corresponding audio files (only those that are no longer used)

                for (AudioGson audioGson : audioGsons) {
                    Log.i(getClass().getName(), "audioGson.getId(): " + audioGson.getId());

                    Audio audio = GsonToRoomConverter.getAudio(audioGson);

                    // Check if the corresponding audio file has already been downloaded
                    File audioFile = FileHelper.getAudioFile(audioGson, getContext());
                    Log.i(getClass().getName(), "audioFile: " + audioFile);
                    Log.i(getClass().getName(), "audioFile.exists(): " + audioFile.exists());
                    if (!audioFile.exists()) {
                        // Download file bytes
                        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
                        String downloadUrl = baseApplication.getBaseUrl() + audioGson.getBytesUrl();
                        Log.i(getClass().getName(), "downloadUrl: " + downloadUrl);
                        byte[] bytes = MultimediaDownloader.downloadFileBytes(downloadUrl);
                        Log.i(getClass().getName(), "bytes.length: " + bytes.length);

                        // Store the downloaded file in the external storage directory
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(audioFile);
                            fileOutputStream.write(bytes);
                        } catch (FileNotFoundException e) {
                            Log.e(getClass().getName(), null, e);
                        } catch (IOException e) {
                            Log.e(getClass().getName(), null, e);
                        }
                        Log.i(getClass().getName(), "audioFile.exists(): " + audioFile.exists());
                    }

                    // Store the Audio in the database
                    audioDao.insert(audio);
                    Log.i(getClass().getName(), "Stored Audio in database with ID " + audio.getId());
                }

                // Update the UI
                List<Audio> audios = audioDao.loadAll();
                Log.i(getClass().getName(), "audios.size(): " + audios.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("audios.size(): " + audios.size());
                    Snackbar.make(textView, "audios.size(): " + audios.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

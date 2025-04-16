package ai.elimu.content_provider.ui.video;

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
import ai.elimu.content_provider.rest.VideosService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.VideoDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Video;
import ai.elimu.content_provider.util.FileHelper;
import ai.elimu.content_provider.util.MultimediaDownloader;
import ai.elimu.model.v2.gson.content.VideoGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import ai.elimu.content_provider.room.dao.Video_WordDao;
//import ai.elimu.content_provider.room.entity.Video_Word;

public class VideosFragment extends Fragment {

    private VideosViewModel videosViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        videosViewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);
        progressBar = root.findViewById(R.id.progress_bar_videos);
        textView = root.findViewById(R.id.text_videos);
        videosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Videos from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        VideosService videosService = retrofit.create(VideosService.class);
        Call<List<VideoGson>> videoGsonsCall = videosService.listVideos();
        Log.i(getClass().getName(), "videoGsonsCall.request(): " + videoGsonsCall.request());
        videoGsonsCall.enqueue(new Callback<List<VideoGson>>() {

            @Override
            public void onResponse(Call<List<VideoGson>> call, Response<List<VideoGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<VideoGson> videoGsons = response.body();
                    Log.i(getClass().getName(), "videoGsons.size(): " + videoGsons.size());

                    if (videoGsons.size() > 0) {
                        processResponseBody(videoGsons);
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
            public void onFailure(Call<List<VideoGson>> call, Throwable t) {
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

    private void processResponseBody(List<VideoGson> videoGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                VideoDao videoDao = roomDb.videoDao();
//                Video_WordDao video_WordDao = roomDb.video_WordDao();

                // Empty the database table before downloading up-to-date content
//                video_WordDao.deleteAll();
                videoDao.deleteAll();
                // TODO: also delete corresponding video files (only those that are no longer used)

                for (VideoGson videoGson : videoGsons) {
                    Log.i(getClass().getName(), "videoGson.getId(): " + videoGson.getId());

                    Video video = GsonToRoomConverter.getVideo(videoGson);

                    // Check if the corresponding video file has already been downloaded
                    File videoFile = FileHelper.getVideoFile(videoGson, getContext());
                    Log.i(getClass().getName(), "videoFile: " + videoFile);
                    Log.i(getClass().getName(), "videoFile.exists(): " + videoFile.exists());
                    if (!videoFile.exists()) {
                        // Download file bytes
                        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
                        String fileUrl = baseApplication.getBaseUrl() + videoGson.getFileUrl();
                        Log.i(getClass().getName(), "fileUrl: " + fileUrl);
                        byte[] bytes = MultimediaDownloader.downloadFileBytes(fileUrl);
                        Log.i(getClass().getName(), "bytes.length: " + bytes.length);

                        // Store the downloaded file in the external storage directory
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(videoFile);
                            fileOutputStream.write(bytes);
                        } catch (FileNotFoundException e) {
                            Log.e(getClass().getName(), null, e);
                        } catch (IOException e) {
                            Log.e(getClass().getName(), null, e);
                        }
                        Log.i(getClass().getName(), "videoFile.exists(): " + videoFile.exists());
                    }

                    // Store the Video in the database
                    videoDao.insert(video);
                    Log.i(getClass().getName(), "Stored Video in database with ID " + video.getId());

//                    // Store all the Video's Word labels in the database
//                    Set<WordGson> wordGsons = videoGson.getWords();
//                    Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());
//                    for (WordGson wordGson : wordGsons) {
//                        Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());
//                        Video_Word video_Word = new Video_Word();
//                        video_Word.setVideo_id(videoGson.getId());
//                        video_Word.setWords_id(wordGson.getId());
//                        video_WordDao.insert(video_Word);
//                        Log.i(getClass().getName(), "Stored Video_Word in database. Video_id: " + video_Word.getVideo_id() + ", words_id: " + video_Word.getWords_id());
//                    }
                }

                // Update the UI
                List<Video> videos = videoDao.loadAll();
                Log.i(getClass().getName(), "videos.size(): " + videos.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("videos.size(): " + videos.size());
                    Snackbar.make(textView, "videos.size(): " + videos.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}
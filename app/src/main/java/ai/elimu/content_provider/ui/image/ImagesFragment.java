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
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.elimu.content_provider.BaseApplication;
import ai.elimu.content_provider.BuildConfig;
import ai.elimu.content_provider.R;
import ai.elimu.content_provider.rest.ImagesService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.ImageDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Image;
import ai.elimu.content_provider.util.MultimediaDownloader;
import ai.elimu.model.v2.gson.content.ImageGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImagesFragment extends Fragment {

    private ImagesViewModel imagesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        imagesViewModel = new ViewModelProvider(this).get(ImagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_letters, container, false);
        final TextView textView = root.findViewById(R.id.text_letters);
        imagesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Images from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        ImagesService imagesService = retrofit.create(ImagesService.class);
        Call<List<ImageGson>> imageGsonsCall = imagesService.listImages();
        Log.i(getClass().getName(), "imageGsonsCall.request(): " + imageGsonsCall.request());
        imageGsonsCall.enqueue(new Callback<List<ImageGson>>() {

            @Override
            public void onResponse(Call<List<ImageGson>> call, Response<List<ImageGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<ImageGson> imageGsons = response.body();
                Log.i(getClass().getName(), "imageGsons.size(): " + imageGsons.size());

                if (imageGsons.size() > 1) {
                    processResponseBody(imageGsons);
                }
            }

            @Override
            public void onFailure(Call<List<ImageGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());
            }
        });
    }

    private void processResponseBody(List<ImageGson> imageGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                ImageDao imageDao = roomDb.imageDao();

                for (ImageGson imageGson : imageGsons) {
                    Log.i(getClass().getName(), "imageGson.getId(): " + imageGson.getId());

                    // Check if the Image has already been stored in the database
                    Image image = imageDao.load(imageGson.getId());
                    Log.i(getClass().getName(), "image: " + image);
                    if (image == null) {
                        // Store the new Image in the database

                        image = GsonToRoomConverter.getImage(imageGson);

                        // Download bytes
                        String downloadUrl = BuildConfig.BASE_URL + imageGson.getDownloadUrl();
                        Log.i(getClass().getName(), "downloadUrl: " + downloadUrl);
                        byte[] bytes = MultimediaDownloader.downloadFileBytes(downloadUrl);
                        Log.i(getClass().getName(), "bytes.length: " + bytes.length);
                        image.setBytes(bytes);

                        imageDao.insert(image);
                        Log.i(getClass().getName(), "Stored Image in database with ID " + image.getId());
                    } else if (image.getRevisionNumber() < imageGson.getRevisionNumber()) {
                        // Update the existing Image in the database

                        image = GsonToRoomConverter.getImage(imageGson);

                        // Download bytes
                        String downloadUrl = BuildConfig.BASE_URL + imageGson.getDownloadUrl();
                        Log.i(getClass().getName(), "downloadUrl: " + downloadUrl);
                        byte[] bytes = MultimediaDownloader.downloadFileBytes(downloadUrl);
                        Log.i(getClass().getName(), "bytes.length: " + bytes.length);
                        image.setBytes(bytes);

                        imageDao.update(image);
                        Log.i(getClass().getName(), "Updated Image in database with ID " + image.getId());
                    }
                }

                // Update the UI
                List<Image> images = imageDao.loadAll();
                Log.i(getClass().getName(), "images.size(): " + images.size());
//                imagesViewModel.getText().postValue("images.size(): " + images.size());
            }
        });
    }
}

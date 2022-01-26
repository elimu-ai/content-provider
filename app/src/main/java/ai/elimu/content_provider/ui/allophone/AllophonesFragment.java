package ai.elimu.content_provider.ui.allophone;

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
import ai.elimu.content_provider.room.dao.AllophoneDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Allophone;
import ai.elimu.model.v2.gson.content.SoundGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllophonesFragment extends Fragment {

    private AllophonesViewModel allophonesViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        allophonesViewModel = new ViewModelProvider(this).get(AllophonesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_allophones, container, false);
        progressBar = root.findViewById(R.id.progress_bar_allophones);
        textView = root.findViewById(R.id.text_allophones);
        allophonesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Allophones from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        SoundsService soundsService = retrofit.create(SoundsService.class);
        Call<List<SoundGson>> allophoneGsonsCall = soundsService.listAllophones();
        Log.i(getClass().getName(), "allophoneGsonsCall.request(): " + allophoneGsonsCall.request());
        allophoneGsonsCall.enqueue(new Callback<List<SoundGson>>() {

            @Override
            public void onResponse(Call<List<SoundGson>> call, Response<List<SoundGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<SoundGson> allophoneGsons = response.body();
                Log.i(getClass().getName(), "allophoneGsons.size(): " + allophoneGsons.size());

                if (allophoneGsons.size() > 0) {
                    processResponseBody(allophoneGsons);
                }
            }

            @Override
            public void onFailure(Call<List<SoundGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());

                // Handle error
                Snackbar.make(textView, t.getCause().toString(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void processResponseBody(List<SoundGson> allophoneGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                AllophoneDao allophoneDao = roomDb.allophoneDao();

                // Empty the database table before downloading up-to-date content
                allophoneDao.deleteAll();

                for (SoundGson allophoneGson : allophoneGsons) {
                    Log.i(getClass().getName(), "allophoneGson.getId(): " + allophoneGson.getId());

                    // Store the Allophone in the database
                    Allophone allophone = GsonToRoomConverter.getAllophone(allophoneGson);
                    allophoneDao.insert(allophone);
                    Log.i(getClass().getName(), "Stored Allophone in database with ID " + allophone.getId());
                }

                // Update the UI
                List<Allophone> allophones = allophoneDao.loadAllOrderedByUsageCount();
                Log.i(getClass().getName(), "allophones.size(): " + allophones.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("allophones.size(): " + allophones.size());
                    Snackbar.make(textView, "allophones.size(): " + allophones.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}

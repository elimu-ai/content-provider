package ai.elimu.content_provider.ui.number;

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
import ai.elimu.content_provider.rest.NumbersService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.NumberDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.Number;
import ai.elimu.model.v2.gson.content.NumberGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NumbersFragment extends Fragment {

    private NumbersViewModel numbersViewModel;

    private ProgressBar progressBar;

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        numbersViewModel = new ViewModelProvider(this).get(NumbersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_numbers, container, false);
        progressBar = root.findViewById(R.id.progress_bar_numbers);
        textView = root.findViewById(R.id.text_numbers);
        numbersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download Numbers from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        NumbersService numbersService = retrofit.create(NumbersService.class);
        Call<List<NumberGson>> numberGsonsCall = numbersService.listNumbers();
        Log.i(getClass().getName(), "numberGsonsCall.request(): " + numberGsonsCall.request());
        numberGsonsCall.enqueue(new Callback<List<NumberGson>>() {

            @Override
            public void onResponse(Call<List<NumberGson>> call, Response<List<NumberGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);
                if (response.isSuccessful()) {
                    List<NumberGson> numberGsons = response.body();
                    Log.i(getClass().getName(), "numberGsons.size(): " + numberGsons.size());

                    if (numberGsons.size() > 0) {
                        processResponseBody(numberGsons);
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
            public void onFailure(Call<List<NumberGson>> call, Throwable t) {
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

    private void processResponseBody(List<NumberGson> numberGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                NumberDao numberDao = roomDb.numberDao();

                // Empty the database table before downloading up-to-date content
                numberDao.deleteAll();

                for (NumberGson numberGson : numberGsons) {
                    Log.i(getClass().getName(), "numberGson.getId(): " + numberGson.getId());

                    // Store the Number in the database
                    Number number = GsonToRoomConverter.getNumber(numberGson);
                    numberDao.insert(number);
                    Log.i(getClass().getName(), "Stored Number in database with ID " + number.getId());
                }

                // Update the UI
                List<Number> numbers = numberDao.loadAllOrderedByValue();
                Log.i(getClass().getName(), "numbers.size(): " + numbers.size());
                getActivity().runOnUiThread(() -> {
                    textView.setText("numbers.size(): " + numbers.size());
                    Snackbar.make(textView, "numbers.size(): " + numbers.size(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}
package ai.elimu.content_provider.ui.letter;

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
import ai.elimu.content_provider.rest.LettersService;
import ai.elimu.model.v2.gson.content.LetterGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LettersFragment extends Fragment {

    private LettersViewModel lettersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        lettersViewModel = new ViewModelProvider(this).get(LettersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_letters, container, false);
        final TextView textView = root.findViewById(R.id.text_letters);
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

                List<LetterGson> letterGsons = response.body();
                Log.i(getClass().getName(), "letterGsons.size(): " + letterGsons.size());

                if (letterGsons.size() > 1) {
                    processResponseBody(letterGsons);
                }
            }

            @Override
            public void onFailure(Call<List<LetterGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());
            }
        });
    }

    private void processResponseBody(List<LetterGson> letterGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(getClass().getName(), "run");
//
//                RoomDb roomDb = RoomDb.getDatabase(getContext());
//                LetterDao letterDao = roomDb.letterDao();
//
//                for (LetterGson letterGson : letterGsons) {
//                    Log.i(getClass().getName(), "letterGson.getId(): " + letterGson.getId());
//
//                    // Check if the Letter has already been stored in the database
//                    Letter letter = letterDao.load(letterGson.getId());
//                    Log.i(getClass().getName(), "letter: " + letter);
//                    if (letter == null) {
//                        // Store the new Letter in the database
//                        letter = GsonToRoomConverter.getLetter(letterGson);
//                        letterDao.insert(letter);
//                        Log.i(getClass().getName(), "Stored Letter in database with ID " + letter.getId());
//                    } else {
//                        // Update the existing Letter in the database
//                        letter = GsonToRoomConverter.getLetter(letterGson);
//                        letterDao.update(letter);
//                        Log.i(getClass().getName(), "Updated Letter in database with ID " + letter.getId());
//                    }
//                }
//
//                // Update the UI
//                List<Letter> letters = letterDao.loadAllOrderedByUsageCount();
//                Log.i(getClass().getName(), "letters.size(): " + letters.size());
////                lettersViewModel.getText().postValue("letters.size(): " + letters.size());
//            }
//        });
    }
}
package ai.elimu.content_provider.ui.storybook;

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
import ai.elimu.content_provider.rest.StoryBooksService;
import ai.elimu.content_provider.room.GsonToRoomConverter;
import ai.elimu.content_provider.room.dao.StoryBookChapterDao;
import ai.elimu.content_provider.room.dao.StoryBookDao;
import ai.elimu.content_provider.room.dao.StoryBookParagraphDao;
import ai.elimu.content_provider.room.dao.StoryBookParagraph_WordDao;
import ai.elimu.content_provider.room.db.RoomDb;
import ai.elimu.content_provider.room.entity.StoryBook;
import ai.elimu.content_provider.room.entity.StoryBookChapter;
import ai.elimu.content_provider.room.entity.StoryBookParagraph;
import ai.elimu.content_provider.room.entity.StoryBookParagraph_Word;
import ai.elimu.model.v2.gson.content.StoryBookChapterGson;
import ai.elimu.model.v2.gson.content.StoryBookGson;
import ai.elimu.model.v2.gson.content.StoryBookParagraphGson;
import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoryBooksFragment extends Fragment {

    private StoryBooksViewModel storyBooksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");

        storyBooksViewModel = new ViewModelProvider(this).get(StoryBooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_storybooks, container, false);
        final TextView textView = root.findViewById(R.id.text_storybooks);
        storyBooksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        // Download StoryBooks from REST API, and store them in the database
        BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        StoryBooksService storyBooksService = retrofit.create(StoryBooksService.class);
        Call<List<StoryBookGson>> call = storyBooksService.listStoryBooks();
        Log.i(getClass().getName(), "call.request(): " + call.request());
        call.enqueue(new Callback<List<StoryBookGson>>() {

            @Override
            public void onResponse(Call<List<StoryBookGson>> call, Response<List<StoryBookGson>> response) {
                Log.i(getClass().getName(), "onResponse");

                Log.i(getClass().getName(), "response: " + response);

                List<StoryBookGson> storyBookGsons = response.body();
                Log.i(getClass().getName(), "storyBookGsons.size(): " + storyBookGsons.size());

                if (storyBookGsons.size() > 1) {
                    processResponseBody(storyBookGsons);
                }
            }

            @Override
            public void onFailure(Call<List<StoryBookGson>> call, Throwable t) {
                Log.e(getClass().getName(), "onFailure", t);

                Log.e(getClass().getName(), "t.getCause():", t.getCause());
            }
        });
    }

    private void processResponseBody(List<StoryBookGson> storyBookGsons) {
        Log.i(getClass().getName(), "processResponseBody");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(getClass().getName(), "run");

                RoomDb roomDb = RoomDb.getDatabase(getContext());
                StoryBookDao storyBookDao = roomDb.storyBookDao();
                StoryBookChapterDao storyBookChapterDao = roomDb.storyBookChapterDao();
                StoryBookParagraphDao storyBookParagraphDao = roomDb.storyBookParagraphDao();
                StoryBookParagraph_WordDao storyBookParagraph_WordDao = roomDb.storyBookParagraph_WordDao();

                for (StoryBookGson storyBookGson : storyBookGsons) {
                    Log.i(getClass().getName(), "storyBookGson.getId(): " + storyBookGson.getId());

                    // Check if the StoryBook has already been stored in the database
                    StoryBook storyBook = storyBookDao.load(storyBookGson.getId());
                    Log.i(getClass().getName(), "storyBook: " + storyBook);
                    if (storyBook == null) {
                        // Store the new StoryBook in the database
                        storyBook = GsonToRoomConverter.getStoryBook(storyBookGson);
                        storyBookDao.insert(storyBook);
                        Log.i(getClass().getName(), "Stored StoryBook in database with ID " + storyBook.getId());

                        List<StoryBookChapterGson> storyBookChapterGsons = storyBookGson.getStoryBookChapters();
                        Log.i(getClass().getName(), "storyBookChapterGsons.size(): " + storyBookChapterGsons.size());
                        for (StoryBookChapterGson storyBookChapterGson : storyBookChapterGsons) {
                            StoryBookChapter storyBookChapter = GsonToRoomConverter.getStoryBookChapter(storyBookChapterGson);
                            storyBookChapter.setStoryBookId(storyBookGson.getId());
                            storyBookChapterDao.insert(storyBookChapter);
                            Log.i(getClass().getName(), "Stored StoryBookChapter in database with ID " + storyBookChapter.getId());

                            List<StoryBookParagraphGson> storyBookParagraphs = storyBookChapterGson.getStoryBookParagraphs();
                            Log.i(getClass().getName(), "storyBookParagraphs.size(): " + storyBookParagraphs.size());
                            for (StoryBookParagraphGson storyBookParagraphGson : storyBookParagraphs) {
                                StoryBookParagraph storyBookParagraph = GsonToRoomConverter.getStoryBookParagraph(storyBookParagraphGson);
                                storyBookParagraph.setStoryBookChapterId(storyBookChapterGson.getId());
                                storyBookParagraphDao.insert(storyBookParagraph);
                                Log.i(getClass().getName(), "Stored StoryBookParagraph in database with ID " + storyBookParagraph.getId());

                                // Store all the StoryBookParagraph's Words in the database
                                List<WordGson> wordGsons = storyBookParagraphGson.getWords();
                                Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());
                                for (int i = 0; i < wordGsons.size(); i++) {
                                    WordGson wordGson = wordGsons.get(i);
                                    Log.i(getClass().getName(), "wordGson: " + wordGson);
                                    if (wordGson != null) {
                                        Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());
                                        StoryBookParagraph_Word storyBookParagraph_Word = new StoryBookParagraph_Word();
                                        storyBookParagraph_Word.setStoryBookParagraph_id(storyBookParagraphGson.getId());
                                        storyBookParagraph_Word.setWords_id(wordGson.getId());
                                        storyBookParagraph_Word.setWords_ORDER(i);
                                        storyBookParagraph_WordDao.insert(storyBookParagraph_Word);
                                        Log.i(getClass().getName(), "Stored StoryBookParagraph_Word in database. StoryBookParagraph_id: " + storyBookParagraph_Word.getStoryBookParagraph_id() + ", words_id: " + storyBookParagraph_Word.getWords_id() + ", words_ORDER: " + storyBookParagraph_Word.getWords_ORDER());
                                    }
                                }
                            }
                        }
                    } else {
                        // Update the existing StoryBook in the database
                        storyBook = GsonToRoomConverter.getStoryBook(storyBookGson);
                        storyBookDao.update(storyBook);
                        Log.i(getClass().getName(), "Updated StoryBook in database with ID " + storyBook.getId());

                        List<StoryBookChapterGson> storyBookChapterGsons = storyBookGson.getStoryBookChapters();
                        Log.i(getClass().getName(), "storyBookChapterGsons.size(): " + storyBookChapterGsons.size());
                        for (StoryBookChapterGson storyBookChapterGson : storyBookChapterGsons) {
                            StoryBookChapter storyBookChapter = GsonToRoomConverter.getStoryBookChapter(storyBookChapterGson);
                            storyBookChapter.setStoryBookId(storyBookGson.getId());
                            storyBookChapterDao.update(storyBookChapter);
                            Log.i(getClass().getName(), "Updated StoryBookChapter in database with ID " + storyBookChapter.getId());

                            List<StoryBookParagraphGson> storyBookParagraphs = storyBookChapterGson.getStoryBookParagraphs();
                            Log.i(getClass().getName(), "storyBookParagraphs.size(): " + storyBookParagraphs.size());
                            for (StoryBookParagraphGson storyBookParagraphGson : storyBookParagraphs) {
                                StoryBookParagraph storyBookParagraph = GsonToRoomConverter.getStoryBookParagraph(storyBookParagraphGson);
                                storyBookParagraph.setStoryBookChapterId(storyBookChapterGson.getId());
                                storyBookParagraphDao.update(storyBookParagraph);
                                Log.i(getClass().getName(), "Updated StoryBookParagraph in database with ID " + storyBookParagraph.getId());

                                // Delete all the StoryBookParagraph's Words (in case changes have been made on the server-side)
                                storyBookParagraph_WordDao.delete(storyBookParagraph.getId());

                                // Store all the StoryBookParagraph's Words in the database
                                List<WordGson> wordGsons = storyBookParagraphGson.getWords();
                                Log.i(getClass().getName(), "wordGsons.size(): " + wordGsons.size());
                                for (int i = 0; i < wordGsons.size(); i++) {
                                    WordGson wordGson = wordGsons.get(i);
                                    Log.i(getClass().getName(), "wordGson: " + wordGson);
                                    if (wordGson != null) {
                                        Log.i(getClass().getName(), "wordGson.getId(): " + wordGson.getId());
                                        StoryBookParagraph_Word storyBookParagraph_Word = new StoryBookParagraph_Word();
                                        storyBookParagraph_Word.setStoryBookParagraph_id(storyBookParagraphGson.getId());
                                        storyBookParagraph_Word.setWords_id(wordGson.getId());
                                        storyBookParagraph_Word.setWords_ORDER(i);
                                        storyBookParagraph_WordDao.insert(storyBookParagraph_Word);
                                        Log.i(getClass().getName(), "Stored StoryBookParagraph_Word in database. StoryBookParagraph_id: " + storyBookParagraph_Word.getStoryBookParagraph_id() + ", words_id: " + storyBookParagraph_Word.getWords_id() + ", words_ORDER: " + storyBookParagraph_Word.getWords_ORDER());
                                    }
                                }
                            }
                        }
                    }
                }

                // Update the UI
                List<StoryBook> storyBooks = storyBookDao.loadAll();
                Log.i(getClass().getName(), "storyBooks.size(): " + storyBooks.size());
//                storyBooksViewModel.getText().postValue("storyBooks.size(): " + storyBooks.size());
            }
        });
    }
}

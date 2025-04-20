package ai.elimu.content_provider.ui.number

import ai.elimu.content_provider.BaseApplication
import ai.elimu.content_provider.R
import ai.elimu.content_provider.rest.NumbersService
import ai.elimu.content_provider.room.GsonToRoomConverter.getNumber
import ai.elimu.content_provider.room.db.RoomDb
import ai.elimu.model.v2.gson.content.NumberGson
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class NumbersFragment : Fragment() {
    private var numbersViewModel: NumbersViewModel? = null

    private var progressBar: ProgressBar? = null

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        numbersViewModel = ViewModelProvider(this).get(
            NumbersViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_numbers, container, false)
        progressBar = root.findViewById(R.id.progress_bar_numbers)
        textView = root.findViewById(R.id.text_numbers)
        numbersViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(javaClass.name, "onChanged")
                textView?.text = s
            }
        })
        return root
    }

    override fun onStart() {
        Log.i(javaClass.name, "onStart")
        super.onStart()

        // Download Numbers from REST API, and store them in the database
        val baseApplication = activity!!.application as BaseApplication
        val retrofit = baseApplication.retrofit
        val numbersService = retrofit.create(NumbersService::class.java)
        val numberGsonsCall = numbersService.listNumbers()
        Log.i(javaClass.name, "numberGsonsCall.request(): " + numberGsonsCall.request())
        numberGsonsCall.enqueue(object : Callback<List<NumberGson>> {
            override fun onResponse(
                call: Call<List<NumberGson>>,
                response: Response<List<NumberGson>>
            ) {
                Log.i(javaClass.name, "onResponse")

                Log.i(javaClass.name, "response: $response")
                if (response.isSuccessful) {
                    val numberGsons = response.body()!!
                    Log.i(javaClass.name, "numberGsons.size(): " + numberGsons.size)

                    if (numberGsons.size > 0) {
                        processResponseBody(numberGsons)
                    } else {
                        progressBar?.visibility = View.GONE
                        textView?.text = getString(R.string.numbers_size, 0)
                    }
                } else {
                    // Handle error
                    Snackbar.make(textView!!, response.toString(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<NumberGson>>, t: Throwable) {
                Log.e(javaClass.name, "onFailure", t)

                Log.e(javaClass.name, "t.getCause():", t.cause)

                // Handle error
                Snackbar.make(textView!!, t.cause.toString(), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(R.color.deep_orange_darken_4))
                    .show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun processResponseBody(numberGsons: List<NumberGson>) {
        Log.i(javaClass.name, "processResponseBody")

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(object : Runnable {
            override fun run() {
                Log.i(javaClass.name, "run")

                val roomDb = RoomDb.getDatabase(context)
                val numberDao = roomDb.numberDao()

                // Empty the database table before downloading up-to-date content
                numberDao.deleteAll()

                for (numberGson in numberGsons) {
                    Log.i(javaClass.name, "numberGson.getId(): " + numberGson.id)

                    // Store the Number in the database
                    val number = getNumber(numberGson)
                    numberDao.insert(number)
                    Log.i(javaClass.name, "Stored Number in database with ID " + number!!.id)
                }

                // Update the UI
                val numbers = numberDao.loadAllOrderedByValue()
                Log.i(javaClass.name, "numbers.size(): " + numbers.size)
                activity!!.runOnUiThread {
                    textView?.text = getString(R.string.numbers_size, numbers.size)
                    Snackbar.make(
                        textView!!,
                        "numbers.size(): " + numbers.size,
                        Snackbar.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })
    }
}
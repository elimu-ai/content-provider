package ai.elimu.content_provider.ui.home

import ai.elimu.content_provider.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {
    private var homeViewModel: HomeViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(javaClass.name, "onCreateView")

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView = root.findViewById<TextView>(R.id.text_home)
        homeViewModel!!.text.observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(s: String?) {
                Log.i(javaClass.name, "onChanged")
                textView.text = s
            }
        })
        return root
    }
}
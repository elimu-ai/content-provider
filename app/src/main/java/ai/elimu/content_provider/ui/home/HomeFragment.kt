package ai.elimu.content_provider.ui.home

import ai.elimu.content_provider.databinding.FragmentHomeBinding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(javaClass.name, "onCreateView")

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel.getText().observe(viewLifecycleOwner, object : Observer<String?> {
            override fun onChanged(value: String?) {
                Log.i(javaClass.name, "onChanged")
                binding.textHome.text = value
            }
        })
        return binding.root
    }
}
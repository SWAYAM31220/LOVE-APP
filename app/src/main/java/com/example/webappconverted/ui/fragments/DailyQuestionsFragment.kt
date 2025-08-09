package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.data.prefs.PrefsManager
import com.example.webappconverted.databinding.FragmentDailyquestionsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Translated from dailyquestions.html -> uses Supabase REST via Retrofit in real app (stubbed in this skeleton)
class DailyQuestionsFragment : Fragment() {
    private var _binding: FragmentDailyquestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyquestionsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        binding.saveBtn.setOnClickListener {
            val q1 = binding.q1.text.toString().trim()
            val q2 = binding.q2.text.toString().trim()
            val q3 = binding.q3.text.toString().trim()
            if (q1.isEmpty() || q2.isEmpty() || q3.isEmpty()) {
                binding.statusMsg.text = "Please fill all fields"
                return@setOnClickListener
            }
            // Save locally as simple example; network code mapped to Supabase in network package
            CoroutineScope(Dispatchers.IO).launch {
                // TODO: call repository to upload to Supabase
                requireActivity().runOnUiThread { binding.statusMsg.text = "Saved successfully 💞" }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

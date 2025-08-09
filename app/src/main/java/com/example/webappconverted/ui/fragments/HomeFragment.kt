package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.R
import com.example.webappconverted.data.prefs.PrefsManager
import com.example.webappconverted.databinding.FragmentHomeBinding

// Translated from home.html
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHomeBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        val user = prefs.getCurrentUser() ?: run { requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, LoginFragment()).commit(); return }
        binding.usernameDisplay.text = if (user=="him") "Him 💙" else "Her 💖"

        binding.dailyQuestionsBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, DailyQuestionsFragment()).commit()
        }
        binding.truthDareBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, TruthDareFragment()).commit()
        }
        binding.countdownBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, CountdownFragment()).commit()
        }
        binding.secretBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SecretFragment()).commit()
        }
        binding.chatBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ChatFragment()).commit()
        }
        binding.logoutBtn.setOnClickListener {
            prefs.clear()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, LoginFragment()).commit()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

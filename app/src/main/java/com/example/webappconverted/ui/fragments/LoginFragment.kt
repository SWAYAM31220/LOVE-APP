package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.R
import com.example.webappconverted.data.prefs.PrefsManager
import com.example.webappconverted.databinding.FragmentLoginBinding

// Translated from index.html and script.js (login logic)
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        prefs = PrefsManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginForm.setOnClickListener {
            val username = binding.username.text.toString().trim().lowercase()
            val password = binding.password.text.toString().trim()
            val users = mapOf("him" to "loveher", "her" to "lovehim")
            if (users[username] == password) {
                prefs.setCurrentUser(username)
                // navigate to home
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, com.example.webappconverted.ui.fragments.HomeFragment())
                    .commit()
            } else {
                android.widget.Toast.makeText(requireContext(), "Incorrect credentials", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}

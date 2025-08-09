package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.databinding.FragmentChatBinding
import com.example.webappconverted.data.prefs.PrefsManager

// Chat translated from chat.html and chat.js using Firebase Realtime Database (Firebase Android SDK)
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentChatBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        binding.logoutBtn.setOnClickListener {
            prefs.clear()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, com.example.webappconverted.ui.fragments.LoginFragment()).commit()
        }
        binding.chatForm.setOnClickListener {
            val msg = binding.chatInput.text.toString().trim()
            if (msg.isNotEmpty()) {
                // Sending using FirebaseChatService
                try {
                    val db = com.google.firebase.database.FirebaseDatabase.getInstance()
                    val currentUser = com.example.webappconverted.data.prefs.PrefsManager(requireContext()).getCurrentUser() ?: "him"
                    val partner = if (currentUser=="him") "her" else "him"
                    val svc = com.example.webappconverted.services.FirebaseChatService(db, currentUser, partner)
                    // send message asynchronously
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch { svc.sendMessage(msg) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                binding.chatInput.text?.clear()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

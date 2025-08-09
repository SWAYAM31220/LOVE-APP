package com.example.webappconverted.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.webappconverted.databinding.FragmentSecretBinding
import com.example.webappconverted.data.prefs.PrefsManager

// Translated from secret.html - upload handled via Supabase in network layer (stubbed here)
class SecretFragment : Fragment() {
    private var _binding: FragmentSecretBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE = 1001
    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSecretBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        binding.uploadForm.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE)
        }
        binding.logoutBtn.setOnClickListener {
            prefs.clear()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, com.example.webappconverted.ui.fragments.LoginFragment()).commit()
        }
        // load mock gallery
        val sample = listOf("file:///android_asset/mock/secret1.jpg","file:///android_asset/mock/secret2.jpg")
        sample.forEach {
            val iv = android.widget.ImageView(requireContext())
            iv.layoutParams = android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,400)
            Glide.with(this).load(it).centerCrop().into(iv)
            binding.gallery.addView(iv)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            // TODO: upload via repository to Supabase storage
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

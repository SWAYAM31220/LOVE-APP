package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.databinding.FragmentTruthdareBinding
import com.example.webappconverted.data.prefs.PrefsManager

// Translated from truthdare.html
class TruthDareFragment : Fragment() {
    private var _binding: FragmentTruthdareBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsManager
    private var mode = ""
    private var currentTurn = "you"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentTruthdareBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        currentTurn = prefs.getTurn() ?: "you"
        updateTurnDisplay()

        binding.truthBtn.setOnClickListener { selectMode("truth") }
        binding.dareBtn.setOnClickListener { selectMode("dare") }
        binding.generateBtn.setOnClickListener { generate() }
        binding.doneBtn.setOnClickListener { switchTurn() }
    }

    private fun updateTurnDisplay() { binding.turnDisplay.text = if (currentTurn=="you") "Your Turn 💖" else "Partner's Turn 💞" }
    private fun selectMode(m: String) { mode = m; binding.questionText.text = "Mode: ${m.uppercase()} selected. Now choose category." }
    private fun generate() {
        if (mode.isEmpty()) { android.widget.Toast.makeText(requireContext(),"Please select a mode first!",android.widget.Toast.LENGTH_SHORT).show(); return }
        val category = binding.categorySelect.selectedItem.toString().lowercase()
        // simplified questions hardcoded similar to web app
        val options = when(mode) {
            "truth" -> listOf("What do you love most about me?","Have you ever imagined our wedding?","What's your favorite memory with me?")
            else -> listOf("Write a poem for me right now!","Record a voice saying 'I love you' 5 times.","Hold my hand tightly for 60 seconds.")
        }
        binding.questionText.text = options.random()
    }
    private fun switchTurn() {
        currentTurn = if (currentTurn=="you") "partner" else "you"
        prefs.setTurn(currentTurn)
        mode = ""
        binding.questionText.text = "Choose a mode and category to begin!"
        updateTurnDisplay()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

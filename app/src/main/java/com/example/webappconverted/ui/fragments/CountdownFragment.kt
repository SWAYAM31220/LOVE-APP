package com.example.webappconverted.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.webappconverted.databinding.FragmentCountdownBinding
import java.util.*

class CountdownFragment : Fragment() {
    private var _binding: FragmentCountdownBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private val anniversary = GregorianCalendar(2026, 6, 7).time // July 7, 2026
    private val birthdays = mapOf("him" to Pair("Her Birthday Countdown", GregorianCalendar(2025,7-1,4).time),
                                  "her" to Pair("His Birthday Countdown", GregorianCalendar(2026,3-1,18).time))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCountdownBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handler.post(object : Runnable {
            override fun run() {
                updateCountdown(binding.anniversary, anniversary)
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun updateCountdown(labelView: android.widget.TextView, target: Date) {
        val now = Date().time
        val diff = target.time - now
        if (diff < 0) {
            labelView.text = "🎉 It's today! 🎉"
            return
        }
        val days = diff / (1000*60*60*24)
        val hours = (diff / (1000*60*60)) % 24
        val minutes = (diff / (1000*60)) % 60
        val seconds = (diff / 1000) % 60
        labelView.text = "${days}d ${hours}h ${minutes}m ${seconds}s"
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

package com.mscarla2.ghostfinder.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mscarla2.ghostfinder.BuildConfig
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.databinding.FragmentArtistBinding
import com.mscarla2.ghostfinder.databinding.FragmentInfoBinding

class ArtistFragment : Fragment() , SharedPreferences.OnSharedPreferenceChangeListener{

    private var binding: FragmentArtistBinding? = null
    private var color: String = ""
    private var textColor: String = ""
    private var language: String = ""
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val artistFragmentBinding = FragmentArtistBinding.inflate(inflater, container, false)
        binding = artistFragmentBinding
        return artistFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            webview.webViewClient = WebViewClient()
            binding?.webview?.loadUrl("https://sventhole.itch.io/")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}
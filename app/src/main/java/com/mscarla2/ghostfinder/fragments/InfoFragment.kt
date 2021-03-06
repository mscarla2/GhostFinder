package com.mscarla2.ghostfinder.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import com.mscarla2.ghostfinder.BuildConfig
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.databinding.FragmentInfoBinding


class InfoFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var color: String = ""
    private var textColor: String = ""
    private var language: String = ""
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private var binding: FragmentInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infoFragmentBinding = FragmentInfoBinding.inflate(inflater, container, false)
        binding = infoFragmentBinding
        return infoFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            appnameTextView.text = resources.getString(R.string.app_name)
            versionTextView.text = BuildConfig.VERSION_NAME
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}
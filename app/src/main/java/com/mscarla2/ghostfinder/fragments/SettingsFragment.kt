package com.mscarla2.ghostfinder.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {


    private var binding: FragmentSettingsBinding? = null
    private var color: String = ""
    private var textColor: String = ""
    private var language: String = ""
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingsFragmentBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding = settingsFragmentBinding
        binding?.apply {
            saveSettingsButton.setOnClickListener {

                languageRadioGroup.setOnCheckedChangeListener { _, id ->
                    language = when (id) {
                        R.id.english_radio_button -> "en"
                        R.id.french_radio_button -> "fr"
                        else -> null.toString()
                    }
                }
                colorRadioGroup.setOnCheckedChangeListener { _, id ->
                    color = when (id) {
                        R.id.blue_radio_button -> "Blue"
                        R.id.red_radio_button -> "Red"
                        R.id.purple_radio_button -> "Purple"
                        R.id.white_radio_button -> "White"
                        else -> null.toString()
                    }
                }
                fontRadioGroup.setOnCheckedChangeListener { _, id ->
                    textColor = when (id) {
                        R.id.redtext_radio_button -> "Red"
                        R.id.blacktext_radio_button -> "Black"
                        else -> null.toString()
                    }
                }
                with(prefs.edit()) {
                    putString("BACKGROUND_COLOR", color)
                    putString("TEXT_COLOR", textColor)
                    putString("LANGUAGE_SELECTION", language)
                    apply()
                }
                findNavController().navigate(R.id.action_settingsFragment_to_mainFragment)
            }
        }
        return settingsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "BACKGROUND_COLOR", "TEXT_COLOR", "LANGUAGE_SELECTION" -> {
                prefs.apply {
                    color = getString("BACKGROUND_COLOR", "White").toString()
                    textColor = getString("TEXT_COLOR", "Black").toString()
                    language = getString("LANGUAGE_SELECTION", "en").toString()
                    context?.toast("$color $textColor")
                }
            }
        }
    }
}
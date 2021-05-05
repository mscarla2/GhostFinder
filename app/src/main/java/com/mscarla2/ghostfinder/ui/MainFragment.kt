package com.mscarla2.ghostfinder.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.databinding.MainFragmentBinding
import com.mscarla2.ghostfinder.fragments.toast


class MainFragment : Fragment(){

    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: MainFragmentBinding? = null
    private var color: String? = ""
    private var textColor: String? = ""
    private var language: String? = ""
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        prefs.apply{
            color = prefs.getString("BACKGROUND_COLOR", "White")
            textColor = getString("TEXT_COLOR", "Black")
            language = getString("LANGUAGE_SELECTION", "en")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingMain = MainFragmentBinding.inflate(inflater, container, false)
        binding = bindingMain
        binding?.apply {
            toMenuButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_characterFragment)
            }
        }
        return bindingMain.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}


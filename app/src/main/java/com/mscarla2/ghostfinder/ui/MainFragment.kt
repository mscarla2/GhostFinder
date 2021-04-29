package com.mscarla2.ghostfinder.ui

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.FragmentCharacterBinding
import com.mscarla2.ghostfinder.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: MainFragmentBinding? = null
//    private var selectedPlayer : Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bindingMain = MainFragmentBinding.inflate(inflater, container, false)
        binding = bindingMain
        binding?.apply {
            settingsButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            }
            playerButton.setOnClickListener {
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


package com.mscarla2.ghostfinder.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.FragmentCharacterBinding
import com.mscarla2.ghostfinder.databinding.FragmentGameBinding
import com.mscarla2.ghostfinder.ui.CharacterAdapter
import com.mscarla2.ghostfinder.ui.MainViewModel

class GameFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var playerAdapter = CharacterAdapter()
    private var player: Array<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        _binding?.apply {
            backMenuButton.setOnClickListener {
                if(player != null){
                    val playedPlayer = GameFragmentDirections.actionGameFragmentToCharacterFragment(
                        player!!
                    )
                    Navigation.findNavController(it).navigate(playedPlayer)
                }
                else {
                    findNavController().navigate(R.id.action_gameFragment_to_characterFragment)
                }
            }
            backSettingsButton.setOnClickListener {
                findNavController().navigate(R.id.action_gameFragment_to_settingsFragment)
            }
            attackButton.setOnClickListener {
                player!![2] = ((player!![2].toInt()) + 1).toString()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val safeArgs = GameFragmentArgs.fromBundle(args)
            player = safeArgs.player
            context?.toast("${player!![0].toLong()}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

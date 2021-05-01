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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.FragmentCharacterBinding
import com.mscarla2.ghostfinder.ui.CharacterAdapter
import com.mscarla2.ghostfinder.ui.MainViewModel


class CharacterFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentCharacterBinding? = null
    private val playerAdapter = CharacterAdapter()
    private var selectedPlayer : Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bindingMain = FragmentCharacterBinding.inflate(inflater, container, false)
        binding = bindingMain
        binding?.apply {
            addPlayerButton.setOnClickListener {
                findNavController().navigate(R.id.action_characterFragment_to_dataEntryFragment)
            }
            deletePlayerButton.setOnClickListener {
                selectedPlayer = playerAdapter.getSelectedPlayer()
                if(selectedPlayer == null){
                    //TODO: Make R id
                    context?.toast("Select a player to delete")
                }
                else{
                    playerDeletedAlert(selectedPlayer!!)
                    selectedPlayer = null
                }
            }
            menuButton.setOnClickListener {
                findNavController().navigate(R.id.action_characterFragment_to_mainFragment)
            }
            gameButton.setOnClickListener {
                selectedPlayer = playerAdapter.getSelectedPlayer()
                var player = arrayOf(
                    selectedPlayer!!.name,
                    selectedPlayer!!.pow,
                    selectedPlayer!!.agi,
                    selectedPlayer!!.def,
                    selectedPlayer!!.name,
                    selectedPlayer!!.sword)

                if (selectedPlayer == null){
                    //TODO: Make R id
                    context?.toast("Select a player to play")
                }
                else {
                    val player = CharacterFragmentDirections.actionCharacterFragmentToGameFragment(
                        player
                    )
                    Navigation.findNavController(it).navigate(player)
                }
            }
            playerRecyclerview.run {
                layoutManager = LinearLayoutManager(context)
                adapter = playerAdapter
            }

        }
        return bindingMain.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel.players.observe(viewLifecycleOwner, {
            playerAdapter.updatePlayers(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun playerDeletedAlert(player: Player) {
        val msg = resources.getString(R.string.player_delete_alert)
        val builder = AlertDialog.Builder(context)
        with(builder) {
            builder.setCancelable(false)
            setTitle("Warning")
            setMessage(msg)
            setIcon(R.drawable.ic_baseline_notifications_active_24)
            setPositiveButton(R.string.ok) { _, _ ->
                sharedViewModel.deletePlayer(player)
                context?.toast("Player has been deleted")
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                playerAdapter.notifyDataSetChanged()
            }
            show()
        }
    }

}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}
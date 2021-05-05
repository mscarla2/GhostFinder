package com.mscarla2.ghostfinder.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.FragmentCharacterBinding
import com.mscarla2.ghostfinder.ui.CharacterAdapter
import com.mscarla2.ghostfinder.ui.MainViewModel


class CharacterFragment : Fragment() , SharedPreferences.OnSharedPreferenceChangeListener{
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentCharacterBinding? = null
    private val playerAdapter = CharacterAdapter()
    private var selectedPlayer : Player? = null
    private var prevPlayer: Player? = Player()
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

                    context?.toast(resources.getString(R.string.select_player_to_delete))
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

                if (selectedPlayer == null){
                    context?.toast(resources.getString(R.string.select_player_to_play))
                }
                else {
                    var player: Array<String> = arrayOf(
                        selectedPlayer!!.id.toString(),
                        selectedPlayer!!.name,
                        selectedPlayer!!.level.toString(),
                        selectedPlayer!!.hp.toString(),
                        selectedPlayer!!.pow.toString(),
                        selectedPlayer!!.agi.toString(),
                        selectedPlayer!!.def.toString(),
                        selectedPlayer!!.sword)
                    val playerInfo = CharacterFragmentDirections.actionCharacterFragmentToGameFragment(
                        player
                    )
                    Navigation.findNavController(it).navigate(playerInfo)
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
        arguments?.let { args ->
            if(requireArguments().isEmpty){
                prevPlayer = null
            }
            else{
                val safeArgs = CharacterFragmentArgs.fromBundle(args)
                prevPlayer!!.id = safeArgs.playedPlayer[0].toLong()
                prevPlayer!!.name = safeArgs.playedPlayer[1]
                prevPlayer!!.level = safeArgs.playedPlayer[2].toInt()
                prevPlayer!!.hp = safeArgs.playedPlayer[3].toInt()
                prevPlayer!!.pow = safeArgs.playedPlayer[4].toInt()
                prevPlayer!!.agi = safeArgs.playedPlayer[5].toInt()
                prevPlayer!!.def = safeArgs.playedPlayer[6].toInt()
                prevPlayer!!.sword = safeArgs.playedPlayer[7]
                sharedViewModel.update(prevPlayer!!)

            }
        }
        sharedViewModel.players.observe(viewLifecycleOwner, {
            playerAdapter.updatePlayers(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
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
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}
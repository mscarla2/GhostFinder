package com.mscarla2.ghostfinder.fragments

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.ui.CharacterAdapter
import com.mscarla2.ghostfinder.ui.MainViewModel

class DialogFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()
    private val playerAdapter = CharacterAdapter()

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
    fun insufficientSkillPoint() {
        val msg = resources.getString(R.string.insufficient_skillpoint)
        val builder = AlertDialog.Builder(context)
        with(builder) {
            builder.setCancelable(false)
            setTitle("Alert")
            setMessage(msg)
            setIcon(R.drawable.ic_baseline_notifications_active_24)
            setPositiveButton(R.string.ok, null)
            show()
        }
    }
    fun insufficientResource(type: String) {
        val msg = resources.getString(R.string.insufficient_resource, type)
        val builder = AlertDialog.Builder(context)
        with(builder) {
            builder.setCancelable(false)
            setTitle("Alert")
            setMessage(msg)
            setIcon(R.drawable.ic_baseline_notifications_active_24)
            setPositiveButton(R.string.ok, null)
            show()
        }
    }
}

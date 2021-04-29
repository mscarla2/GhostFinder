package com.mscarla2.ghostfinder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.RecyclerviewItemBinding


class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {
    private var players: List<Player> = emptyList()
    private var selectedPlayerPos: Int = Int.MAX_VALUE
    class CharacterViewHolder(private val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        private lateinit var player: Player
        fun bind(player: Player){
            this.player = player
            binding.apply{
                levelTextview.text = player.level.toString()
                nameTextview.text = player.name
            }
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(players[position])
        holder.itemView.setOnClickListener {
            selectedPlayerPos = holder.adapterPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharacterViewHolder(
        RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = players.size

    fun updatePlayers(newPlayers: List<Player>) {
        this.players = newPlayers
        notifyDataSetChanged()
    }

    fun getPlayerAtPosition(position: Int): Player {
        return players[position]
    }
    fun getSelectedPlayer(): Player? {
        if (players.size <= selectedPlayerPos){
            return null
        }
        else{
            return players[selectedPlayerPos]
        }
    }
}
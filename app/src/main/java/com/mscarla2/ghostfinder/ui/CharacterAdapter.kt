package com.mscarla2.ghostfinder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mscarla2.ghostfinder.databinding.RecyclerviewItemBinding


class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {
    private var characters: List<Character> = emptyList()
    private var selectedCharPos: Int = Int.MAX_VALUE
    class CharacterViewHolder(private val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        private lateinit var character: Character
        fun bind(character: Character){
            this.character = character
            binding.apply{
//
            }
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
        holder.itemView.setOnClickListener {
            selectedCharPos = holder.adapterPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharacterViewHolder(
        RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = characters.size

    fun updateCharacter(newCharacters: List<Character>) {
        this.characters = newCharacters
        notifyDataSetChanged()
    }

    fun getCharacterAtPosition(position: Int): Character {
        return characters[position]
    }
    fun getSelectedCharacter(): Character? {
        if (characters.size <= selectedCharPos){
            return null
        }
        else{
            return characters[selectedCharPos]
        }
    }
}
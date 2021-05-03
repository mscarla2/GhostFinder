package com.mscarla2.ghostfinder.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
import java.lang.Math.floor

class GameFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var playerAdapter = CharacterAdapter()
    private var player: Array<String>? = null
    private var pow: Int = 0
    private var agi: Int = 0
    private var def: Int = 0
    private var HP: Int = 0
    private var name: String? = null
    private var level: Int = 0
    private var sword: String? = null
    private var enemy_hp: Int = 0
    private var enemy_dmg: Int = 0
    private var enemy_level: Int = 0
    private var player_dmg: Int = 0
    private var sword_dmg = mapOf("wood_sword" to 1, "stone_sword" to 2, "diamond_sword" to 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        _binding?.apply {
            nextstageButton.isVisible = false
            nextstageButton.isEnabled = false

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
                animateAttack()
//                enemy_hp -= player_dmg
                if(enemy_hp <= 0){
                    ghostImageview.isVisible = false
                    nextstageButton.isVisible = true
                    nextstageButton.isEnabled = true
                    attackButton.isEnabled = false
                    binding.ghostHealthText.isVisible = false
                    binding.ghostHealthTextview.isVisible = false
//                    animateGhostDeath()
                }
//                else{
                    animateGhostAttack()
//                    if(HP <= 0){
//
//                    }
//                }
            }
            fleeButton.setOnClickListener{

            }
            nextstageButton.setOnClickListener{
                ghostGen()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val safeArgs = GameFragmentArgs.fromBundle(args)
            player = safeArgs.player
            name = player!![1]
            level = player!![2].toInt()
            HP = player!![3].toInt()
            pow = player!![4].toInt()
            agi = player!![5].toInt()
            def = player!![6].toInt()
            sword = player!![7]

            binding.gameNameText.text = name
            binding.gameHealthText.text = HP.toString()
            binding.gameLevelText.text = level.toString()
            binding.gamePowText.text = pow.toString()
            binding.gameAgiText.text = agi.toString()
            binding.gameDefText.text = def.toString()

            ghostGen()
            calcPlayerDmg()
//            binding.apply{
//
//            }
        }
    }
    private fun ghostGen(){
        enemy_level = (level..level+2).random()
        enemy_hp = (kotlin.math.floor((enemy_level * 0.1 + 1) * HP)).toInt()
            enemy_dmg = (enemy_level..enemy_level+1).random() + kotlin.math.floor((pow / 2).toDouble())
            .toInt()
        binding.ghostImageview.isVisible = true
        binding.nextstageButton.isVisible = false
        binding.nextstageButton.isEnabled = false
        binding.attackButton.isEnabled = true
        binding.ghostHealthText.isVisible = true
        binding.ghostHealthTextview.isVisible = true
        binding.ghostHealthText.text = enemy_hp.toString()
    }
    private fun calcPlayerDmg(){
        player_dmg = (kotlin.math.floor((pow * 0.01 + 1) * 0.5)).toInt()
    }
    private fun animateAttack() {
        val animationGo = ObjectAnimator.ofFloat(binding.playerImageview, "translationX", 0f, -100f)

        animationGo.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.playerImageview.setImageResource(R.drawable.player_attack)
            }
        })
        val animationDamage = ObjectAnimator.ofFloat(binding.ghostImageview, "translationX", 0f, -60f)
        animationDamage.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.ghostImageview.setImageResource(R.drawable.ghost_damaged)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
            }
        })
        val animationDamageEnd = ObjectAnimator.ofFloat(binding.ghostImageview, "translationX", -60f, 0f)
        animationDamageEnd.startDelay = 100;
        animationDamageEnd.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.ghostImageview.setImageResource(R.drawable.ghost)
            }
        })
        val animationReturn = ObjectAnimator.ofFloat(binding.playerImageview, "translationX", -100f, 0f)

        animationReturn.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.playerImageview.setImageResource(R.drawable.player_ready)
            }
        })


        val set = AnimatorSet()
        set.play(animationGo).before(animationDamage).before(animationDamageEnd).before(animationReturn)
        set.duration = 1000
        set.start()
    }
    private fun animateGhostAttack() {
        val animationGo = ObjectAnimator.ofFloat(binding.ghostImageview, "translationX", 0f, -100f)
        animationGo.startDelay = 2000;
        val animationDamage = ObjectAnimator.ofFloat(binding.playerImageview, "translationX", 0f, -50f)
        animationDamage.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.playerImageview.setImageResource(R.drawable.player_hurt)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {

            }
        })
        val animationDamageEnd = ObjectAnimator.ofFloat(binding.playerImageview, "translationX", -50f, 0f)
        animationDamage.startDelay = 200;
        animationDamageEnd.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.playerImageview.setImageResource(R.drawable.player_ready)
            }
        })
        val animationReturn = ObjectAnimator.ofFloat(binding.ghostImageview, "translationX", -100f, 0f)

        val set = AnimatorSet()
        set.play(animationGo).before(animationDamage).before(animationDamageEnd).before(animationReturn)
        set.duration = 700
        set.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.mscarla2.ghostfinder.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
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
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.round

class GameFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var player: Array<String>? = null
    private var pow: Int = 0
    private var agi: Int = 0
    private var def: Int = 0
    private var HP: Int = 0
    private var maxHP: Int = 0
    private var name: String? = null
    private var level: Int = 0
    private var sword: String? = null
    private var enemy_hp: Int = 0
    private var enemy_dmg: Int = 0
    private var enemy_level: Int = 0
    private var player_dmg: Int = 0
    private var ghost_killed: Int = 3
    private var dead: Boolean = false

    private var color: String = ""
    private var textColor: String = ""
    private var language: String = ""
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    private var sword_dmg = mapOf("wood_sword" to 1, "stone_sword" to 2, "diamond_sword" to 3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        _binding?.apply {
            nextstageButton.isVisible = false
            nextstageButton.isEnabled = false
            levelUpButton.isVisible = false
            levelUpButton.isEnabled = false
            gameOverHelpText.isVisible = false
            gameOverText.isVisible = false
            gameOverWarningText.isVisible = false
            ghostDeathText.isVisible = false
            ghostLeftText.text = ghost_killed.toString()
            backMenuButton.setOnClickListener {

                if(player != null){
                    var thisPlayer = Player()
                    thisPlayer.id = player!![0].toLong()
                    thisPlayer.name = player!![1]
                    thisPlayer.level = player!![2].toInt()
                    thisPlayer.hp = player!![3].toInt()
                    thisPlayer.pow = player!![4].toInt()
                    thisPlayer.agi = player!![5].toInt()
                    thisPlayer.def = player!![6].toInt()
                    thisPlayer.sword = player!![7]
                    if(dead){
                        sharedViewModel.deletePlayer(thisPlayer)
                        findNavController().navigate(R.id.action_gameFragment_to_characterFragment)
                    }
                    else{
                        val playedPlayer = GameFragmentDirections.actionGameFragmentToCharacterFragment(
                            player!!
                        )
                        Navigation.findNavController(it).navigate(playedPlayer)
                    }
                }
                else {
                    findNavController().navigate(R.id.action_gameFragment_to_characterFragment)
                }
            }
            attackButton.setOnClickListener {
                if(_binding != null){
                    playerAttack()
                }
                if(enemy_hp <= 0) {
                    animateGhostDeath()
                    ghostImageview.isVisible = false
                    nextstageButton.isVisible = true
                    nextstageButton.isEnabled = true
                    attackButton.isEnabled = false
                    binding.ghostHealthText.isVisible = false
                    binding.ghostHealthTextview.isVisible = false
                    ghost_killed -= 1
                    if(ghost_killed == 0){
                        levelUpButton.isEnabled = true
                        levelUpButton.isVisible = true
                        ghost_killed = 3
                        ghostLeftText.text = ghost_killed.toString()
                    }
                }
                else{
                    ghostAttack()
                }
            }
            levelUpButton.setOnClickListener{
                if (player != null) {
                    val playedPlayer =
                        GameFragmentDirections.actionGameFragmentToLevelUpFragment(
                            player!!
                        )
                    Navigation.findNavController(it).navigate(playedPlayer)
                }
                else {
                    findNavController().navigate(R.id.action_gameFragment_to_levelUpFragment)

                }
            }
            fleeButton.setOnClickListener{
                ghostImageview.isVisible = false
                attackButton.isVisible = false
                ghostHealthText.isVisible = false
                ghostHealthTextview.isVisible = false
                nextstageButton.isVisible = true
                nextstageButton.isEnabled = true
                HP = maxHP
                animateHealthGain()
                gameHealthText.text = HP.toString()
            }
            nextstageButton.setOnClickListener{
                ghostImageview.isVisible = true
                nextstageButton.isVisible = false
                nextstageButton.isEnabled = false
                attackButton.isEnabled = true
                attackButton.isVisible = true
                ghostHealthText.isVisible = true
                ghostHealthTextview.isVisible = true
                ghostGen()
            }
        }
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val safeArgs = GameFragmentArgs.fromBundle(args)
            player = safeArgs.player
            name = player!![1]
            level = player!![2].toInt()
            HP = player!![3].toInt()
            maxHP = player!![3].toInt()
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
        }
    }
    private fun playerAttack(){
        animateAttack()
        val critChance = (0..100).random()
        enemy_hp -= if(critChance in 0..(95 - agi * 3)){
            player_dmg
        } else{
            (player_dmg * 2)
        }
    }
    private fun ghostAttack(){
        animateGhostAttack()
        HP -= (enemy_dmg - kotlin.math.floor((def * 0.5 + 1)).toInt())
    }
    private fun ghostGen(){
        enemy_level = (level..level+2).random()
        enemy_hp = (kotlin.math.floor((((enemy_level-1..enemy_level).random()) * HP/2 + 1).toDouble())).toInt()
        enemy_dmg = (enemy_level..enemy_level+1).random()
        binding.ghostImageview.isVisible = true
        binding.nextstageButton.isVisible = false
        binding.nextstageButton.isEnabled = false
        binding.attackButton.isEnabled = true
        binding.ghostHealthText.isVisible = true
        binding.ghostHealthTextview.isVisible = true
        binding.ghostHealthText.text = enemy_hp.toString()
    }
    private fun calcPlayerDmg(){
        player_dmg = (round((pow * 0.25 + 1) * 0.5).toInt())
    }
    private fun animateHealthGain() {
        val animationGo = ObjectAnimator.ofFloat(binding.gameHealthGainText, "translationY", 0f, -20f)

        animationGo.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.gameHealthGainText.text = "+${maxHP}"
                binding.gameHealthGainText.isVisible = true
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.gameHealthGainText.isVisible = false
            }
        })
        val animationReturn = ObjectAnimator.ofFloat(binding.gameHealthGainText, "translationY", -20f, 0f)

        val set = AnimatorSet()
        set.play(animationGo).before(animationReturn)
        set.duration = 500
        set.start()
    }
    private fun animateGhostDeath() {
        val animationGo = ObjectAnimator.ofFloat(binding.ghostDeathText, "translationY", 0f, -20f)
        animationGo.startDelay = 500;
        animationGo.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.ghostDeathText.isVisible = true
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                binding.ghostDeathText.isVisible = false
                binding.ghostLeftText.text = ghost_killed.toString()
            }
        })
        val animationReturn = ObjectAnimator.ofFloat(binding.gameHealthGainText, "translationY", -20f, 0f)

        val set = AnimatorSet()
        set.play(animationGo).before(animationReturn)
        set.duration = 500
        set.start()
    }
    private fun animateAttack() {
        val animationGo = ObjectAnimator.ofFloat(binding.playerImageview, "translationX", 0f, -100f)

        animationGo.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.apply{
                    attackButton.isEnabled = false
                    fleeButton.isEnabled = false
                    backMenuButton.isEnabled = false

                }
            }
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
                binding.ghostHealthText.text = enemy_hp.toString()

                binding.apply{
                    attackButton.isEnabled = true
                    fleeButton.isEnabled = true
                    backMenuButton.isEnabled = true

                }
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
                binding.gameHealthText.text = HP.toString()
                if(HP <= 0){
                    binding.apply{
                        playerImageview.setImageResource(R.drawable.player_dead)
                        ghostImageview.isVisible = false
                        ghostHealthText.isVisible = false
                        ghostHealthTextview.isVisible = false
                        gameOverText.isVisible = true
                        gameOverHelpText.isVisible = true
                        gameOverWarningText.isVisible = true
                        attackButton.isEnabled = false
                        nextstageButton.isEnabled = false
                        fleeButton.isEnabled = false
                        dead = true
                    }
                }
                else{
                    binding.playerImageview.setImageResource(R.drawable.player_ready)
                }
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    }
}

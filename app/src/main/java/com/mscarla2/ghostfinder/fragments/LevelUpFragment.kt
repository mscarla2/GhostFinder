package com.mscarla2.ghostfinder.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.databinding.FragmentLevelUpBinding
import com.mscarla2.ghostfinder.ui.MainViewModel

class LevelUpFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentLevelUpBinding? = null
    private var skillPoints = 5
    private var powNum: Int? = null
    private var agiNum: Int? = null
    private var defNum: Int? = null
    private var HP: Int = 0
    private var player: Array<String>? = null
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val levelUpBinding = FragmentLevelUpBinding.inflate(inflater, container, false)
        binding = levelUpBinding
        binding?.apply {
            totalSp2.text = skillPoints.toString()
            backToGameButton.setOnClickListener {
                if(skillPoints != 0){
                    context?.toast("Please use all your skill points!")
                }
                else{
                    player!![2] = (player!![2].toInt() + 1).toString()
                    player!![3] = (HP + 5).toString()
                    player!![4] = powNum.toString()
                    player!![5]= defNum.toString()
                    player!![6] = agiNum.toString()
                    val playedPlayer = LevelUpFragmentDirections.actionLevelUpFragmentToGameFragment(
                        player!!
                    )
                    Navigation.findNavController(it).navigate(playedPlayer)
                    context?.hideKeyboard(it)
                }
            }
        }
        return levelUpBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val safeArgs = GameFragmentArgs.fromBundle(args)
            player = safeArgs.player

            HP = player!![3].toInt()
            powNum = player!![4].toInt()
            agiNum = player!![5].toInt()
            defNum = player!![6].toInt()

            val inPowNum =powNum
            val inAgiNum =agiNum
            val inDefNum =defNum
            binding?.apply {
                levelHealthText.text = (HP + 5).toString()
                powTextview2.text = powNum.toString()
                agiTextview2.text = agiNum.toString()
                defTextview2.text = defNum.toString()

                upPowButton2.setOnClickListener {
                    skillPoints -= 1
                    powNum = powNum!! + 1
                    if (skillPoints < 0) {
                        skillPoints = 0
                        powNum = powNum!! - 1
                        insufficientSkillPoint()
                    }
                    powTextview2.text = powNum.toString()
                    totalSp2.text = skillPoints.toString()

                }
                upAgiButton2.setOnClickListener {
                    skillPoints -= 1
                    agiNum = agiNum!! + 1
                    if (skillPoints < 0) {
                        skillPoints = 0
                        agiNum = agiNum!! - 1
                        insufficientSkillPoint()
                    }
                    agiTextview2.text = agiNum.toString()
                    totalSp2.text = skillPoints.toString()
                }
                upDefButton2.setOnClickListener {
                    skillPoints -= 1
                    defNum = defNum!! + 1
                    if (skillPoints < 0) {
                        skillPoints = 0
                        defNum = defNum!! - 1
                        insufficientSkillPoint()
                    }
                    defTextview2.text = defNum.toString()
                    totalSp2.text = skillPoints.toString()
                }
                downPowButton2.setOnClickListener {
                    skillPoints += 1
                    powNum = powNum!! - 1
                    if (skillPoints > 10 || powNum!! <= inPowNum!!) {
                        skillPoints -= 1
                        powNum = powNum!! + 1
                        insufficientResource("POW")
                    }
                    powTextview2.text = powNum.toString()
                    totalSp2.text = skillPoints.toString()
                }
                downAgiButton2.setOnClickListener {
                    skillPoints += 1
                    agiNum = agiNum!! - 1
                    if (skillPoints > 10 || agiNum!! <= inAgiNum!!) {
                        skillPoints -= 1
                        agiNum = agiNum!! + 1
                        insufficientResource("AGI")
                    }
                    agiTextview2.text = agiNum.toString()
                    totalSp2.text = skillPoints.toString()

                }
                downDefButton2.setOnClickListener {
                    skillPoints += 1
                    defNum = defNum!! - 1
                    if (skillPoints > 10 || defNum!! <= inDefNum!!) {
                        skillPoints -= 1
                        defNum = defNum!! + 1
                        insufficientResource("DEF")
                    }
                    defTextview2.text = defNum.toString()
                    totalSp2.text = skillPoints.toString()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
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
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}

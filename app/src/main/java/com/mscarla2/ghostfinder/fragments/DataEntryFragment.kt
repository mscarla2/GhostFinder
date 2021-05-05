package com.mscarla2.ghostfinder.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mscarla2.ghostfinder.R
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.databinding.FragmentDataEntryBinding
import com.mscarla2.ghostfinder.ui.MainViewModel

class DataEntryFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentDataEntryBinding? = null
    private var skillPoints = 7
    private var powNum: Int = 1
    private var agiNum: Int = 1
    private var defNum: Int = 1
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
        val dataEntryBinding = FragmentDataEntryBinding.inflate(inflater, container, false)
        binding = dataEntryBinding
        binding?.apply {
            powTextview.text = powNum.toString()
            agiTextview.text = agiNum.toString()
            defTextview.text = defNum.toString()
            totalSp.text = skillPoints.toString()

            upPowButton.setOnClickListener {
                skillPoints -= 1
                powNum += 1
                if(skillPoints < 0){
                    skillPoints = 0
                    powNum -= 1
                    insufficientSkillPoint()
                }
                powTextview.text = powNum.toString()
                totalSp.text = skillPoints.toString()

            }
            upAgiButton.setOnClickListener {
                skillPoints -= 1
                agiNum += 1
                if(skillPoints < 0){
                    skillPoints = 0
                    agiNum -= 1
                    insufficientSkillPoint()
                }
                agiTextview.text = agiNum.toString()
                totalSp.text = skillPoints.toString()
            }
            upDefButton.setOnClickListener {
                skillPoints -= 1
                defNum += 1
                if(skillPoints < 0){
                    skillPoints = 0
                    defNum -= 1
                    insufficientSkillPoint()
                }
                defTextview.text = defNum.toString()
                totalSp.text = skillPoints.toString()
            }
            downPowButton.setOnClickListener {
                skillPoints += 1
                powNum -= 1
                if(skillPoints > 10 || powNum <= 0){
                    skillPoints -= 1
                    powNum += 1
                    insufficientResource("POW")
                }
                powTextview.text = powNum.toString()
                totalSp.text = skillPoints.toString()
            }
            downAgiButton.setOnClickListener {
                skillPoints += 1
                agiNum -= 1
                if(skillPoints > 10 || agiNum <= 0){
                    skillPoints -= 1
                    agiNum += 1
                    insufficientResource("AGI")
                }
                agiTextview.text = agiNum.toString()
                totalSp.text = skillPoints.toString()

            }
            downDefButton.setOnClickListener {
                skillPoints += 1
                defNum -= 1
                if(skillPoints > 10 || defNum <= 0){
                    skillPoints -= 1
                    defNum += 1
                    insufficientResource("DEF")
                }
                defTextview.text = defNum.toString()
                totalSp.text = skillPoints.toString()
            }
            saveButton.setOnClickListener {
                val player = Player()
                player.name = editName.text.toString()
                player.pow = powNum
                player.def = defNum
                player.agi = agiNum
                sharedViewModel.insert(player)
                val msg = resources.getString(R.string.player_added_alert, "${player.name}")
                val builder = AlertDialog.Builder(context)
                with(builder) {
                    setTitle("Alert")
                    setMessage(msg)
                    setIcon(R.drawable.ic_baseline_notifications_active_24)
                    setPositiveButton("OK", null)
                    show()
                }
                findNavController().navigate(R.id.action_dataEntryFragment_to_characterFragment)
                context?.hideKeyboard(it)
            }
            cancelButton.setOnClickListener{
                findNavController().navigate(R.id.action_dataEntryFragment_to_characterFragment)
            }
        }
        return dataEntryBinding.root
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

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.dataclass.Fighter
import java.util.concurrent.Executors

class FightViewModel: ViewModel() {

    private val FORWARD: Int = 1
    private val BACKWARD: Int = 2

    // Flags
    private var fightReady: MutableLiveData<Boolean> = MutableLiveData()
    private var needToRefreshFighters: MutableLiveData<Boolean> = MutableLiveData()

    // Data
    private var fightersList: MutableList<Fighter> = ArrayList()

    // Live Data
    fun isFightReady() = fightReady
    fun needRefreshingFighters() = needToRefreshFighters

    fun getNewestFightersData() = fightersList

    /**
     * Receive basic rolled data
     */
    fun inputFightersData(fighters: ArrayList<Fighter>) {
        fightersList = fighters
        fightersList.forEach { it.currentHp = it.hitPoints }
        getFightersDataSortedByInit()
        fightReady.postValue(true)
    }

    fun updateFighterHp(modifiedHp: Int, position: Int) {
        fightersList[position].currentHp = modifiedHp
        needToRefreshFighters.postValue(true)
    }

    fun killFighter(deadFighter: Fighter) { fightersList.remove(deadFighter) }
    fun nextFighter() { refreshFightersList(FORWARD) }
    fun backFighter() { refreshFightersList(BACKWARD) }

    // Private Functions
    private fun selector(fighter: Fighter): Int = fighter.rolledInitiative

    private fun refreshFightersList(direction: Int) {
        val reOrderedList: MutableList<Fighter> = ArrayList()
        val executors = Executors.newSingleThreadExecutor()
        if (direction == FORWARD) {
            executors.submit {
                for (i in 1 until fightersList.size step 1) {
                    reOrderedList.add(fightersList[i])
                }
            }
            executors.submit { reOrderedList.add(fightersList[0]) }
        } else if (direction == BACKWARD) {
            executors.submit { reOrderedList.add(fightersList.last()) }
            executors.submit {
                for (i in 0 until fightersList.size-1 step 1) {
                    reOrderedList.add(fightersList[i])
                }
            }
        }
        executors.submit { fightersList = reOrderedList }
        executors.submit { needToRefreshFighters.postValue(true) }
        executors.shutdown()
    }

    private fun getFightersDataSortedByInit(): MutableList<Fighter> {
        fightersList.sortByDescending { selector(it) }
        return fightersList
    }

}
package com.kevgilles.dm.initiativetracker.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.SwipeToDeleteCallback
import com.kevgilles.dm.initiativetracker.adapters.FightAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Fighter
import com.kevgilles.dm.initiativetracker.fragments.DamageReceivedFragment
import com.kevgilles.dm.initiativetracker.viewmodels.FightViewModel
import kotlinx.android.synthetic.main.activity_fight.*

class FightActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mFightVM: FightViewModel
    private lateinit var mAdapter: FightAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var damagedFighterPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val fighters = intent.getParcelableArrayListExtra<Fighter>("fighters")
        mFightVM = ViewModelProviders.of(this).get(FightViewModel::class.java)
        mFightVM.isFightReady().observe(this, Observer { initialiseInterface() })
        mFightVM.needRefreshingFighters().observe(this, Observer { if(it) refreshAdapter() })

        nextButton.setOnClickListener { mFightVM.nextFighter() }
        backButton.setOnClickListener { mFightVM.backFighter() }
        mFightVM.inputFightersData(fighters)

    }

    private fun initialiseInterface() {
        mAdapter = FightAdapter(ArrayList(mFightVM.getNewestFightersData()), this)
        mRecyclerView = findViewById(R.id.rv_fighters)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        val swipeHandlerLeft = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mRecyclerView.adapter as FightAdapter
                val killedFighter = adapter.removeAt(viewHolder.adapterPosition)
                mFightVM.killFighter(killedFighter)
            }
        }
        ItemTouchHelper(swipeHandlerLeft).attachToRecyclerView(mRecyclerView)
    }


    private fun refreshAdapter() {
        mAdapter = FightAdapter(ArrayList(mFightVM.getNewestFightersData()), this)
        mRecyclerView.adapter = mAdapter
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            damagedFighterPosition = view.tag as Int
            val fighter = mAdapter.getFighterAtPosition(damagedFighterPosition)
            val fragment = DamageReceivedFragment()
            val args = Bundle()
            args.putParcelable("fighter", fighter)
            fragment.arguments = args
            fragment.show(supportFragmentManager, "ok TAG")
        }
    }

    fun updateFighterHealth(modifiedHp: Int) {
        if (damagedFighterPosition == -1) return
        mFightVM.updateFighterHp(modifiedHp, damagedFighterPosition)
        damagedFighterPosition = -1
    }

    fun cancelFighterHealth() { damagedFighterPosition = -1 }

//        TODO: Add round counter in title bar
//        supportActionBar?.title = "Round Number: ${mTrackingVM.roundCount++}"
//        private fun nextRound() {}
}

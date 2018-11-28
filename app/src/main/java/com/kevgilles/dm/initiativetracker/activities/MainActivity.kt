package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.adapters.CampaignAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import com.kevgilles.dm.initiativetracker.dataclass.Campaign.Companion.CAMPAIGN_ID
import com.kevgilles.dm.initiativetracker.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mMainActivityVM: MainActivityViewModel
    private lateinit var mAdapter: CampaignAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener{
            val intent = Intent(this, AddCampaignActivity::class.java)
            startActivityForResult(intent, 1)
        }
        mMainActivityVM = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mMainActivityVM.getCampaignsList().observe(this, Observer { updateRecyclerView(it!!) })
        mMainActivityVM.refreshCampaignList()
    }

    /**
     * Refresh Recycler View on Campaign Added
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            mMainActivityVM.refreshCampaignList()
        }
    }

    /**
     * Refresh the view when database is updated
     */
    private fun updateRecyclerView(campaignsList: List<Campaign>) {
        mAdapter = CampaignAdapter(campaignsList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_campaigns)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            val campaignId = mAdapter.getCampaignIdAtPosition(view.tag as Int)
            val intent = Intent(this, CampaignActivity::class.java)
            intent.putExtra(CAMPAIGN_ID, campaignId)
            startActivity(intent)
        }
    }
}

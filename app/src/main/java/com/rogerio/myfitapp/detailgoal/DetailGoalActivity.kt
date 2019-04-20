package com.rogerio.myfitapp.detailgoal

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.rogerio.myfitapp.R
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import kotlinx.android.synthetic.main.activity_detail_goal.*


class DetailGoalActivity : AppCompatActivity() {
    companion object {
        const val DATAGOAL = "data_goal"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_goal)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val intent = getIntent()
        val fitItemViewEntity = intent.getParcelableExtra(DATAGOAL) as? FitItemViewEntity

        val detailsFragment =
                DetailGoalFragment.newInstance(fitItemViewEntity)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_detail, detailsFragment, "fitDetails")
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


}

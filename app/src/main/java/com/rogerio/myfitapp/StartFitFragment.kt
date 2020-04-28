package com.rogerio.myfitapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.rogerio.myfitapp.detailgoal.DetailGoalActivity
import com.rogerio.myfitapp.detailgoal.DetailGoalActivity.Companion.DATAGOAL
import com.rogerio.myfitapp.presentation.MyFitViewModel
import com.rogerio.myfitapp.presentation.adapter.FitListAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class StartFitFragment : Fragment() {
    private var fitnessOptions: FitnessOptions? = null
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var binding: com.rogerio.myfitapp.databinding.FragmentMainBinding
    val viewModel by viewModel<MyFitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = FitListAdapter()
        viewModel.start()
        val layoutManager = LinearLayoutManager(context)
        fitlist.layoutManager = layoutManager
        fitlist.hasFixedSize()

        fitlist.adapter = adapter
        adapter.selecteItem.observe(this, Observer { fitItem ->
            fitnessOptions?.let {
                if (validPermissions(it)) {
                    viewModel.selectedItem(fitItem)
                }
            }
        })

        viewModel.closeScreen.observe(this, Observer {
            val intent = Intent(activity, DetailGoalActivity::class.java)
            intent.putExtra(DATAGOAL, it)
            startActivity(intent)
        })

        fitlist.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .build()

        fitnessOptions?.let { fit : FitnessOptions ->
            validPermissions(fit)
        }

    }

    private fun validPermissions(fit: FitnessOptions) : Boolean {

        val account = /*GoogleSignIn.getLastSignedInAccount(context)*/ GoogleSignIn.getAccountForExtension(context!!, fit)

        if (!GoogleSignIn.hasPermissions(account, fit)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fit)

            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                Toast.makeText(context,"Permission accept", Toast.LENGTH_LONG).show()
            }
        }
    }

}

package com.rogerio.myfitapp.detailgoal


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.rogerio.myfitapp.R
import com.rogerio.myfitapp.databinding.FragmentDetailGoalBinding
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailGoalFragment : Fragment() {
    private var fitnessOptions: FitnessOptions? = null
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    val viewModel by viewModel<DetailGoalViewModel>()

    private lateinit var binding: FragmentDetailGoalBinding
    companion object {
        private val FIT_ITEM_ARG = "fitItemViewEntity"
        fun newInstance(fitItemViewEntity: FitItemViewEntity?): DetailGoalFragment {
            val fragment = DetailGoalFragment()
            fitItemViewEntity?.let {
                var args = Bundle()
                args.putParcelable(FIT_ITEM_ARG, fitItemViewEntity)
                fragment.arguments = args
            }
            return fragment
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_goal, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.setFitItemViewEntity(arguments?.getParcelable(FIT_ITEM_ARG))
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .build()

        fitnessOptions?.let {
            if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), it)) {
                activity?.let { it1 ->
                    GoogleSignIn.requestPermissions(
                            it1,
                            GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                            GoogleSignIn.getLastSignedInAccount(it1),
                            it
                    )
                }
            } else {
               viewModel.start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                viewModel.start()
            }
        }
    }


}

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
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.rogerio.myfitapp.R
import com.rogerio.myfitapp.databinding.FragmentDetailGoalBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailGoalFragment : Fragment() {
    private var fitnessOptions: FitnessOptions? = null
    private var myStepCountListener: OnDataPointListener? = null
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    val viewModel by viewModel<DetailGoalViewModel>()

    private lateinit var binding: FragmentDetailGoalBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_goal, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
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

//    private fun accessGoogleFit() {
//        val cal = Calendar.getInstance()
//        cal.setTime(Date())
//        val endTime = cal.getTimeInMillis()
//        cal.add(Calendar.YEAR, -1)
//        val startTime = cal.getTimeInMillis()
//        activity?.let { act ->
//            myStepCountListener = OnDataPointListener {
//                viewModel.setDataPoint(it)
//            }
//
//            val readRequest = DataReadRequest.Builder()
//                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                    //.read(DataType.TYPE_STEP_COUNT_DELTA)
//                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                    .bucketByTime(1, TimeUnit.HOURS)
//                    .build()
//
//
//            GoogleSignIn.getLastSignedInAccount(act)?.let {
//                Fitness.getHistoryClient(act, it)
//                        .readData(readRequest)
//                        .addOnSuccessListener(OnSuccessListener<DataReadResponse> {
//                            Timber.d("onSuccess -> %s", it.toString())
//                            viewModel.setHistorics(it)
//
//
//                        })
//                        .addOnFailureListener(OnFailureListener { e -> viewModel.setHistoryFailed(e) })
//                        .addOnCompleteListener(OnCompleteListener<DataReadResponse> { viewModel.setHistoricsCompleted() })
//
//
//                Fitness.getSensorsClient(act, it)
//                        .add(
//                                SensorRequest.Builder()
//                                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                                        .setSamplingRate(10, TimeUnit.SECONDS)  // sample once per minute
//                                        .build(),
//                                myStepCountListener!!
//                        )
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                viewModel.start()
            }
        }
    }


}

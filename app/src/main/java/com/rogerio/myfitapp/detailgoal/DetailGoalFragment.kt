package com.rogerio.myfitapp.detailgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.fitness.FitnessOptions
import com.rogerio.myfitapp.R
import com.rogerio.myfitapp.databinding.FragmentDetailGoalBinding
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailGoalFragment : Fragment() {
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




}

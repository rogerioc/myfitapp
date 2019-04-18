package com.rogerio.myafitapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rogerio.myafitapp.presentation.MyFitViewModel
import com.rogerio.myafitapp.presentation.adapter.FitListAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class StartFitFragment : Fragment() {

    private lateinit var binding: com.rogerio.myafitapp.databinding.FragmentMainBinding
    val viewModel by viewModel<MyFitViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.start()
        val layoutManager = LinearLayoutManager(context)
        fitlist.layoutManager = layoutManager
        fitlist.hasFixedSize()
        fitlist.adapter = FitListAdapter()
        fitlist.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

    }

}

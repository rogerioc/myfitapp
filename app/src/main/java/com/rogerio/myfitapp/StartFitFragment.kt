package com.rogerio.myfitapp


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

    private lateinit var binding: com.rogerio.myfitapp.databinding.FragmentMainBinding
    val viewModel by viewModel<MyFitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.setLifecycleOwner(this)
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
        adapter.selecteItem.observe(this, Observer {
            viewModel.selectedItem(it)
        })

        viewModel.closeScreen.observe(this, Observer {
            val intent = Intent(activity, DetailGoalActivity::class.java)
            intent.putExtra(DATAGOAL, it)
            startActivity(intent)
        })

        fitlist.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

    }

}

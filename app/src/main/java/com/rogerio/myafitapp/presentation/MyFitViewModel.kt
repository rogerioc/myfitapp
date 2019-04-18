package com.rogerio.myafitapp.presentation

import com.rogerio.myafitapp.core.BaseViewModel
import com.rogerio.myafitapp.core.addTo
import com.rogerio.myafitapp.presentation.model.FitItemViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers

class MyFitViewModel(
        private val interactor: FitInteractor
) : BaseViewModel() {
    var fitList: ArrayList<FitItemViewEntity> = ArrayList()

    fun start() {
        interactor.getFitsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it->
                        fitList.addAll(it)
                        notifyChange()
                }.addTo(this)

    }
}
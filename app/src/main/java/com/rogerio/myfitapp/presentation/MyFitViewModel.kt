package com.rogerio.myfitapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rogerio.myfitapp.core.BaseViewModel
import com.rogerio.myfitapp.core.addTo
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers

class MyFitViewModel(
    private val interactor: FitInteractor
) : BaseViewModel() {
    var fitList: ArrayList<FitItemViewEntity> = ArrayList()
    private val _callScreen = MutableLiveData<FitItemViewEntity>()
    val closeScreen: LiveData<FitItemViewEntity>
        get() = _callScreen

    fun start() {
        interactor.getFitsList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                fitList.addAll(it)
                notifyChange()
            }.addTo(this)

    }

    fun selectedItem(it: FitItemViewEntity?) {
        _callScreen.postValue(it)
    }
}
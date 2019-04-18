package com.rogerio.myafitapp

import com.nhaarman.mockitokotlin2.verify
import com.rogerio.myafitapp.presentation.FitInteractor
import com.rogerio.myafitapp.services.repository.FitDataSource
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FitInteractorTests {

    @Mock
    private lateinit var repository: FitDataSource

    private  lateinit var interactor: FitInteractor
    @Before
    fun seTup(){
        interactor = FitInteractor(repository)
    }

    @Test
    fun shouldCallServiceGetFitLists() {
        interactor.getFitsList()
        verify(repository).getFitList()
    }

}
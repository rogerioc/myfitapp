package com.rogerio.myfitapp

import com.nhaarman.mockitokotlin2.verify
import com.rogerio.myfitapp.presentation.FitInteractor
import com.rogerio.myfitapp.services.repository.FitDataSource
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
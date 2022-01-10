package com.example.data.usecase

import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseUsecaseTest {


    @Before
    open fun setUp() {
        MockitoAnnotations.initMocks(this)
    }
}
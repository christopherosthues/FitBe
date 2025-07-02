package org.darthacheron.fitbe.dependencies

import androidx.lifecycle.ViewModel

class MyViewModel(private val repository: MyRepository): ViewModel() {
    fun getHelloWorldString(): String {
        return repository.helloWorld()
    }
}
package com.iliasahmed.testpluang.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.iliasahmed.testpluang.data.PreferenceRepository
import com.iliasahmed.testpluang.rest.ApiRepository
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    open var apiRepository: ApiRepository? = null
    open var preferenceRepository: PreferenceRepository? = null
    var savedStateHandle: SavedStateHandle? = null

    constructor() {}
    constructor(
        savedStateHandle: SavedStateHandle?,
        apiRepository: ApiRepository?,
        preferenceRepository: PreferenceRepository?
    ) {
        this.savedStateHandle = savedStateHandle
        this.apiRepository = apiRepository
        this.preferenceRepository = preferenceRepository
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
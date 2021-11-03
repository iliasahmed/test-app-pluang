package com.iliasahmed.testpluang.ui.account

import com.iliasahmed.testpluang.data.PreferenceRepository
import com.iliasahmed.testpluang.rest.ApiRepository
import com.iliasahmed.testpluang.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    apiRepository: ApiRepository,
    preferenceRepository: PreferenceRepository
) :
    BaseViewModel() {

    override var apiRepository: ApiRepository? = null
    override var preferenceRepository: PreferenceRepository? = null

    fun getName(): String? {
        return preferenceRepository!!.userName
    }

    fun getEmail(): String? {
        return preferenceRepository!!.email
    }

    fun getImage(): String? {
        return preferenceRepository!!.image
    }

    init {
        this.apiRepository = apiRepository
        this.preferenceRepository = preferenceRepository
    }
}
package com.iliasahmed.testpluang.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.iliasahmed.testpluang.data.PreferenceRepository
import com.iliasahmed.testpluang.listener.ResponseListener
import com.iliasahmed.testpluang.model.CommonSuccessResponse
import com.iliasahmed.testpluang.model.QuotesModel
import com.iliasahmed.testpluang.rest.ApiRepository
import com.iliasahmed.testpluang.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore


@HiltViewModel
class HomeViewModel @Inject constructor(
    apiRepository: ApiRepository,
    preferenceRepository: PreferenceRepository
) :
    BaseViewModel() {

    @Inject lateinit var db: FirebaseFirestore

    override var apiRepository: ApiRepository? = null
    override var preferenceRepository: PreferenceRepository? = null
    var error = MutableLiveData<String>()
    var liveList = MutableLiveData<List<QuotesModel>>()
    var saved = MutableLiveData<Boolean>()

    fun getQuotes(){
        apiRepository!!.getQuotes(object :
            ResponseListener<CommonSuccessResponse<List<QuotesModel>?>?, String> {
            override fun onDataFetched(
                response: CommonSuccessResponse<List<QuotesModel>?>?,
                statusCode: Int
            ) {
                liveList.value = response!!.data
            }

            override fun onFailed(errorBody: String?, errorCode: Int) {
                if (errorBody != null) {
                    Log.e("==========", errorBody)
                };
                error.value = errorBody
            }
        })
    }

    fun saveToFirestore(map: HashMap<String, QuotesModel>){
        for(item in map){
            val data: MutableMap<String, Any> = HashMap()
            data["sid"] = item.value.sid
            data["price"] = item.value.price
            data["high"] = item.value.high
            data["low"] = item.value.low
            data["change"] = item.value.change
            data["volume"] = item.value.volume
            data["date"] = item.value.date
            data["close"] = item.value.close
            preferenceRepository!!.email.also { data["email"] = it!! }
            db.collection("quotes").document("${preferenceRepository!!.email}_${item.value.sid}")
                .set(data)
                .addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully written!"
                    )
                    saved.value = true
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Error writing document",
                        e
                    )
                    saved.value = false
                })
        }

    }

    init {
        this.apiRepository = apiRepository
        this.preferenceRepository = preferenceRepository
    }
}

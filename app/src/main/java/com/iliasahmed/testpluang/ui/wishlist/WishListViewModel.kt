package com.iliasahmed.testpluang.ui.wishlist

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.iliasahmed.testpluang.model.QuotesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.iliasahmed.testpluang.data.PreferenceRepository


@HiltViewModel
class WishListViewModel @Inject constructor(db: FirebaseFirestore) : ViewModel() {

    @Inject lateinit var preferenceRepository: PreferenceRepository

    var liveList = MutableLiveData<ArrayList<QuotesModel>>()
    var list = ArrayList<QuotesModel>()
    private var db: FirebaseFirestore = db
    var deletedObject = MutableLiveData<String>()

    fun loadData(){
        list.clear()
        db.collection("quotes")
            .whereEqualTo("email", preferenceRepository.email)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e(TAG, "${document.id} => ${document.data}")
                    list.add(QuotesModel(
                        document.data.get("sid") as String,
                        (document.data.get("price") as Double),
                        (document.data.get("close") as Double),
                        (document.data.get("change") as Double),
                        (document.data.get("high") as Double),
                        (document.data.get("low") as Double),
                        (document.data.get("volume") as Double),
                        document.data.get("date") as String
                    ))
                }
                liveList.value = list
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun deleteItemFromWishlist(item: String){
        db.collection("quotes").document(item)
            .delete()
            .addOnSuccessListener(OnSuccessListener<Void?> {
                deletedObject.value = item
                Log.d(
                    TAG,
                    "DocumentSnapshot successfully deleted!"
                )
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w(
                    TAG,
                    "Error deleting document",
                    e
                )
            })
    }
}
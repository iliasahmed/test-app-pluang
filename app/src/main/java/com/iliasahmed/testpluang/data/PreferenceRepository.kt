package com.iliasahmed.testpluang.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.iliasahmed.testpluang.model.UserModel
import com.iliasahmed.testpluang.utils.ConstantUtils
import javax.inject.Inject


class PreferenceRepository @Inject constructor(context: Context, gson: Gson) {
    private val preference: SharedPreferences
    private val preferenceEditor: SharedPreferences.Editor
    private val gson: Gson

    fun saveUserName(userName: String?) {
        preferenceEditor.putString(ConstantUtils.USERNAME, userName).commit()
    }

    val userName: String?
        get() = preference.getString(ConstantUtils.USERNAME, "")

    fun saveEmail(userName: String?) {
        preferenceEditor.putString(ConstantUtils.EMAIL, userName).commit()
    }

    val email: String?
        get() = preference.getString(ConstantUtils.EMAIL, "")

    fun saveImage(userName: String?) {
        preferenceEditor.putString(ConstantUtils.IMAGE, userName).commit()
    }

    val image: String?
        get() = preference.getString(ConstantUtils.IMAGE, "")

    fun saveUserData(userModel: UserModel?) {
        preferenceEditor.putString(ConstantUtils.USER_MODEL, gson.toJson(userModel)).commit()
    }

    val userData: UserModel?
        get() {
            val model: UserModel? = gson.fromJson(
                preference.getString(ConstantUtils.USER_MODEL, ""),
                UserModel::class.java
            )
            if (model == null) return null
            return model
        }


    fun clearAll() {
        preferenceEditor.clear().commit()
    }

    init {
        preference = context.getSharedPreferences("EasyPreferencePref", Context.MODE_PRIVATE)
        preferenceEditor = preference.edit()
        this.gson = gson
    }
}

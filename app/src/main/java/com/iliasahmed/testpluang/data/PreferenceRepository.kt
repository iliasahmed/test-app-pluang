package com.iliasahmed.testpluang.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.iliasahmed.testpluang.utils.ConstantUtils
import javax.inject.Inject


class PreferenceRepository @Inject constructor(context: Context, gson: Gson) {
    private val preference: SharedPreferences = context.getSharedPreferences("EasyPreferencePref", Context.MODE_PRIVATE)
    private val preferenceEditor: SharedPreferences.Editor = preference.edit()

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


    fun clearAll() {
        preferenceEditor.clear().commit()
    }

}

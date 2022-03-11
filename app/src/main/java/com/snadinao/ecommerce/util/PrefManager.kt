package com.snadinao.ecommerce.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class PrefManager(var context: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    // shared pref mode
    var PRIVATE_MODE = 0
    var isFirstTimeLaunch: Boolean
    get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    set(isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object{
        // shared pref file name
        private const val PREF_NAME = "welcome"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }
}
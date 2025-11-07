package com.someverse.someverse

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.someverse.someverse.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

         KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
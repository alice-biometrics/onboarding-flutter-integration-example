package com.example.onboarding_integration_test

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import com.alicebiometrics.onboarding.api.OnboardingError
import com.alicebiometrics.onboarding.utils.DISMISS_BUTTON_CODE
import com.alicebiometrics.onboarding.utils.ONBOARDING_ERROR
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.alicebiometrics/onboarding"
    private val REQUEST_CODE = 5000
    private var result: MethodChannel.Result? = null
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            var trialToken = call.argument<String>("trialToken")
            var email = call.argument<String>("email")

            this.result = result
            val intent =
                Intent(this, Onboarding::class.java)
            intent.putExtra("trialToken", trialToken)
            intent.putExtra("email", email)
            startActivityForResult(intent, 500)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val userInfo = data!!.getStringExtra("userStatus")
                        val userId = JSONObject(userInfo).getString("user_id")
                        result?.success(userId)
                    }
                    Activity.RESULT_CANCELED -> {
                        result?.success("canceled")
                    }
                    ONBOARDING_ERROR -> {
                        val onboardingError: OnboardingError =
                            data!!.getParcelableExtra("onboardingError")!!
                            result?.error("ERROR", onboardingError.toString(), onboardingError.toString())
                    }
                    AUTHENTICATION_ERROR -> {
                        val authenticationError: String =
                            data!!.getStringExtra("authenticationError")!!
                        result?.error("ERROR", authenticationError, authenticationError)
                    }
                    DISMISS_BUTTON_CODE -> {
                        result?.success("dismissButton")
                    }
                }
            }
        }
    }
}
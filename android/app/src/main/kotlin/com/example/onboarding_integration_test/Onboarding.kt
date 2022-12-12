package com.example.onboarding_integration_test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alicebiometrics.onboarding.api.DocumentType
import com.alicebiometrics.onboarding.api.Onboarding
import com.alicebiometrics.onboarding.api.OnboardingError
import com.alicebiometrics.onboarding.auth.Response
import com.alicebiometrics.onboarding.auth.TrialAuthenticator
import com.alicebiometrics.onboarding.config.OnboardingConfig
import com.alicebiometrics.onboarding.sandbox.UserInfo
import com.alicebiometrics.onboarding.utils.DISMISS_BUTTON_CODE
import com.alicebiometrics.onboarding.utils.ONBOARDING_ERROR
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class Onboarding: AppCompatActivity() {

    val REQUEST_CODE = 5000
    var trialToken = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trialToken = intent.getStringExtra("trialToken").toString()
        email = intent.getStringExtra("email").toString()
    }

    override fun onResume() {
        super.onResume()

        val userInfo = UserInfo(email)

        val authenticator = TrialAuthenticator(
            trialToken = trialToken,
            userInfo = userInfo
        )

        authenticator.execute { response ->
            when (response) {
                is Response.Success -> {
                    val onboardingConfig = getOnboardingConfig(response.message)
                    runOnboarding(onboardingConfig)
                }
                is Response.Failure -> {
                    val intent = Intent()
                    intent.putExtra("authenticationError",  response.error)
                    setResult(AUTHENTICATION_ERROR, intent)
                    finish()
                }
            }
        }
    }

    fun getOnboardingConfig(userToken: String): OnboardingConfig {
        val config = OnboardingConfig.builder()
            .withUserToken(userToken)
            .withAddSelfieStage()
            .withAddDocumentStage(type = DocumentType.IDCARD, issuingCountry = "ESP")
            .withAddDocumentStage(type = DocumentType.DRIVERLICENSE, issuingCountry = "ESP")
        return config
    }

    fun runOnboarding(config: OnboardingConfig) {
        val onboarding = Onboarding(this, config = config)
        onboarding.run(REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(requestCode, data)
        finish()
    }
}
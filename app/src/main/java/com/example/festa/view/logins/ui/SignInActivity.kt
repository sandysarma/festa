package com.example.festa.view.logins.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels

import androidx.lifecycle.Observer
import com.developer.kalert.KAlertDialog
import com.example.festa.view.logins.viewmodel.verifyphonenumber.VerifyPhoneNumberViewModel
import com.example.festa.view.logins.viewmodel.login.LoginBody
import com.example.festa.view.logins.viewmodel.login.LoginViewModel
import com.example.festa.R
import com.example.festa.view.signup.ui.SignUpActivity
import com.example.festa.application.Festa
import com.example.festa.databinding.ActivitySignInBinding
import com.example.festa.ui.DashboardActivity
import com.freqwency.promotr.utils.ErrorUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var activity: Activity
    private lateinit var loinBody: LoginBody
    private var strPhoneno = ""
    val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var auth: FirebaseAuth
    private var verificationId: String = ""

    private val verifyViewModel: VerifyPhoneNumberViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private var progressDialogs: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this


        progressDialogs = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()
        binding.sendotp.setOnClickListener {
            strPhoneno = binding.phoneNoText.text.toString()
            if (binding.phoneNoText.text.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    "Please enter mobile no",
                    Toast.LENGTH_SHORT
                )
                    .show()

            } else {
                //getVerifyNumber()
                getLogin()
            }


        }

        binding.signinsendotptosignup.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.skip.setOnClickListener {
            val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
            intent.putExtra("login", "skip")
            startActivity(intent)
        }
        loginObserver()
        //verifyNumberObserver()
    }


    private fun getVerifyNumber() {
        loinBody = LoginBody(
            phone_no = strPhoneno
        )
        verifyViewModel.getLogIn(progressDialog, activity, loinBody)
    }

    private fun verifyNumberObserver() {
        verifyViewModel.progressIndicator.observe(this, Observer {
        })
        verifyViewModel.mloginResponse.observe(
            this
        ) {
            val message = it.peekContent().successMessage

            //  sendVerificationCode("+91$strPhoneno")
        }
        verifyViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SignInActivity, it)
            // errorDialogs()
        }
    }

    private fun errorDialogs() {
        KAlertDialog(this, KAlertDialog.ERROR_TYPE)
            .setTitleText("Invalid")
            .setContentText("Login Error")
            .confirmButtonColor(R.color.purple_600)
            .setConfirmClickListener(getString(R.string.OK), R.color.white_font, null)
            .show()

    }


    private fun sendVerificationCode(phoneNumber: String) {

        // showLoader()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatically sign in the user when verification is done
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle error
                    Log.e("UserMessage", "Verification failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save the verification ID
                    this@SignInActivity.verificationId = verificationId
                    val intent = Intent(this@SignInActivity, SignInVerify::class.java)
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("phoneNumber", strPhoneno)
                    startActivity(intent)
                    Log.e("UserMessage", "Verification failed: ${verificationId}")

                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in successful, go to the next activity or perform desired actions
                    Log.e("UserMessage", "onCreate: Successfully")
                    hideLoader()
                } else {
                    // Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun showLoader() {
        // Show your loader (progress indicator or dialog) here
        progressDialogs?.setMessage("Sending OTP...")
        progressDialogs?.show()
    }

    private fun hideLoader() {
        // Hide your loader here
        progressDialogs?.dismiss()
    }


    private fun getLogin() {
        loinBody = LoginBody(
            phone_no = strPhoneno
        )
        loginViewModel.getLogIn(progressDialog, activity, loinBody)
    }

    private fun loginObserver() {
        loginViewModel.progressIndicator.observe(this) {
        }
        loginViewModel.mloginResponse.observe(
            this
        ) {
            val message = it.peekContent().message
            Festa.encryptedPrefs.UserId = it.peekContent().data?.id!!
            Festa.encryptedPrefs.isFirstTime = false

            Toast.makeText(this@SignInActivity, message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("login", "login")
            startActivity(intent)
            finish()


        }
        loginViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SignInActivity, it)
            // errorDialogs()
        }
    }

}
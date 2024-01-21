package com.example.festa.view.logins.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.festa.view.logins.viewmodel.login.LoginBody
import com.example.festa.view.logins.viewmodel.login.LoginViewModel
import com.example.festa.view.signup.ui.SignUpActivity
import com.example.festa.application.Festa
import com.example.festa.databinding.ActivitySignInVerifyBinding
import com.example.festa.ui.DashboardActivity
import com.freqwency.promotr.utils.ErrorUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInVerify : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivitySignInVerifyBinding

    private lateinit var activity: Activity
    private val loginViewModel: LoginViewModel by viewModels()
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var strPhoneno = ""
    private lateinit var loinBody: LoginBody
    val progressDialog by lazy { CustomProgressDialog(this) }
    var verificationId = ""

    private var progressDialogs: ProgressDialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        //////////////FireBase///////////////////////
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        progressDialogs = ProgressDialog(this)

        verificationId = intent.getStringExtra("verificationId").toString()
        strPhoneno = intent.getStringExtra("phoneNumber").toString()
        Log.e("UserMessage", "phoneNumber " + strPhoneno)

        binding.verify.setOnClickListener {
            val otp = binding.otpEdiText.text.toString()
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhoneAuthCredential(credential)
        }


        binding.signusigninverifypage.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.resendOtpBtn.setOnClickListener {
            resendVerificationCode(strPhoneno)
        }

        loginObserver()

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in successful, go to the next activity or perform desired actions

                    getLogin()
                } else {
                    // Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }


    private fun resendVerificationCode(phoneNumber: String) {
        showLoader()
        val options = resendToken?.let {
            PhoneAuthOptions.newBuilder(mAuth)
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
                        resendToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // Save the new verification ID and resend token
                        this@SignInVerify.verificationId = verificationId
                        this@SignInVerify.resendToken = resendToken
                        Toast.makeText(this@SignInVerify, "OTP Resent", Toast.LENGTH_SHORT).show()
                        hideLoader()
                    }
                })
                .setForceResendingToken(it)
                .build()
        }

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
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

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("login", "login")
            startActivity(intent)
            finish()


        }
        loginViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SignInVerify, it)
            // errorDialogs()
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

}
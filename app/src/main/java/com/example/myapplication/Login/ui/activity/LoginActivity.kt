package com.example.myapplication.Login.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.BuildConfig
import com.example.myapplication.Login.data.model.UserResponse
import com.example.myapplication.Login.data.model.UserUtil
import com.example.myapplication.Login.data.model.userData
import com.example.myapplication.Login.data.repository.LoginRepository
import com.example.myapplication.Login.ui.viewmodel.LoginViewModel
import com.example.myapplication.Login.ui.viewmodel.LoginViewModelFactory
import com.example.myapplication.QC.ui.activity.QcActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.LayoutLoginBinding
import com.example.myapplication.utils.NetworkCheckUtils
import com.example.myapplication.utils.RetrofitBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginLayoutBinding: LayoutLoginBinding
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var viewModel: LoginViewModel
    private val loginRepository = LoginRepository(RetrofitBuilder.loginService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize ViewBinding for layout_login.xml (the included layout)
        loginLayoutBinding = binding.layoutLogin

        hideKeyboard()
        hideActionBar()
        initData()
        observeData()
        initEvent()
    }

    private fun initEvent() {
        loginLayoutBinding.buttonLogin.setOnClickListener {
            val email = loginLayoutBinding.editTextEmail.text.toString().trim()
            val password = loginLayoutBinding.editTextPassword.text.toString().trim()
            val modeNetwork = NetworkCheckUtils.isNetworkConnected(this)

            if (!modeNetwork) {
                offLineMode()
            } else if (modeNetwork) {
                mProgressDialog.show()
                if (email.isEmpty()) {
                    loginLayoutBinding.textInputEmail.error = "Email address cannot be empty"
                    mProgressDialog.dismiss()
                } else if (password.isEmpty()) {
                    loginLayoutBinding.textInputPassword.error = "Password cannot be empty"
                    mProgressDialog.dismiss()
                } else {
                    loginLayoutBinding.textInputPassword.error = null
                    loginLayoutBinding.textInputEmail.error = null
                    login(email, password)
                }
            } else {
                mProgressDialog.dismiss()
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(userData(email = email, password = password))
    }

    private fun observeData() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
        }

        viewModel.loading.observe(this) { loading ->
            if (loading) mProgressDialog.show()
            else mProgressDialog.dismiss()
        }

        viewModel.loginResult.observe(this) { userData ->
            if (userData != null) {
                saveUser(userData)
                handleLoginSuccess()
            }
        }
    }

    private fun saveUser(userData: UserResponse) {
        UserUtil.setLogin(applicationContext, true)
        UserUtil.saveCurrentUser(this, userData.user, userData.id.toString())
        mProgressDialog.dismiss()
    }

    private fun handleLoginSuccess() {
        // Navigate to the next activity
        val intent = Intent(this, QcActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun offLineMode() {
        mProgressDialog.dismiss()
        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
    }

    private fun initData() {
        mProgressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_progress_msg))
            setCancelable(true)
        }
        binding.version.text = BuildConfig.VERSION_NAME

        viewModel = ViewModelProvider(this, LoginViewModelFactory(loginRepository))
            .get(LoginViewModel::class.java)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.job.cancel()
    }
}

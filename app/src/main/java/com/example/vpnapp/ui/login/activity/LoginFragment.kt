package com.example.vpnapp.ui.login.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentLoginScreenBinding
import com.example.vpnapp.ui.login.viewmodels.LoginViewModel
import com.example.vpnapp.utils.extensions.navigateWithAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewBinding: FragmentLoginScreenBinding
    private val viewModel by viewModels<LoginViewModel>() // Login View model

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginScreenBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.saveIsUserLoggedIn(false)
        addBtnClickListeners()
        setupTextWatcher()
        lifecycleScope.launch {
            viewModel.isUserLoggedIn.first()
        }
    }

    private fun addBtnClickListeners() {
        viewBinding.apply {
            btnAppLogo.setOnClickListener {
                // TODO: Implement click
            }

            btnSetting.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.settingsFragment)
            }

            btnTextFieldArrow.setOnClickListener {
                // Add condition based on how many devices are connected and then show the screen
//                findNavController().navigateWithAnimation(R.id.homeFragment)
                viewModel.saveIsUserLoggedIn(true) // Test only
                findNavController().navigateWithAnimation(R.id.tooManyDeviceFragment)
            }

            btnCreateAccountNumber.setOnClickListener {
                viewModel.saveIsUserLoggedIn(true) // Test only
                findNavController().navigateWithAnimation(R.id.accountNumberCreatedFragment)
            }
        }
    }

    private fun setupTextWatcher() {
        val accountNumberEditText = viewBinding.editTextAccountNumber
        accountNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString().replace(" ", "") // Remove spaces

                // Format the text
                val formatted = formatText(text)

                // Remove the listener to prevent infinite loop
                accountNumberEditText.removeTextChangedListener(this)

                // Set the formatted text
                accountNumberEditText.setText(formatted)

                // Move the cursor to the correct position
                accountNumberEditText.setSelection(formatted.length)

                // Re-attach the listener
                accountNumberEditText.addTextChangedListener(this)
            }

            private fun formatText(text: String): String {
                val formatted = StringBuilder()
                val length = text.length

                for (i in 0 until length) {
                    formatted.append(text[i])

                    // Add a space every 4 characters
                    if ((i + 1) % 4 == 0 && i != length - 1) {
                        formatted.append(" ")
                    }
                }
                return formatted.toString()
            }
        })
    }
}
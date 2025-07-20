package com.example.vpnapp.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentAccountNumberCreatedBinding
import com.example.vpnapp.ui.MainActivity
import com.example.vpnapp.ui.auth.viewModel.AccountNumberCreatedViewModel
import com.example.vpnapp.utils.extensions.navigateWithAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountNumberCreatedFragment : Fragment() {

    private lateinit var binding: FragmentAccountNumberCreatedBinding
    private lateinit var activity: MainActivity
    private val viewModel: AccountNumberCreatedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setUpViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity = requireActivity() as MainActivity
        binding = FragmentAccountNumberCreatedBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun setUpObservers() {
        viewModel.deviceNameWithStyle.observe(viewLifecycleOwner) { styledText ->
            binding.tvDeviceName.text = styledText
        }

        viewModel.animatedText.observe(viewLifecycleOwner) { animatedText ->
            binding.etAccountNumber.text = animatedText
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnBuyCredit.isEnabled = !isLoading
        }
    }

    private fun setUpViews() {
        binding.apply {
            val deviceName = "Deep Coyote" // Replace with your dynamic value
            viewModel.appendDeviceNameWithStyle(
                activity,
                "Device name:",
                deviceName
            )

            val targetText = "1115083471058536" // Replace with your desired final text
            viewModel.animateTextSelection(targetText)

            btnSettings.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.settingsFragment)
            }
            btnAccount.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.accountFragment)
            }
            binding.btnBuyCredit.setOnClickListener {
                //  TODO: Add navigation later
            }
        }
    }
}
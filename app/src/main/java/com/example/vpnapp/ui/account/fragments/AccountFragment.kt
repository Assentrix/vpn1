package com.example.vpnapp.ui.account.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vpnapp.databinding.FragmentAccountBinding
import com.example.vpnapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.apply {

            btnVisibility.setOnClickListener {
                if (etAccountNumber.inputType == InputType.TYPE_CLASS_TEXT) {
                    etAccountNumber.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                } else {
                    etAccountNumber.inputType = InputType.TYPE_CLASS_TEXT
                }
            }

            btnCopy.setOnClickListener {
                copyText(etAccountNumber.text.toString())
            }
        }
    }

    private fun copyText(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("accout_number", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, "Copied", Toast.LENGTH_LONG).show()
    }
}
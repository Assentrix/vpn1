package com.example.vpnapp.ui.tooManyDevice.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentTooManyDeviceBinding
import com.example.vpnapp.ui.MainActivity
import com.example.vpnapp.ui.tooManyDevice.adapter.DeviceListAdapter
import com.example.vpnapp.ui.tooManyDevice.model.LoggedDeviceInfo
import com.example.vpnapp.ui.tooManyDevice.viewModel.TooManyDeviceViewModel
import com.example.vpnapp.utils.extensions.navigateWithAnimation
import com.example.vpnapp.utils.extensions.showCustomDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TooManyDeviceFragment : Fragment() {

    private lateinit var viewBinding: FragmentTooManyDeviceBinding
    private val viewModel: TooManyDeviceViewModel by viewModels()
    private lateinit var activity: MainActivity
    private var adapter: DeviceListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTooManyDeviceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        setupObservers()
        setUpClickListeners()
        getLoggedDevices()
    }

    private fun setUpClickListeners() {
        viewBinding.btnContinueLogin.setOnClickListener {
            findNavController().navigateWithAnimation(R.id.homeFragment)
        }
    }

    private fun setupObservers() {
        viewModel.loggedDevices.observe(viewLifecycleOwner) { result ->
            when {
                result.isSuccess -> {
                    val deviceLoggedList = result.getOrNull()
                    if (deviceLoggedList != null) {
                        setupDeviceList(deviceLoggedList)
                    }
                }

                result.isFailure -> {
                    Toast.makeText(
                        activity,
                        getString(R.string.txt_failed_to_logged_devices), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            viewBinding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun getLoggedDevices() {
        viewModel.fetchLoggedDevices()
    }

    private fun setupDeviceList(deviceLoggedList: MutableList<LoggedDeviceInfo>) {
        viewBinding.apply {
            adapter = DeviceListAdapter(deviceLoggedList) { position ->
                activity.showCustomDialog(
                    deviceLoggedList[position].deviceName,
                    onSuccess = { adapter?.removeItem(position) },
                    onFailure = {
                        // TODO: Handle failure
                    }
                )
            }

            rvDeviceList.adapter = adapter
            rvDeviceList.layoutManager = LinearLayoutManager(activity)
        }
    }
}
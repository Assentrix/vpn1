package com.example.vpnapp.ui.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentVpnSettingsBinding
import com.example.vpnapp.ui.settings.adapter.SettingsAdapter
import com.example.vpnapp.ui.settings.model.SettingsInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VpnSettingsFragment : Fragment() {

    private lateinit var binding: FragmentVpnSettingsBinding
    private var adapter: SettingsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVpnSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setUpVpnSettingRecyclerView()
        setUpOnClickListeners()
    }

    private fun setUpVpnSettingRecyclerView() {
        adapter = SettingsAdapter(
            getVpnSettingsItems(),
            onSettingClicked = {
                Toast.makeText(activity, "Setting Item Clicked: $it", Toast.LENGTH_SHORT).show()
            },
            onInfoIconClicked = {
                Toast.makeText(
                    activity,
                    "Setting Item's Info Icon Clicked: $it",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.rvSettingsItems.adapter = adapter
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            tvBackTitle.setOnClickListener {
                findNavController().popBackStack()
            }
            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getVpnSettingsItems(): List<SettingsInfo> {
        return listOf(
            SettingsInfo(getString(R.string.dns_settings)),
            SettingsInfo(getString(R.string.server_ip_override)),
            SettingsInfo(getString(R.string.wireguardb_ports), topMargin = 16, hasInfoIcon = true),
            SettingsInfo(getString(R.string.wireguardb_obfuscation), hasInfoIcon = true),
            SettingsInfo(getString(R.string.udp_over_tcp_port), hasInfoIcon = true),
            SettingsInfo(getString(R.string.quantum_resistant_tunnel), hasInfoIcon = true)
        )
    }
}
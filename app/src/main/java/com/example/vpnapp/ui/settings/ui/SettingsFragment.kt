package com.example.vpnapp.ui.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpnapp.databinding.FragmentSettingsBinding
import com.example.vpnapp.ui.MainActivity
import com.example.vpnapp.ui.settings.adapter.SettingsAdapter
import com.example.vpnapp.ui.settings.viewmodel.SettingsViewModel
import com.example.vpnapp.utils.extensions.navigateWithAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var activity: MainActivity
    private val viewModel by viewModels<SettingsViewModel>()
    private var adapter: SettingsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as MainActivity
        lifecycleScope.launch {
            setUpRecyclerView()
        }
    }

    /**
     * Configures the RecyclerView with a list of settings options provided by the ViewModel.
     */
    private suspend fun setUpRecyclerView() {
        val settingsOptions = viewModel.getSettingsOptions()
        adapter = SettingsAdapter(settingsOptions) { destinationId ->
            findNavController().navigateWithAnimation(destinationId)
        }
        binding.rvSettingsItems.adapter = adapter
    }
}
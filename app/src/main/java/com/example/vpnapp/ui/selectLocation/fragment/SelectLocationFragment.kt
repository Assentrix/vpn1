package com.example.vpnapp.ui.selectLocation.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vpnapp.R
import com.example.vpnapp.databinding.FragmentSelectLocationBinding
import com.example.vpnapp.ui.selectLocation.adapter.CountryAdapter
import com.example.vpnapp.ui.selectLocation.adapter.ProviderAdapter
import com.example.vpnapp.ui.selectLocation.model.City
import com.example.vpnapp.ui.selectLocation.model.Country
import com.example.vpnapp.ui.selectLocation.model.Provider
import com.example.vpnapp.ui.selectLocation.model.Server

class SelectLocationFragment : Fragment() {

    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var providerAdapter: ProviderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(
            inflater,
            container,
            false
        )
        setUpClickListeners()
        setupRadioGroupTint()
        setUpCountryList()
        setupProvidersList()
        return binding.root
    }

    private fun setUpClickListeners() {
        binding.apply {
            tvFilter.setOnClickListener {
                tvFilter.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.textLightGray
                    )
                )
                tvDoneCancel.setText(R.string.txt_cancel)
                normalScreenGroup.visibility = View.GONE
                groupProvider.visibility = View.VISIBLE
            }
            tvDoneCancel.setOnClickListener {
                if (tvDoneCancel.text == resources.getString(R.string.txt_cancel)) {
                    tvDoneCancel.setText(R.string.txt_done)
                    tvFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    normalScreenGroup.visibility = View.VISIBLE
                    groupProvider.visibility = View.GONE
                } else {
                    findNavController().popBackStack()
                }
                tvFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            // Button to get checked items
            btnApplyFilter.setOnClickListener {
                val checkedItems = providerAdapter.getCheckedItems()
                Toast.makeText(
                    requireContext(),
                    "Checked: ${checkedItems.map { it.text }}",
                    Toast.LENGTH_SHORT
                ).show()
                tvDoneCancel.setText(R.string.txt_done)
                tvFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                normalScreenGroup.visibility = View.VISIBLE
                groupProvider.visibility = View.GONE
            }
            imgArrowOwnership.setOnClickListener {
                if (radioGroupOwnership.visibility == View.VISIBLE) {
                    radioGroupOwnership.visibility = View.GONE
                } else {
                    radioGroupOwnership.visibility = View.VISIBLE
                }
            }
            imgArrowProviders.setOnClickListener {
                frameLayoutProviderRv.visibility =
                    if (frameLayoutProviderRv.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }

    private fun setupRadioGroupTint() {
        binding.apply {
            for (i in 0 until radioGroupOwnership.childCount) {
                val view = radioGroupOwnership.getChildAt(i)
                if (view is RadioButton) {
                    view.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                }
            }
        }
    }

    private fun setupProvidersList() {
        val items = mutableListOf(
            Provider("All Providers", true),
            Provider("Test 1", false),
            Provider("Test 2", false),
            Provider("Test 3", false),
            Provider("Test 4", false),
        )
        // Set up RecyclerView
        providerAdapter = ProviderAdapter(items)
        binding.rvProviders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProviders.adapter = providerAdapter
    }

    private fun setUpCountryList() {

        binding.apply {
            rvParentCountry.layoutManager = LinearLayoutManager(requireContext())

            // Sample Data
            val countries = listOf(
                Country(
                    "Albania",
                    listOf(
                        City(
                            "Tirana",
                            listOf(Server("Server 1", false), Server("Server 2", false))
                        )
                    )
                ),
                Country(
                    "India",
                    listOf(
                        City("Delhi", listOf(Server("Server 3", false))),
                        City("Mumbai", listOf(Server("Server 4", false))),
                        City("Bangalore", listOf(Server("Server 5", false)))
                    )
                ),
                Country("Australia", listOf(City("Sydney", listOf(Server("Server 4", false))))),
            )
            rvParentCountry.adapter =
                CountryAdapter(countries) { countryPosition, cityPosition, serverPosition ->

                }
        }
    }
}

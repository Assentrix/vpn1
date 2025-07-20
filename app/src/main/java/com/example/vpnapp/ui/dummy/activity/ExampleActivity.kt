package com.example.vpnapp.ui.dummy.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.vpnapp.databinding.ActivityExampleBinding
import com.example.vpnapp.ui.dummy.viewModel.ExampleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExampleBinding
    private val viewModel: ExampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonFetchData.setOnClickListener {
            viewModel.fetchExampleData()
        }
    }

    private fun setupObservers() {
        viewModel.exampleData.observe(this, Observer { result ->
            when {
                result.isSuccess -> {
                    val data = result.getOrNull()
                    binding.textViewData.text = "ID: ${data?.id}\nName: ${data?.name}\nDescription: ${data?.description}"
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()
                    binding.textViewData.text = "Error: ${error?.message}"
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonFetchData.isEnabled = !isLoading
        })
    }
}
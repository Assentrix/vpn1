package com.example.vpnapp.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.databinding.ItemSettingsRowBinding
import com.example.vpnapp.ui.settings.model.SettingsInfo
import com.example.vpnapp.utils.extensions.dpToPx

class SettingsAdapter(
    private val items: List<SettingsInfo>,
    private val onInfoIconClicked: ((Int) -> Unit)? = null,
    private val onSettingClicked: (Int) -> Unit,
) : RecyclerView.Adapter<SettingsAdapter.ItemViewHolder>() {

    private enum class SettingItemType {
        NO_MARGIN,
        TOP_MARGIN,
        BOTTOM_MARGIN,
        VERTICAL_MARGIN
    }

    inner class ItemViewHolder(private val binding: ItemSettingsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    onSettingClicked(items[adapterPosition].destinationId)
                }

                btnInfoAboutSetting.setOnClickListener {
                    onInfoIconClicked?.let { it1 -> it1(adapterPosition) }
                }
            }
        }

        fun bind(item: SettingsInfo) {
            binding.apply {
                tvItemTitle.text = item.title
                btnInfoAboutSetting.isVisible = item.hasInfoIcon
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemSettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.apply {
            when (viewType) {
                SettingItemType.TOP_MARGIN.ordinal -> {
                    topMargin = 16.dpToPx(parent.context)
                }

                SettingItemType.BOTTOM_MARGIN.ordinal -> {
                    bottomMargin = 16.dpToPx(parent.context)
                }

                SettingItemType.VERTICAL_MARGIN.ordinal -> {
                    topMargin = 16.dpToPx(parent.context)
                    bottomMargin = 16.dpToPx(parent.context)
                }
            }
            binding.root.layoutParams = this
        }

        return ItemViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].topMargin != 0 && items[position].bottomMargin == 0) {
            SettingItemType.TOP_MARGIN.ordinal
        } else if (items[position].topMargin == 0 && items[position].bottomMargin != 0) {
            SettingItemType.BOTTOM_MARGIN.ordinal
        } else if (items[position].topMargin != 0 && items[position].bottomMargin != 0) {
            SettingItemType.VERTICAL_MARGIN.ordinal
        } else {
            SettingItemType.NO_MARGIN.ordinal
        }
    }
}
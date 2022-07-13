package net.decentr.module_decentr_staking.presentation.staking.items

import android.os.Build
import android.view.View
import android.widget.Checkable
import androidx.core.graphics.drawable.toDrawable
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.ItemRewardBinding
import net.decentr.module_decentr_staking.databinding.ItemRewardLegendBinding
import net.decentr.module_decentr_staking.presentation.base.adapter.recycler.BaseListItem
import net.decentr.module_decentr_staking.presentation.extensions.getColorById
import net.decentr.module_decentr_staking.presentation.extensions.gone
import net.decentr.module_decentr_staking.presentation.extensions.show
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardLegendViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardsViewState

sealed class RewardsItem(vs: RewardsViewState) : BaseListItem<RewardsViewState>(vs) {
    companion object {
        fun newInstance(viewState: RewardsViewState): RewardsItem =
            when (viewState) {
                is RewardViewState -> RewardItem(viewState)
                is RewardLegendViewState -> RewardLegendItem(viewState)
            }
    }
}

class RewardItem(viewState: RewardViewState) : RewardsItem(viewState), Checkable {

    override fun getViewId(): Int = R.layout.item_reward

    private lateinit var binding: ItemRewardBinding

    override fun renderView(view: View, positionInAdapter: Int) {
        if (viewState !is RewardViewState) return
        binding = ItemRewardBinding.bind(view)
        with(binding) {
            setChecked(false)
            validator.text = viewState.validatorName
            if (viewState.rewardAmount.isNotEmpty() && (viewState.rewardAmount.toDoubleOrNull() ?: 0.0) > 0.0) {
                containerReward.show()
                containerReward.invalidate()
                reward.text = viewState.rewardAmount
            } else {
                containerReward.gone()
            }
            delegated.text = viewState.delegated
        }
    }

    private var isChecked = false

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        val context = binding.container.context
        isChecked = checked
        if (checked) {
            binding.statusChecked.setImageResource(R.drawable.ic_rewards_checked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.container.foreground =
                    context.getColorById(R.color.dec_blue_hover_10).toDrawable()
            } else {
                binding.container.setBackgroundColor(context.getColorById(R.color.dec_blue_hover_10))
            }
        } else {
            binding.statusChecked.setImageResource(R.drawable.ic_rewards_unchecked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.container.foreground = null
            } else {
                binding.container.setBackgroundColor(context.getColorById(R.color.dec_white))
            }
        }

    }

}

class RewardLegendItem(viewState: RewardLegendViewState) : RewardsItem(viewState) {

    override fun getViewId(): Int = R.layout.item_reward_legend

    private lateinit var binding: ItemRewardLegendBinding

    override fun renderView(view: View, positionInAdapter: Int) {
        if (viewState !is RewardLegendViewState) return
        binding = ItemRewardLegendBinding.bind(view)
        with(binding) {
            withdraw.text = viewState.withdraw
            fee.text = viewState.fee
        }
    }
}
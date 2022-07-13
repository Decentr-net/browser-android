package net.decentr.module_decentr_staking.presentation.staking.items

import android.annotation.SuppressLint
import android.view.View
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.ItemValidatorBinding
import net.decentr.module_decentr_staking.presentation.base.adapter.recycler.BaseListItem
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import java.text.DecimalFormat

class ValidatorItem(viewState: ValidatorViewState): BaseListItem<ValidatorViewState>(viewState) {

    override fun getViewId(): Int = R.layout.item_validator

    private lateinit var binding: ItemValidatorBinding

    @SuppressLint("SetTextI18n")
    override fun renderView(view: View, positionInAdapter: Int) {
        binding = ItemValidatorBinding.bind(view)
        with(binding) {
            val decimalFormat = DecimalFormat("0.000000")

            name.text = viewState.name
            votingPower.text = viewState.votingPower + " (" + viewState.votingPowerPercent + "%)"
            status.text = viewState.status.name
            comission.text = viewState.commission
            amount.text = decimalFormat.format(viewState.delegatedAmount.toBigDecimal())
        }
    }

}
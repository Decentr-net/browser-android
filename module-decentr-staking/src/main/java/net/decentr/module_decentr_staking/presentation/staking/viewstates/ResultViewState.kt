package net.decentr.module_decentr_staking.presentation.staking.viewstates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.decentr.module_decentr_staking.presentation.staking.ValidatorActionsViewModel

@Parcelize
class ResultViewState(
    val type: ValidatorActionsViewModel.ActionType,
    val fromValidator: String?,
    val toValidator: String?,
    val decAmount: String
): Parcelable
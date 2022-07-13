package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr_staking.NavStakingDirections
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentActionValidatorResultBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.extensions.copyToClipboard
import net.decentr.module_decentr_staking.presentation.extensions.textAppearance
import net.decentr.module_decentr_staking.presentation.extensions.textClick
import net.decentr.module_decentr_staking.presentation.extensions.textUnderline
import javax.inject.Inject

class ValidatorActionResult : BaseFragment(R.layout.fragment_action_validator_result) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var _binding: FragmentActionValidatorResultBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ValidatorActionResultArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionValidatorResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
            when (args.result.type) {
                ValidatorActionsViewModel.ActionType.DELEGATE -> {
                    result.text = getString(R.string.validator_result_delegate_success)
//                    val spannable = Spannable()
                    val span = SpannableString(getString(
                        R.string.validator_result_delegate_info,
                        args.result.toValidator,
                        args.result.decAmount
                    ))
                    args.result.toValidator?.let {
                        span.textAppearance(context, R.style.Base_Bold_Primary, it)
                        span.textUnderline(it)
                        span.textClick(it) {
                            context?.copyToClipboard(it)
                        }
                    }
                    info.movementMethod = LinkMovementMethod.getInstance()
                    info.text = span
                }
                ValidatorActionsViewModel.ActionType.REDELEGATE -> {
                    result.text = getString(R.string.validator_result_redelegate_success)
                    val span = SpannableString(getString(
                        R.string.validator_result_redelegate_info,
                        args.result.fromValidator,
                        args.result.toValidator,
                        args.result.decAmount
                    ))
                    args.result.fromValidator?.let {
                        span.textAppearance(context, R.style.Base_Bold_Primary, it)
                        span.textUnderline(it)
                        span.textClick(it) {
                            context?.copyToClipboard(it)
                        }
                    }
                    args.result.toValidator?.let {
                        span.textAppearance(context, R.style.Base_Bold_Primary, it)
                        span.textUnderline(it)
                        span.textClick(it) {
                            context?.copyToClipboard(it)
                        }
                    }
                    info.movementMethod = LinkMovementMethod.getInstance()
                    info.text = span
                }
                ValidatorActionsViewModel.ActionType.UNDELEGATE -> {
                    result.text = getString(R.string.validator_result_undelegate_success)
                    val span = SpannableString(getString(
                        R.string.validator_result_undelegate_info,
                        args.result.fromValidator,
                        args.result.decAmount
                    ))
                    args.result.fromValidator?.let {
                        span.textAppearance(context, R.style.Base_Bold_Primary, it)
                        span.textUnderline(it)
                        span.textClick(it) {
                            context?.copyToClipboard(it)
                        }
                    }
                    info.movementMethod = LinkMovementMethod.getInstance()
                    info.text = span
                }
            }

            action.setOnClickListener {
                view?.findNavController()?.popBackStack(R.id.pagerFragment, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
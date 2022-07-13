package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.postDelayed
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentActionsValidatorBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.base.adapter.KEY_TITLE
import net.decentr.module_decentr_staking.presentation.extensions.*
import net.decentr.module_decentr_staking.presentation.views.searchablespinner.OnItemSelectListener
import net.decentr.module_decentr_staking.presentation.views.searchablespinner.SearchableSpinner
import net.decentr.module_decentr_staking.presentation.views.searchablespinner.SpinnerViewState
import net.decentr.module_decentr_staking.remote.mapper.DIVIDER_SMALL
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class ValidatorActionsFragment : BaseFragment(R.layout.fragment_actions_validator) {

    companion object {
        const val SCREEN_TYPE_DELEGATE = "SCREEN_TYPE_DELEGATE"
        const val SCREEN_TYPE_UNDELEGATE = "SCREEN_TYPE_UNDELEGATE"
        const val SCREEN_TYPE_REDELEGATE = "SCREEN_TYPE_REDELEGATE"

        fun getArgs(type: String) = bundleOf(
            KEY_TITLE to when (type) {
                SCREEN_TYPE_DELEGATE -> R.string.validator_delegate_action
                SCREEN_TYPE_UNDELEGATE -> R.string.validator_undelegate_action
                SCREEN_TYPE_REDELEGATE -> R.string.validator_redelegate_action
                else -> ""
            }
        )
    }

    private val args by navArgs<ValidatorActionsFragmentArgs>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<ValidatorActionsViewModel>(factory) }

    private var _binding: FragmentActionsValidatorBinding? = null
    private val binding get() = _binding!!

    private var searchableSpinner: SearchableSpinner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionsValidatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        with(binding) {
            when (args.screenType) {
                SCREEN_TYPE_DELEGATE -> {
                    setTitle(R.string.validator_delegate_action)
                    infoDelegate.show()
                    infoUndelegate.gone()
                    iconSpinner.gone()
                    action.text = getString(R.string.validator_delegate_action)
                    actionTypeHint.text =
                        getString(R.string.validator_actions_delegated_amount_hint)
                    withdrawComission.text =
                        getString(
                            R.string.validator_actions_withdraw_commission,
                            args.validator.commission
                        )
                    validatorName.text = args.validator.name
                }
                SCREEN_TYPE_UNDELEGATE -> {
                    setTitle(R.string.validator_undelegate_action)
                    infoDelegate.gone()
                    infoUndelegate.show()
                    iconSpinner.gone()
                    action.text = getString(R.string.validator_undelegate_action)
                    actionTypeHint.text =
                        getString(R.string.validator_actions_undelegated_amount_hint)
                    withdrawComission.text =
                        getString(
                            R.string.validator_actions_withdraw_commission,
                            args.validator.commission
                        )
                    validatorName.text = args.validator.name
                }
                SCREEN_TYPE_REDELEGATE -> {
                    setTitle(R.string.validator_redelegate_action)
                    infoDelegate.gone()
                    infoUndelegate.gone()
                    iconSpinner.show()
                    action.text = getString(R.string.validator_redelegate_action)
                    actionTypeHint.text =
                        getString(R.string.validator_actions_redelegated_amount_hint)
                    initRedelegateViews()
                }
            }

            operationFee.text = getString(R.string.validator_actions_fee, "0")
            operationAmount
                .textChanges()
                .debounce(1000)
                .onEach {
                    calculateFee(it.toString())
                }
                .launchIn(lifecycleScope)
            operationAmount.requestFocus()

            action.setOnClickListener {
                action()
            }
        }
    }

    private fun initRedelegateViews() {
        viewModel.getValidators()
        searchableSpinner = SearchableSpinner(requireContext())
    }

    private fun calculateFee(value: String) {
        val amount = value.trim().toDoubleOrNull()
        if (validate(0.0))
            when (val type = getActionType()) {
                ValidatorActionsViewModel.ActionType.DELEGATE -> {
                    val toValidator = args.validator.address
                    if (amount != null && amount > 0)
                        viewModel.calculateFee(type, toValidator, null, amount)
                }
                ValidatorActionsViewModel.ActionType.REDELEGATE -> {
                    val fromValidator = args.validator.address
                    val toValidator = viewModel.getToValidator()

                    if (amount != null && amount > 0 && toValidator != null)
                        viewModel.calculateFee(type, toValidator.address, fromValidator, amount)
                }
                ValidatorActionsViewModel.ActionType.UNDELEGATE -> {
                    val fromValidator = args.validator.address
                    if (amount != null && amount > 0)
                        viewModel.calculateFee(type, null, fromValidator, amount)
                }
            }
    }

    private fun action() {
        val type = getActionType()
        viewModel.fee.value?.let { fee ->
            val amount = binding.operationAmount.string().trim().toDoubleOrNull()
            if (amount != null && amount > 0) {
                when (type) {
                    ValidatorActionsViewModel.ActionType.DELEGATE -> {
                        val toValidator = args.validator.address
                        viewModel.action(type, toValidator, null, amount, fee)
                    }
                    ValidatorActionsViewModel.ActionType.REDELEGATE -> {
                        val fromValidator = args.validator.address
                        val toValidator = viewModel.getToValidator()
                        if (toValidator != null) viewModel.action(
                            type,
                            toValidator.address,
                            fromValidator,
                            amount,
                            fee
                        )
                    }
                    ValidatorActionsViewModel.ActionType.UNDELEGATE -> {
                        val fromValidator = args.validator.address
                        viewModel.action(type, null, fromValidator, amount, fee)
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            when (getActionType()) {
                ValidatorActionsViewModel.ActionType.DELEGATE -> {
                    binding.balance.text =
                        getString(R.string.validator_actions_balance, balance.decAmount.toString())
                    viewModel.setMaxAvailable(balance.decAmount)
                }
                ValidatorActionsViewModel.ActionType.REDELEGATE,
                ValidatorActionsViewModel.ActionType.UNDELEGATE -> {
                    binding.balance.text =
                        getString(
                            R.string.validator_actions_available,
                            args.validator.delegatedAmount
                        )
                    args.validator.delegatedAmount.toDoubleOrNull()?.let {
                        viewModel.setMaxAvailable(it)
                    }
                }
            }
        }
        viewModel.fee.observe(viewLifecycleOwner) { fee ->
            validate(fee)
            val decimalFormat = DecimalFormat("0.00000")
            binding.operationFee.text = getString(
                R.string.validator_actions_fee,
                decimalFormat.format(fee).removeEndingZeros().trimEnd('.')
            )
        }
        viewModel.validatorViewStates.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    toast(result.exception.message)
                    binding.progress.gone()
                }
                Result.Loading -> {
                    binding.progress.show()
                }
                is Result.Success -> {
                    binding.progress.gone()
                    binding.validatorName.text = result.data.name
                    binding.withdrawComission.text = getString(
                        R.string.validator_actions_withdraw_commission,
                        result.data.commission
                    )
                }
            }
        }
        viewModel.validatorsList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val validatorsTo = result.data.filter { it.address != args.validator.address }

                    binding.withdrawComission.text =
                        getString(
                            R.string.validator_actions_withdraw_commission,
                            result.data.first().commission
                        )
                    binding.validatorName.text = validatorsTo.first().name

                    viewModel.setToValidator(validatorsTo.first())

                    searchableSpinner?.windowTitle = getString(R.string.title_pager_validators)
                    searchableSpinner?.spinnerCancelable = true
                    searchableSpinner?.dismissSpinnerOnItemClick = true
                    searchableSpinner?.onItemSelectListener = object : OnItemSelectListener {
                        override fun setOnItemSelectListener(
                            position: Int,
                            selectedItem: SpinnerViewState
                        ) {
                            val validator =
                                validatorsTo.first { it.address == selectedItem.identifier }
                            binding.validatorName.text = validator.name
                            binding.withdrawComission.text = getString(
                                R.string.validator_actions_withdraw_commission,
                                validator.commission
                            )

                            viewModel.setToValidator(validator)
                        }
                    }
                    val spinnerViewStatesList = validatorsTo.map {
                        SpinnerViewState(
                            primaryText = it.name,
                            secondaryText = it.commission + "%",
                            identifier = it.address
                        )
                    }
                    searchableSpinner?.setSpinnerListItems(spinnerViewStatesList)

                    binding.validatorNameContainer.setOnClickListener {
                        searchableSpinner?.show()
                    }
                }
            }
        }
        viewModel.actionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.exception.cause?.cause?.message ?: "Oops... Unknown issue",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progress.gone()
                }
                Result.Loading -> {
                    binding.progress.show()
                }
                is Result.Success -> {
                    binding.progress.postDelayed(500) {
                        binding.progress.gone()
                        navigateTo(
                            ValidatorActionsFragmentDirections.actionValidatorActionsFragmentToResultFragment(
                                result.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun validate(fee: Double): Boolean {
        val amount = binding.operationAmount.string().trim().toDoubleOrNull()
        val maxAvailable = viewModel.getMaxAvailable()
        val minAmount = 0.000001

        return when {
            amount == null -> {
                TextViewCompat.setTextAppearance(binding.balance, R.style.Small_Regular_Red)
                binding.errorMessage.show()
                binding.errorMessage.text = getString(R.string.validator_actions_amount_error)

                binding.action.isEnabled = false
                false
            }
            amount < minAmount -> {
                TextViewCompat.setTextAppearance(binding.balance, R.style.Small_Regular_Red)
                binding.errorMessage.show()
                binding.errorMessage.text = getString(R.string.validator_actions_amount_min_error)

                binding.action.isEnabled = false
                return false
            }
            amount + fee > maxAvailable -> {
                TextViewCompat.setTextAppearance(binding.balance, R.style.Small_Regular_Red)
                binding.errorMessage.show()
                binding.errorMessage.text = getString(R.string.validator_actions_error)

                binding.action.isEnabled = false
                false
            }
            else -> {
                TextViewCompat.setTextAppearance(binding.balance, R.style.Small_Regular_Secondary)
                binding.errorMessage.invisible()

                binding.action.isEnabled = true
                true
            }
        }
    }

    private fun getActionType(): ValidatorActionsViewModel.ActionType {
        return when (args.screenType) {
            SCREEN_TYPE_DELEGATE -> ValidatorActionsViewModel.ActionType.DELEGATE
            SCREEN_TYPE_UNDELEGATE -> ValidatorActionsViewModel.ActionType.UNDELEGATE
            SCREEN_TYPE_REDELEGATE -> ValidatorActionsViewModel.ActionType.REDELEGATE
            else -> throw IllegalStateException("unknown action type")
        }
    }

    override fun onStop() {
        viewModel.close()
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
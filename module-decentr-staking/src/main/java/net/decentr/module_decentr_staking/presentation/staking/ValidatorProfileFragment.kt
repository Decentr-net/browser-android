package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentProfileValidatorBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.base.adapter.KEY_TITLE
import net.decentr.module_decentr_staking.presentation.extensions.*
import net.decentr.module_decentr_staking.presentation.staking.ValidatorActionsFragment.Companion.SCREEN_TYPE_DELEGATE
import net.decentr.module_decentr_staking.presentation.staking.ValidatorActionsFragment.Companion.SCREEN_TYPE_REDELEGATE
import net.decentr.module_decentr_staking.presentation.staking.ValidatorActionsFragment.Companion.SCREEN_TYPE_UNDELEGATE
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import javax.inject.Inject

class ValidatorProfileFragment : BaseFragment(R.layout.fragment_profile_validator) {

    companion object {

        fun getArgs() = bundleOf(
            KEY_TITLE to R.string.title_validator
        )
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<ValidatorProfileViewModel>(factory) }

    private var _binding: FragmentProfileValidatorBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ValidatorProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileValidatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        with(binding) {
            refresh.setOnRefreshListener {
                viewModel.loadValidator(args.validator.address)
            }
            website.setOnClickListener {
                context?.copyToClipboard(website.text.toString())
            }
            address.setOnClickListener {
                context?.copyToClipboard(website.text.toString())
            }
        }
        setValidator(args.validator)
        setActionsListeners()
    }

    private fun setActionsListeners() {
        with(binding) {
            if (args.validator.isJailed) {
                jailedStatus.show()

                actionRedelegate.isEnabled = false
                actionUndelegate.isEnabled = false
                actionDelegate.isEnabled = false
            } else {
                jailedStatus.gone()

                actionDelegate.setOnClickListener {
                    navigateTo(
                        ValidatorProfileFragmentDirections.actionValidatorProfileFragmentToValidatorActionsFragment(
                            screenType = SCREEN_TYPE_DELEGATE,
                            validator = (viewModel.validatorViewStates.value as? Result.Success)?.data
                                ?: args.validator
                        )
                    )
                }

                if (args.validator.delegatedAmount.toDoubleOrNull() != null && args.validator.delegatedAmount.toDouble() > 0.0) {
                    actionUndelegate.isEnabled = true
                    actionUndelegate.setOnClickListener {
                        navigateTo(
                            ValidatorProfileFragmentDirections.actionValidatorProfileFragmentToValidatorActionsFragment(
                                screenType = SCREEN_TYPE_UNDELEGATE,
                                validator = (viewModel.validatorViewStates.value as? Result.Success)?.data
                                    ?: args.validator
                            )
                        )
                    }
                    actionRedelegate.isEnabled = true
                    actionRedelegate.setOnClickListener {
                        navigateTo(
                            ValidatorProfileFragmentDirections.actionValidatorProfileFragmentToValidatorActionsFragment(
                                screenType = SCREEN_TYPE_REDELEGATE,
                                validator = (viewModel.validatorViewStates.value as? Result.Success)?.data
                                    ?: args.validator
                            )
                        )
                    }
                } else {
                    actionUndelegate.isEnabled = false
                    actionRedelegate.isEnabled = false
                }
            }
        }
    }

    private fun setValidator(viewState: ValidatorViewState) {
        with(binding) {
            name.text = viewState.name
            comission.text = viewState.commission
            delegatedAmount.text = getString(R.string.validator_actions_delegated_amount_value, viewState.delegatedAmount)
            address.text = viewState.address
            description.text = getSpannableField(
                viewState.description,
                getString(R.string.validator_not_loaded_description_hint)
            )
            website.text = getSpannableField(
                viewState.website,
                getString(R.string.validator_not_loaded_website_hint)
            )

            when (viewState.status) {
                ValidatorViewState.Status.UNSPECIFIED -> {
                    textStatus.text = getString(R.string.validator_status_unspecified)
                    imageStatus.setImageResource(R.drawable.ic_status_unbonded)
                    containerStatus.setBackgroundResource(R.drawable.shape_bg_grey_rounded)
                }
                ValidatorViewState.Status.UNBONDED -> {
                    textStatus.text = getString(R.string.validator_status_not_bonded)
                    imageStatus.setImageResource(R.drawable.ic_status_unbonded)
                    containerStatus.setBackgroundResource(R.drawable.shape_bg_grey_rounded)
                }
                ValidatorViewState.Status.UNBONDING -> {
                    textStatus.text = getString(R.string.validator_status_unbonding)
                    imageStatus.setImageResource(R.drawable.ic_status_unbonding)
                    containerStatus.setBackgroundResource(R.drawable.shape_bg_grey_rounded)
                }
                ValidatorViewState.Status.BONDED -> {
                    textStatus.text = getString(R.string.validator_status_bonded)
                    imageStatus.setImageResource(R.drawable.ic_status_bonded)
                    containerStatus.setBackgroundResource(R.drawable.shape_bg_green_50_rounded)
                }
            }
            containerStatus.show()
        }
    }

    private fun getSpannableField(text: String?, hint: String): Spannable {
        return SpannableString(text ?: hint).apply {
            if (text.isNullOrEmpty()) {
                textAppearance(context, R.style.Base_Regular_Secondary, hint)
            } else {
                textAppearance(context, R.style.Base_Regular_Primary, text)
            }
        }
    }

    private fun initViewModel() {
        viewModel.validatorViewStates.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.refresh.isRefreshing = false
                    toast(result.exception.message)
                }
                Result.Loading -> {
                    binding.refresh.isRefreshing = true
                }
                is Result.Success -> {
                    binding.refresh.isRefreshing = false
                    setValidator(result.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadValidator(args.validator.address)
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
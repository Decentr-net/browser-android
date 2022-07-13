package net.decentr.module_decentr_staking.presentation.staking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentValidatorsBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.base.adapter.KEY_TITLE
import net.decentr.module_decentr_staking.presentation.extensions.gone
import net.decentr.module_decentr_staking.presentation.extensions.injectViewModel
import net.decentr.module_decentr_staking.presentation.extensions.show
import net.decentr.module_decentr_staking.presentation.extensions.toast
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import javax.inject.Inject

class ValidatorsFragment : BaseFragment(R.layout.fragment_validators) {

    companion object {

        fun getArgs() = bundleOf(
            KEY_TITLE to R.string.title_pager_validators
        )
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<ValidatorsViewModel>(factory) }

    private var _binding: FragmentValidatorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentValidatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        with(binding) {
            refresh.setOnRefreshListener {
                viewModel.getValidators()
            }
        }
    }

    private fun initViewModel() {
        viewModel.viewStatesList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.refresh.isRefreshing = false
                    binding.errorMessage.text = String()
                    binding.progress.gone()
                    binding.errorMessage.show()
                    binding.errorMessage.text = result.exception.message
                    Log.d("GRPC", result.exception.message.toString())
                }
                Result.Loading -> {
                    binding.errorMessage.gone()
                    binding.progress.show()
                }
                is Result.Success -> {
                    binding.refresh.isRefreshing = false
                    binding.errorMessage.gone()
                    binding.progress.gone()

                    if (result.data.isNotEmpty()) {
                        binding.table.show()
                        binding.table.setActualYListener { y ->
                            binding.refresh.isEnabled = y == 0
                        }
                        setAdapter(result.data)
                    } else {
                        binding.errorMessage.show()
                        binding.errorMessage.text = getString(R.string.common_empty_list)
                    }
                }
            }
        }
    }

    private fun onValidatorClick(item: ValidatorViewState) {
        navigateTo(StakingViewPagerFragmentDirections.actionPagerFragmentToValidatorProfileFragment(item))
    }

    private fun setAdapter(validators: List<ValidatorViewState>) {
        val matrixTableAdapter: ValidatorsTableAdapter<String> = ValidatorsTableAdapter(
            requireContext(), validators.toTypedArray(), listener = {
                onValidatorClick(it)
            }
        )
        binding.table.adapter = matrixTableAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getValidators()
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
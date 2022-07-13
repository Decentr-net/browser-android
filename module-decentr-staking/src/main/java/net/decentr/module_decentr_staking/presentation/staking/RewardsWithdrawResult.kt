package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentRewardsWithdrawResultBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import javax.inject.Inject

class RewardsWithdrawResult : BaseFragment(R.layout.fragment_rewards_withdraw_result) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var _binding: FragmentRewardsWithdrawResultBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ValidatorActionResultArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsWithdrawResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
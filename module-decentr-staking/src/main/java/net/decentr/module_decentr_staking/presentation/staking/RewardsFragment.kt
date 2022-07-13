package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.core.os.bundleOf
import androidx.core.view.postDelayed
import androidx.lifecycle.ViewModelProvider
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentRewardsBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.base.adapter.KEY_TITLE
import net.decentr.module_decentr_staking.presentation.base.adapter.recycler.BaseRecyclerAdapter
import net.decentr.module_decentr_staking.presentation.extensions.*
import net.decentr.module_decentr_staking.presentation.staking.items.RewardItem
import net.decentr.module_decentr_staking.presentation.staking.items.RewardsItem
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardLegendViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardsViewState
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class RewardsFragment : BaseFragment(R.layout.fragment_rewards) {

    companion object {

        fun getArgs() = bundleOf(
            KEY_TITLE to R.string.title_pager_rewards
        )
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<RewardsViewModel>(factory) }

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        BaseRecyclerAdapter(
            mapper = ::mapToItem,
            onItemClickListener = ::onRewardClick
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.recycler.adapter = adapter
        binding.refresh.setOnRefreshListener {
            viewModel.getRewards(viewModel.address)
        }
        binding.actionWithdraw.setOnClickListener {
            val viewStatesChecked = adapter.dataset.filter { (it as? Checkable)?.isChecked == true }.map { it.viewState }
            val array = viewStatesChecked.map { (it as? RewardViewState)?.validatorAddress }.toTypedArray()
            if (!array.isNullOrEmpty()) viewModel.fee.value?.let { viewModel.withdraw(arrayListOf<String?>().apply { addAll(array) }, it) }
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
//                    toast(result.exception.message)
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
                        binding.headersContainer.show()
                        val itemsWithLegend = mutableListOf<RewardsViewState>()
                        itemsWithLegend.addAll(result.data)
                        itemsWithLegend.add(RewardLegendViewState(
                            withdraw = getString(
                                R.string.rewards_legend_withdraw,
                                "0",
                                "0"
                            ),
                            fee = getString(R.string.rewards_legend_fee, "0")
                        ))
                        adapter.replaceElements(itemsWithLegend)
                        adapter.notifyDataSetChanged()
                        calculateLegendValues(adapter.dataset as List<RewardsItem>)
                    } else {
                        binding.headersContainer.invisible()
                        binding.errorMessage.show()
                        binding.errorMessage.text = getString(R.string.common_empty_list)
                    }
                }
            }
        }
        viewModel.fee.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.actionWithdraw.isEnabled = true

                val decimalFormat = DecimalFormat("0.000000")
                val formattedFee = decimalFormat.format(it.toBigDecimal())

                val legendViewState =
                    (adapter.dataset.last().viewState as? RewardLegendViewState)?.copy(
                        fee = getString(R.string.rewards_legend_fee, formattedFee)
                    )
                legendViewState?.let { vs ->
                    adapter.replaceElement(vs, adapter.dataset.lastIndex)
                    adapter.notifyItemChanged(adapter.dataset.lastIndex)
                }
            }
        }
        viewModel.withdrawResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.refresh.isRefreshing = false
                    binding.errorMessage.text = String()
                    binding.progress.gone()
                    binding.errorMessage.show()
                    binding.errorMessage.text = result.exception.message
                    toast(result.exception.message)
                    Log.d("GRPC", result.exception.message.toString())
                }
                Result.Loading -> {
                    binding.errorMessage.gone()
                    binding.progress.show()
                }
                is Result.Success -> {
                    binding.refresh.isRefreshing = false
                    binding.errorMessage.text = String()
                    binding.progress.gone()
                }
            }
        }
    }

    private fun calculateLegendValues(list: List<RewardsItem>) {
        binding.actionWithdraw.isEnabled = false

        val viewStates = list.filterIsInstance<RewardItem>().map { it.viewState }
        val sumOfRewards = viewStates.sumOf {
            (it as? RewardViewState)?.rewardAmount?.toDoubleOrNull()?.toBigDecimal()
                ?: BigDecimal.ZERO
        }

        val viewStatesChecked =
            list.filterIsInstance<RewardItem>().filter { (it as? Checkable)?.isChecked == true }
                .map { it.viewState }
        val sumOfCheckedRewards = viewStatesChecked.sumOf {
            (it as? RewardViewState)?.rewardAmount?.toDoubleOrNull()?.toBigDecimal()
                ?: BigDecimal.ZERO
        }

        val viewState = RewardLegendViewState(
            withdraw = getString(
                R.string.rewards_legend_withdraw,
                sumOfCheckedRewards.format(),
                sumOfRewards.format()
            ),
            fee = getString(R.string.rewards_legend_fee, "–")
        )

        val array = viewStatesChecked.map { (it as? RewardViewState)?.validatorAddress }.toTypedArray()
        if (!array.isNullOrEmpty()) viewModel.calculateFee(arrayListOf<String?>().apply { addAll(array) })

        adapter.replaceElement(viewState, adapter.dataset.lastIndex)
        adapter.notifyItemChanged(adapter.dataset.lastIndex)
    }

    private fun mapToItem(viewState: RewardsViewState) = RewardsItem.newInstance(viewState)

    private fun onRewardClick(item: RewardsViewState, view: View, position: Int) {
        if (item is RewardViewState) {
            (adapter.getItem(position) as? RewardItem)?.toggle()
            calculateLegendValues(adapter.dataset as List<RewardsItem>)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
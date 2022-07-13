package net.decentr.module_decentr_staking.presentation.staking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.databinding.FragmentStakingPagerBinding
import net.decentr.module_decentr_staking.presentation.base.BaseFragment
import net.decentr.module_decentr_staking.presentation.base.adapter.ViewPagerAdapter
import net.decentr.module_decentr_staking.presentation.views.Segment
import javax.inject.Inject

class StakingViewPagerFragment: BaseFragment(R.layout.fragment_staking_pager) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var _binding: FragmentStakingPagerBinding? = null
    private val binding get() = _binding!!

    private var pagerAdapter: ViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStakingPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        pagerAdapter = ViewPagerAdapter(this, listOf(
            ValidatorsFragment::class to ValidatorsFragment.getArgs(),
            RewardsFragment::class to RewardsFragment.getArgs()
        ))
        binding.apply {
            val validators = Segment(0, getString(R.string.title_pager_validators), true, true)
            val rewards = Segment(1, getString(R.string.title_pager_rewards), false, true)
            viewSegments.setValues(listOf(validators, rewards))

            pager.adapter = pagerAdapter
            pager.isUserInputEnabled = false
            pager.offscreenPageLimit = pagerAdapter?.itemCount ?: 0
//            tabs.setupWithViewPager(pager)

            viewSegments.listener = { segment, v ->
                pager.currentItem = segment.id
            }
        }
    }
    
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}
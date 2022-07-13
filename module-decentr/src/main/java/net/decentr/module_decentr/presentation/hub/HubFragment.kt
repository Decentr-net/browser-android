package net.decentr.module_decentr.presentation.hub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentHubBinding
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.base.DecentrModuleHostActivity
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.toast
import javax.inject.Inject

class HubFragment: BaseFragment(R.layout.fragment_hub) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<HubViewModel>(factory) }

    private var _binding: FragmentHubBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<HubFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.containerProfile.setOnClickListener {
            view?.findNavController()?.navigate(NavDecentrDirections.actionProfile(args.profile))
        }
        binding.containerStaking.setOnClickListener {
            (activity as? DecentrModuleHostActivity)?.openStakingModule()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package net.decentr.module_decentr.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentProfileBinding
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.base.DecentrModuleHostActivity
import net.decentr.module_decentr.presentation.extensions.copyToClipboard
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.mappers.toViewState
import net.decentr.module_decentr.presentation.viewstates.ProfileViewState
import java.text.DecimalFormat
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<ProfileViewModel>(factory) }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        setProfile(args.profile)
        binding.logo.setOnClickListener {
            (activity as? DecentrModuleHostActivity)?.closeDecentrModule()
        }
        binding.logOut.setOnClickListener {
            viewModel.logout()
            view?.findNavController()?.navigate(ProfileFragmentDirections.actionDecentrProfileFragmentToIntroLoginFragment())
        }
        binding.actionCopyAddress.setOnClickListener {
            context?.copyToClipboard(args.profile.address)
        }
        binding.actionGoToBrowsing.setOnClickListener {
            (activity as? DecentrModuleHostActivity)?.closeDecentrModule()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProfile(profile: ProfileViewState) {
        with(profile) {
            viewModel.getBalances(address)

            binding.name.text = "$firstName $lastName"
            binding.bio.text = bio
            binding.birth.text = birthDate
            binding.gender.text = gender
        }
    }

    private fun initViewModel() {
        viewModel.checkUpdateProfile(args.profile.address)
        viewModel.balanceLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> binding.balance.text = it.exception.message
//                Result.Loading -> toast(getString(R.string.common_loading))
                is Result.Success -> {
                    val decimalFormat = DecimalFormat("0.000000")

                    binding.balance.text = getString(
                        R.string.profile_balance_value_formatter,
                        decimalFormat.format(it.data.first.toBigDecimal()),
                        it.data.second.toBigDecimal().toPlainString()
                    )
                    if (it.data.third > (0).toDouble()) {
                        binding.inProgress.show()
                        binding.inProgress.text = getString(R.string.profile_pdv_in_progress_value_formatter, decimalFormat.format(it.data.third.toBigDecimal()))
                    }
                }
            }
        }
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            it?.let { setProfile(it.toViewState()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
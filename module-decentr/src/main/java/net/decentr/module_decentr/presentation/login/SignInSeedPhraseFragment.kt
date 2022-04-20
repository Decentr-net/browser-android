package net.decentr.module_decentr.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentSignInSeedPhraseBinding
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.extensions.copyToClipboard
import net.decentr.module_decentr.presentation.extensions.gone
import net.decentr.module_decentr.presentation.extensions.onTextChanged
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.toast
import javax.inject.Inject

class SignInSeedPhraseFragment : BaseFragment(R.layout.fragment_sign_in_seed_phrase) {

    companion object {
        const val TYPE_REGISTER = "TYPE_REGISTER"
        const val TYPE_AUTH = "TYPE_AUTH"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<SignInSeedViewModel>(factory) }

    private var _binding: FragmentSignInSeedPhraseBinding? = null
    private val binding get() = _binding!!

    private var showSeed: Boolean = false

    private val args by navArgs<SignInSeedPhraseFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInSeedPhraseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        when (args.screenType) {
            TYPE_AUTH -> {
                binding.info.text = getString(R.string.sign_in_seed_auth_info)
                binding.inputSeed.setText(args.seedString)
                binding.actionScanQrCode.show()
                binding.registrationInfo.gone()
                binding.actionCopyAddress.gone()
                binding.actionImportUser.text = getString(R.string.sign_in_seed_import_button)
                binding.actionImportUser.isEnabled = true
            }
            TYPE_REGISTER -> {
                viewModel.generateMnemonic()
                binding.info.text = getString(R.string.sign_in_seed_register_info)
                binding.actionScanQrCode.gone()
                binding.registrationInfo.show()
                binding.actionCopyAddress.show()
                binding.actionImportUser.text = getString(R.string.create_by_seed_button)
                binding.actionImportUser.isEnabled = false
            }
        }
        binding.inputSeed.onTextChanged { sequence ->
            binding.actionImportUser.isEnabled = sequence?.length ?: 0 > 0
        }
        binding.actionScanQrCode.setOnClickListener {
            view?.findNavController()
                ?.navigate(SignInSeedPhraseFragmentDirections.actionSignInSeedPhraseToQrScanFragment())
        }
        binding.actionImportUser.setOnClickListener {
            when (args.screenType) {
                TYPE_AUTH -> {
                    try {
                        binding.inputSeed.text.toString()
                            .let { viewModel.getWalletFromSeed(it.trim()) }
                    } catch (e: Exception) {
                        toast(getString(R.string.error_invalid_seed))
                    }
                }
                TYPE_REGISTER -> {
                    try {
                        binding.inputSeed.text.toString()
                            .let { viewModel.createWalletFromSeed(it.trim()) }
                    } catch (e: Exception) {
                        toast(getString(R.string.error_invalid_seed))
                    }
                }
            }
        }
        binding.actionCopyAddress.setOnClickListener {
            context?.copyToClipboard(binding.inputSeed.text.toString())
        }

//        binding.actionShowSeed.setOnClickListener {
//            showSeed = !showSeed
//            if (showSeed) {
////                binding.inputSeed.transformationMethod = PasswordTransformationMethod.getInstance()
//                binding.inputSeed.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
//                binding.actionShowSeed.setImageResource(R.drawable.ic_eye_enabled)
//            } else {
//                binding.inputSeed.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_TEXT_FLAG_MULTI_LINE
//                binding.actionShowSeed.setImageResource(R.drawable.ic_eye_disabled)
//                binding.inputSeed.setSelection(binding.inputSeed.text?.length ?: 0)
//            }
//        }
    }

    private fun initViewModel() {
        viewModel.mnemonicLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> binding.description.text = it.exception.message
                is Result.Success -> {
                    binding.inputSeed.setText(it.data)
                }
            }
        }
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    toast(it.exception.message ?: getString(R.string.error_wallet_default))
                }
                Result.Loading -> toast(getString(R.string.common_loading))
                is Result.Success -> {
                    view?.findNavController()
                        ?.navigate(NavDecentrDirections.actionProfile(profile = it.data))
                }
            }
        }
        viewModel.keysWithAddressLiveData.observe(viewLifecycleOwner) {
            if (args.screenType == TYPE_REGISTER) {
                when (it) {
                    is Result.Error -> toast(it.exception.message)
                    Result.Loading -> toast(getString(R.string.common_processing))
                    is Result.Success -> {
                        view?.findNavController()?.navigate(
                            SignInSeedPhraseFragmentDirections.actionSignInSeedPhraseToSignInEmail(
                                it.data.first
                            )
                        )
                    }
                }
            }
        }
        viewModel.setProfileLiveData.observe(viewLifecycleOwner) {
            it?.let {
                view?.findNavController()?.navigate(
                    SignInSeedPhraseFragmentDirections.actionSignInSeedPhraseToDecentrSetProfileFragment(email = null, address = it)
                )
            }
        }
    }
}
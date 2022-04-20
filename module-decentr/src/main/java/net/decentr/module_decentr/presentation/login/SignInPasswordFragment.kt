package net.decentr.module_decentr.presentation.login

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.data.utils.AESHelper
import net.decentr.module_decentr.databinding.FragmentSignInPasswordBinding
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.extensions.gone
import net.decentr.module_decentr.presentation.extensions.onTextChanged
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.toast
import javax.inject.Inject

class SignInPasswordFragment: BaseFragment(R.layout.fragment_sign_in_password) {

    companion object {
        const val TYPE_REGISTER = "TYPE_REGISTER"
        const val TYPE_AUTH = "TYPE_AUTH"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<SignInPasswordViewModel>(factory) }

    private var _binding: FragmentSignInPasswordBinding? = null
    private val binding get() = _binding!!

    private var showPass: Boolean = false

    private val args by navArgs<SignInPasswordFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        when (args.screenType) {
            TYPE_REGISTER -> {
                binding.info.text = getString(R.string.sign_in_pass_info)
                binding.actionSavePass.text = getString(R.string.sign_in_pass_save_button)
                binding.containerRepeatPass.show()
            }
            TYPE_AUTH -> {
                binding.info.text = getString(R.string.sign_up_pass_info)
                binding.actionSavePass.text = getString(R.string.sign_up_pass_confirm_button)
                binding.containerRepeatPass.gone()
            }
        }

        binding.inputPass.onTextChanged { seq ->
            binding.actionSavePass.isEnabled = seq?.length ?: 0 > 0
        }
        binding.actionSavePass.setOnClickListener {
            when (args.screenType) {
                TYPE_REGISTER -> {
                    if (validatePass(binding.inputPass.text.toString())) {
                        viewModel.getWalletFromSeed(args.mnemonicString)
                    }
                }
                TYPE_AUTH -> {
                    if (validatePass(binding.inputPass.text.toString()))
                        args.cipherString?.let { cipher -> decryptByPassCode(binding.inputPass.text.toString().trim(), cipher) }
                }
            }
        }
        binding.actionScanQrCode.setOnClickListener {
            view?.findNavController()?.navigate(NavDecentrDirections.actionScanQr())
        }
        binding.actionShowPass.setOnClickListener {
            showPass = !showPass
            if (showPass) {
                binding.inputPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.actionShowPass.setImageResource(R.drawable.ic_eye_enabled)
            } else {
                binding.inputPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.inputPass.setSelection(binding.inputPass.text?.length ?: 0)
                binding.actionShowPass.setImageResource(R.drawable.ic_eye_disabled)
            }
        }
    }

    private fun initViewModel() {
        viewModel.keysWithAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> toast(it.exception.message)
                Result.Loading -> toast(getString(R.string.common_processing))
                is Result.Success -> {
//                    view?.findNavController()?.navigate(NavDecentrDirections.actionConnectEmail(it.data.first))
                }
            }
        }
    }

    private fun decryptByPassCode(pass: String, cipherText: String) {
        try {
            val decrypted = AESHelper.decryptCryptoJS(pass, cipherText)
            view?.findNavController()?.navigate(SignInPasswordFragmentDirections.actionSignInPassToSignInSeedPhrase(seedString = decrypted, screenType = TYPE_AUTH))
        } catch (e: Exception) {
            toast(getString(R.string.error_wrong_password))
        }
    }

    private fun validatePass(pass: String?): Boolean {
        when {
            pass.isNullOrEmpty() -> {
                toast(getString(R.string.error_empty_password))
                return false
            }
            pass.matches(Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$")) -> {
                return if (!binding.containerRepeatPass.isVisible || pass == binding.inputRepeatPass.text.toString()) true
                else {
                    toast(getString(R.string.error_not_match_password))
                    false
                }
            }
            else -> {
                toast(getString(R.string.error_validation_pass))
                return false
            }
        }
    }
}
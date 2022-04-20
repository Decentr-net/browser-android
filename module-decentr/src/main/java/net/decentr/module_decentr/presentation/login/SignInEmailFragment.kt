package net.decentr.module_decentr.presentation.login

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.delay
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentSignInEmailBinding
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.extensions.onTextChanged
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.login.views.otpview.OTPListener
import net.decentr.module_decentr.presentation.toast
import javax.inject.Inject

class SignInEmailFragment : BaseFragment(R.layout.fragment_sign_in_email) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<SignInEmailViewModel>(factory) }

    private var _binding: FragmentSignInEmailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<SignInEmailFragmentArgs>()
    private var modeConnectEmail = true
    private val OTP_CODE_SIZE = 6

    private var countDownTimer: CountDownTimer? = null
    private var timeToResend = String()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.inputEmail.onTextChanged {
            if (modeConnectEmail) binding.actionConfirmEmail.isEnabled = isEmailValid(it.toString())
        }
        binding.inputOtpCode.otpListener = object: OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                binding.actionConfirmEmail.isEnabled = (binding.inputOtpCode.otp?.length == OTP_CODE_SIZE) && !modeConnectEmail
            }

        }
        binding.actionConfirmEmail.setOnClickListener {
            if (modeConnectEmail) {
                viewModel.sendConfirmEmail(binding.inputEmail.text.toString(), args.address)
                binding.actionConfirmEmail.isEnabled = false
                setResendClickListener()
                setTimer()
            } else {
                viewModel.sendConfirmCode(binding.inputEmail.text.toString(), binding.inputOtpCode.otp ?: String())
                binding.actionConfirmEmail.isEnabled = false
            }
        }
    }

    private fun setResendClickListener() {
        binding.actionResend.setOnClickListener {
            if (timeToResend.isNotEmpty()) toast(timeToResend)
            else {
                modeConnectEmail = true
                viewModel.sendConfirmEmail(binding.inputEmail.text.toString(), args.address)
                setTimer()
            }
        }
    }

    private fun setTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeToResend = getString(R.string.sign_in_email_resend_timer, (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                timeToResend = String()
            }
        }.start()
    }

    private fun initViewModel() {
        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> toast(it.exception.message)
                Result.Loading -> toast(getString(R.string.common_processing))
                is Result.Success -> {
                    if (modeConnectEmail) {
                        toast(getString(R.string.sign_in_email_confirmation_sent))
                        modeConnectEmail = false
                        showCodeInput()
                    } else {
                        view?.findNavController()?.navigate(SignInEmailFragmentDirections.actionSignInEmailToDecentrSetProfileFragment(binding.inputEmail.text.toString(), args.address))
                    }
                }
            }
        }
    }

    private fun showCodeInput() {
        binding.inputEmail.isEnabled = false
        binding.containerEmail.animate()
            .alpha(1f)
            .setDuration(1000)
            .withStartAction {
                binding.containerEmail.show()
                binding.inputOtpCode.requestFocusOTP()
            }
            .start()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }
}
package net.decentr.module_decentr.presentation.login

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.postDelayed
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentIntroLoginBinding
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.base.DecentrModuleHostActivity
import net.decentr.module_decentr.presentation.extensions.gone
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.login.SignInSeedPhraseFragment.Companion.TYPE_REGISTER
import javax.inject.Inject

class IntroLoginFragment: BaseFragment(R.layout.fragment_intro_login) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<IntroLoginViewModel>(factory) }

    private var _binding: FragmentIntroLoginBinding? = null
    private val binding get() = _binding!!

    private val permissionResult: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            view?.findNavController()?.navigate(IntroLoginFragmentDirections.actionIntroLoginFragmentToQrScanFragment())
        } else {
            (activity as DecentrModuleHostActivity).permissionNeeded()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun hideSoftInput() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun initView() {
        hideSoftInput()
        viewModel.checkIsUserContains()
        binding.login.gone()
        binding.registration.gone()
        binding.skip.gone()
    }

    private fun initViewModel() {
        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                view?.postDelayed(500) {
                    view?.findNavController()?.navigate(NavDecentrDirections.actionProfile(it))
                }
            } else {
                binding.login.show()
                binding.registration.show()
                binding.skip.show()

                binding.login.setOnClickListener {
                    goToQrFragment()
                }
                binding.registration.setOnClickListener {
                    view?.findNavController()?.navigate(IntroLoginFragmentDirections.actionIntroLoginFragmentToSignInSeedPhrase(screenType = TYPE_REGISTER))
                }
                binding.skip.setOnClickListener {
                    (activity as? DecentrModuleHostActivity)?.closeDecentrModule()
                }
            }
        }
    }

    private fun goToQrFragment() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PermissionChecker.PERMISSION_GRANTED) {
            permissionResult.launch(Manifest.permission.CAMERA)
        } else {
            view?.findNavController()?.navigate(IntroLoginFragmentDirections.actionIntroLoginFragmentToQrScanFragment())
        }
    }

}
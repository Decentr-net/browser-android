package net.decentr.module_decentr.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentSignInCredentialsBinding
import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.models.PDVProfile
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.extensions.gone
import net.decentr.module_decentr.presentation.extensions.onTextChanged
import net.decentr.module_decentr.presentation.extensions.show
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.profile.views.DateSelectorDialog
import net.decentr.module_decentr.presentation.profile.views.DropdownStringView
import net.decentr.module_decentr.presentation.toast
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SetProfileFragment : BaseFragment(R.layout.fragment_sign_in_credentials) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { injectViewModel<SetProfileViewModel>(factory) }

    private var _binding: FragmentSignInCredentialsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<SetProfileFragmentArgs>()

    private var validationFirstName = false
    private var validationLastName = true
    private var validationEmail = false
    private var validationBIO = true
    private var localBirthDateTime: Date? = null
    private var localGender: String? = null
    private val MIN_LENGTH_NAME = 2
    private val MAX_LENGTH_NAME = 20
    private val MAX_LENGTH_BIO = 70
    private val MIN_DATE = Triple(1901, 1, 1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {
        if (args.email != null) {
            binding.containerEmail.gone()
            validationEmail = true
        } else {
            binding.containerEmail.show()
            binding.inputEmail.onTextChanged {
                validationEmail = isEmailValid(it.toString())
                validateFields()
            }
        }
        binding.actionSetProfile.setOnClickListener {
            if (validateFields(true)) {
                val email = args.email ?: binding.inputEmail.text.toString()
                val profile = PDVProfile(
                    address = args.address,
                    type = PDV.PDVType.TYPE_PROFILE,
                    avatar = null,
                    bio = binding.inputBio.text.toString(),
                    birthday = localBirthDateTime?.let {
                        SimpleDateFormat(viewModel.timespampPattern).format(
                            it
                        )
                    },
                    emails = listOf(email),
                    firstName = binding.inputFirstName.text.toString(),
                    lastName = binding.inputLastName.text.toString(),
                    gender = localGender
                )
                viewModel.setProfile(profile, args.address)
            }
        }
        binding.inputFirstName.onTextChanged {
            validationFirstName = it?.length ?: 0 in MIN_LENGTH_NAME..MAX_LENGTH_NAME
            validateFields()
        }
        binding.inputLastName.onTextChanged {
            validationLastName = it?.length ?: 0 <= MAX_LENGTH_NAME
            validateFields()
        }
        binding.inputBio.onTextChanged {
            validationBIO = it?.length ?: 0 <= MAX_LENGTH_BIO
            validateFields()
        }
        binding.containerBirthday.setOnClickListener {
            chooseDate()
        }
        val spinnerGender = DropdownStringView(requireContext()).apply {
            setListValues(
                resources.getStringArray(R.array.gender).toList(),
                getString(R.string.sign_in_credentials_choose_gender_hint)
            )
            selectedItemListener = { value, v ->
                localGender = value.lowercase()
            }
        }
        binding.containetSpinner.addView(spinnerGender)
    }

    private fun initViewModel() {
        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> toast(it.exception.message)
                Result.Loading -> toast(getString(R.string.common_loading))
                is Result.Success -> {
                    view?.findNavController()?.navigate(NavDecentrDirections.actionProfile(it.data))
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun chooseDate() {
        val dialog = DateSelectorDialog().apply {
            setListener { year, month, day ->
                if (year == 0) return@setListener
                val time = Calendar.getInstance().apply {
                    set(year, month, day)
                }.time
                localBirthDateTime = time
                localBirthDateTime?.let {
                    binding.birthday.text = SimpleDateFormat(viewModel.timespampPattern).format(it)
                }
            }

            val currentDate = Date().time
            val minDate = Calendar.getInstance().apply {
                set(MIN_DATE.first, MIN_DATE.second - 1, MIN_DATE.third)
            }
            val maxDate = Calendar.getInstance().apply {
                set(
                    this.get(Calendar.YEAR),
                    this.get(Calendar.MONTH),
                    this.get(Calendar.DAY_OF_MONTH)
                )
            }
            arguments =
                DateSelectorDialog.getArgs(minDate.timeInMillis, maxDate.timeInMillis, currentDate)
        }
        dialog.show(parentFragmentManager, null)
    }

    private fun validateFields(withToast: Boolean = false): Boolean {
        val valid = when {
            !validationFirstName -> {
                if (withToast) toast(getString(R.string.error_cred_validation_first_name, MIN_LENGTH_NAME, MAX_LENGTH_NAME))
                false
            }
            !validationLastName -> {
                if (withToast) toast(getString(R.string.error_cred_validation_last_name, MAX_LENGTH_NAME))
                false
            }
            !validationEmail -> {
                if (withToast) toast(getString(R.string.error_cred_validation_email))
                false
            }
            !validationBIO -> {
                if (withToast) toast(getString(R.string.error_cred_validation_bio, MAX_LENGTH_BIO))
                false
            }
            else -> {
                true
            }
        }
        return valid
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
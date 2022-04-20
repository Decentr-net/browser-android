package net.decentr.module_decentr.presentation.profile.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.decentr.module_decentr.databinding.DialogDateSelectorBinding
import java.util.*

class DateSelectorDialog : BottomSheetDialogFragment() {

    companion object {
        private const val KEY_MIN_DATE = "MonthSelectorDialog.KEY_MIN_DATE"
        private const val KEY_MAX_DATE = "MonthSelectorDialog.KEY_MAX_DATE"
        private const val KEY_CURRENT_DATE = "MonthSelectorDialog.KEY_CURRENT_DATE"


        fun getArgs(minDate: Long?, maxDate: Long?, currentDate: Long?) = bundleOf(
            KEY_MIN_DATE to minDate,
            KEY_MAX_DATE to maxDate,
            KEY_CURRENT_DATE to currentDate
        )
    }

    private var _binding: DialogDateSelectorBinding? = null
    private val binding get() = _binding!!


    private lateinit var listener: (Int, Int, Int) -> Unit

    fun setListener(listener: (Int, Int, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDateSelectorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong(KEY_MIN_DATE)?.let {
            binding.datePicker.minDate = it
        }

        arguments?.getLong(KEY_MAX_DATE)?.let {
            binding.datePicker.maxDate = it
        }

        arguments?.getLong(KEY_CURRENT_DATE)?.let {
            val c = Calendar.getInstance()
            c.timeInMillis = it
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            binding.datePicker.updateDate(year, month + 1, day)
        }

        binding.saveButton.setOnClickListener {
            listener.invoke(binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth)
            dismiss()
        }
    }
}
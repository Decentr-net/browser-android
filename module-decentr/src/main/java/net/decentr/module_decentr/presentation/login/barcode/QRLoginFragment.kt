package net.decentr.module_decentr.presentation.login.barcode

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.FragmentQrCodeBinding
import net.decentr.module_decentr.presentation.base.BaseFragment
import net.decentr.module_decentr.presentation.injectViewModel
import net.decentr.module_decentr.presentation.login.SignInPasswordFragment
import net.decentr.module_decentr.presentation.login.SignInSeedPhraseFragment.Companion.TYPE_AUTH
import net.decentr.module_decentr.presentation.login.barcode.QRLoginViewModel.WorkflowState
import net.decentr.module_decentr.presentation.login.barcode.barcodedetection.BarcodeField
import net.decentr.module_decentr.presentation.login.barcode.barcodedetection.BarcodeProcessor
import net.decentr.module_decentr.presentation.login.barcode.views.camera.CameraSource
import net.decentr.module_decentr.presentation.login.barcode.views.camera.CameraSourcePreview
import net.decentr.module_decentr.presentation.login.barcode.views.camera.GraphicOverlay
import java.io.IOException
import java.util.*
import javax.inject.Inject

class QRLoginFragment : BaseFragment(R.layout.fragment_qr_code) {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var settingsButton: View? = null
    private var flashButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null

    private var currentWorkflowState: WorkflowState? = null

    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val workflowModel by lazy { injectViewModel<QRLoginViewModel>(factory) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @Suppress("DEPRECATION")
    private fun initView() {
        preview = binding.cameraPreview

        graphicOverlay = binding.cameraPreviewGraphicOverlay.apply {
            cameraSource = CameraSource(this)
        }

        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                context,
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        binding.closeButton.setOnClickListener {
            activity?.onBackPressed()
        }
        flashButton = binding.flashButton.apply {
            this.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                } else {
                    it.isSelected = true
                    cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                }
            }
        }
        binding.actionEnterSeed.setOnClickListener {
            view?.findNavController()?.navigate(QRLoginFragmentDirections.actionQrScanFragmentToSignInSeedPhrase(screenType = TYPE_AUTH))
        }

        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()
        workflowModel.markCameraFrozen()
        settingsButton?.isEnabled = true
        currentWorkflowState = WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(BarcodeProcessor(graphicOverlay!!, workflowModel))
        workflowModel.setWorkflowState(WorkflowState.DETECTING)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            flashButton?.isSelected = false
            preview?.stop()
        }
    }

    private fun setUpWorkflowModel() {
        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.workflowState.observe(viewLifecycleOwner, Observer { workflowState ->
            if (workflowState == null || Objects.equals(currentWorkflowState, workflowState)) {
                return@Observer
            }

            currentWorkflowState = workflowState

            val wasPromptChipGone = promptChip?.visibility == View.GONE

            when (workflowState) {
                WorkflowState.DETECTING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }
                WorkflowState.CONFIRMING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_move_camera_closer)
                    startCameraPreview()
                }
                WorkflowState.SEARCHING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_searching)
                    stopCameraPreview()
                }
                WorkflowState.DETECTED, WorkflowState.SEARCHED -> {
                    promptChip?.visibility = View.GONE
                    stopCameraPreview()
                }
                else -> promptChip?.visibility = View.GONE
            }

            val shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && promptChip?.visibility == View.VISIBLE
            promptChipAnimator?.let {
                if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
            }
        })

        workflowModel.detectedBarcode.observe(viewLifecycleOwner) { barcode ->
            if (barcode != null) {
                val barcodeFieldList = ArrayList<BarcodeField>()
                barcodeFieldList.add(BarcodeField("Raw Value", barcode.rawValue ?: ""))
                val encrypted = barcode.rawValue?.substringAfter("decentr://login?encryptedSeed=") ?: String()
                view?.findNavController()?.navigate(QRLoginFragmentDirections.actionQrScanFragmentToSignInPass(encrypted, SignInPasswordFragment.TYPE_AUTH, String()))
            }
        }
    }

    companion object {
        private const val TAG = "QRLoginFragment"
    }
}
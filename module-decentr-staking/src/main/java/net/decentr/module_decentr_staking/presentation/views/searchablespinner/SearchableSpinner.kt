package net.decentr.module_decentr_staking.presentation.views.searchablespinner

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.decentr.module_decentr_staking.R
import net.decentr.module_decentr_staking.presentation.extensions.hideKeyboard
import net.decentr.module_decentr_staking.presentation.extensions.showKeyboard


@Suppress("RedundantSetter", "RedundantGetter")
class SearchableSpinner(private val context: Context) {
    lateinit var onItemSelectListener: OnItemSelectListener
    private lateinit var dialog: AlertDialog

    private lateinit var dialogView: View
    private lateinit var recyclerAdapter: SpinnerRecyclerAdapter

    private var title: TextView? = null
    private var search: SearchView? = null
    private var close: Button? = null
    private var recycler: RecyclerView? = null
    private var error: TextView? = null
    private var headerContainer: RelativeLayout? = null
    private var headerLayout: LinearLayout? = null

    private var showKeyboardByDefault: Boolean = true
    var spinnerCancelable: Boolean = false
    var windowTitle: String? = null
    var searchQueryHint: String = context.getString(android.R.string.search_go)
    var negativeButtonText: String = context.getString(android.R.string.cancel)
    var dismissSpinnerOnItemClick: Boolean = true
    var highlightSelectedItem: Boolean = true
    var negativeButtonVisibility: SpinnerView = SpinnerView.VISIBLE
    var windowTitleVisibility: SpinnerView = SpinnerView.GONE
    var searchViewVisibility: SpinnerView = SpinnerView.VISIBLE
    var selectedItemPosition: Int = -1
    var selectedItem: SpinnerViewState? = null

    @Suppress("unused")
    enum class SpinnerView(val visibility: Int) {
        VISIBLE(View.VISIBLE),
        INVISIBLE(View.INVISIBLE),
        GONE(View.GONE)
    }

    fun show() {
        if (getDialogInfo(true)) {
            dialogView = View.inflate(context, R.layout.view_searchable_spinner, null)
            val dialogBuilder =
                AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(spinnerCancelable || negativeButtonVisibility != SpinnerView.VISIBLE)

            dialog = dialogBuilder.create()
            dialog.show()
            dialog.initView()
            dialog.initAlertDialogWindow()
        }
    }

    fun dismiss() {
        if (getDialogInfo(false)) {
            toggleKeyBoard(false)
            if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(null)
            dialog.dismiss()
        }
    }

    fun setSpinnerListItems(spinnerList: List<SpinnerViewState>) {
        recyclerAdapter =
            SpinnerRecyclerAdapter(context, spinnerList, object : OnItemSelectListener {
                override fun setOnItemSelectListener(position: Int, selectedItem: SpinnerViewState) {
                    selectedItemPosition = position
                    this@SearchableSpinner.selectedItem = selectedItem
                    if (::onItemSelectListener.isInitialized) onItemSelectListener.setOnItemSelectListener(
                        position,
                        selectedItem
                    )
                    dismiss()
                }
            })
    }

//    //    Private helper methods
//    override fun onDestroy(owner: LifecycleOwner) {
//        if (getDialogInfo(false)) dialog.dismiss()
//        super.onDestroy(owner)
//    }

//    private fun initLifeCycleObserver() {
//        if (context is AppCompatActivity)
//            context.lifecycle.addObserver(this)
//        if (context is FragmentActivity)
//            context.lifecycle.addObserver(this)
//        if (context is Fragment)
//            context.lifecycle.addObserver(this)
//    }

    private fun getDialogInfo(toShow: Boolean): Boolean {
        return if (toShow) {
            !::dialog.isInitialized || !dialog.isShowing
        } else
            ::dialog.isInitialized && dialog.isShowing && dialog.window != null && dialog.window?.decorView != null && dialog.window?.decorView?.isAttachedToWindow!!
    }

    private fun AlertDialog.initView() {
        title = this.findViewById(R.id.title)
        search = this.findViewById(R.id.search)
        headerLayout = this.findViewById(R.id.header_layout)
        headerContainer = this.findViewById(R.id.header_container)
        error = this.findViewById(R.id.error)
        recycler = this.findViewById(R.id.recycler)
        close = this.findViewById(R.id.close)

        setOnShowListener {
            toggleKeyBoard(true)
        }

        if (spinnerCancelable || negativeButtonVisibility != SpinnerView.VISIBLE)
            setOnCancelListener {
                if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(
                    null
                )
            }

        dialog.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                this@SearchableSpinner.dismiss()
            }
            false
        }

        //init WindowTittle
        if (windowTitle != null || windowTitleVisibility.visibility == SearchView.VISIBLE) {
            title?.visibility = View.VISIBLE
            title?.text = windowTitle
        } else title?.visibility = windowTitleVisibility.visibility

        //init SearchView
        if (searchViewVisibility.visibility == SearchView.VISIBLE) {
            search?.queryHint = searchQueryHint
            search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(newText)
                    return false
                }

            })
        } else search?.visibility = searchViewVisibility.visibility


        //init NegativeButton
        if (negativeButtonVisibility.visibility == SearchView.VISIBLE) {
            close?.setOnClickListener {
                it.isClickable = false
                this@SearchableSpinner.dismiss()
            }
            close?.text = negativeButtonText
        } else close?.visibility = negativeButtonVisibility.visibility

        //set Recycler Adapter
        if (::recyclerAdapter.isInitialized) {
            recyclerAdapter.highlightSelectedItem = highlightSelectedItem
            recycler?.adapter = recyclerAdapter
        }
    }

    private fun AlertDialog.initAlertDialogWindow() {
        window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
    }

    private fun toggleKeyBoard(showKeyBoard: Boolean) {
        if (showKeyboardByDefault && showKeyBoard) {
            search?.post {
                search?.requestFocus()
                search?.showKeyboard()
            }
        } else {
            search.hideKeyboard()
        }
    }
}
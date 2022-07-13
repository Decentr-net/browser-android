package net.decentr.module_decentr_staking.presentation.base.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.reflect.KClass

const val KEY_TITLE = "TITLE"
open class ViewPagerAdapter(
    private val parentFragment: Fragment,
    private val fragments: List<Pair<KClass<out Fragment>, Bundle?>>
) : FragmentStateAdapter(parentFragment) {

    constructor(
        parentFragment: Fragment,
        vararg fragments: Pair<KClass<out Fragment>, Bundle?>
    ) : this(parentFragment, listOf(*fragments))

    fun getPageTitle(position: Int): CharSequence? {
        val titleId = fragments[position].second?.getInt(KEY_TITLE) ?: 0
        return if (titleId > 0) parentFragment.context?.getString(titleId) else null
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        parentFragment.context?.let {
            return parentFragment.childFragmentManager.fragmentFactory.instantiate(it.classLoader, fragments[position].first.java.name).apply {
                arguments = fragments[position].second
            }
        }
        throw IllegalStateException("Context is null")
    }
}
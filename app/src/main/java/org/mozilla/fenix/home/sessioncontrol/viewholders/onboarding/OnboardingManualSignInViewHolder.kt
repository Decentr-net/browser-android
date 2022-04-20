/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.home.sessioncontrol.viewholders.onboarding

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.mozilla.fenix.R
import org.mozilla.fenix.components.metrics.Event
import org.mozilla.fenix.databinding.OnboardingManualSigninBinding
import org.mozilla.fenix.ext.components
import org.mozilla.fenix.home.HomeFragmentDirections

class OnboardingManualSignInViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var binding: OnboardingManualSigninBinding = OnboardingManualSigninBinding.bind(view)

    init {
        binding.fxaSignInButton.setOnClickListener {
            it.context.components.analytics.metrics.track(Event.OnboardingManualSignIn)

                //todo recheck direction from ui logic
//            val directions = HomeFragmentDirections.actionGlobalTurnOnSync()
            val directions = HomeFragmentDirections.actionNavigateToDecentrLogin()
            Navigation.findNavController(view).navigate(directions)
        }
    }

    fun bind() {
        val context = itemView.context
        binding.headerText.text = context.getString(R.string.onboarding_decentr_account_sync_title)
    }

    companion object {
        const val LAYOUT_ID = R.layout.onboarding_manual_signin
    }
}

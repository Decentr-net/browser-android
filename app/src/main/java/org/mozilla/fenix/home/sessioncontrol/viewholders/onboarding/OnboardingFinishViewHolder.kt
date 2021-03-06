/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.home.sessioncontrol.viewholders.onboarding

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import mozilla.components.browser.state.state.searchEngines
import org.mozilla.fenix.R
import org.mozilla.fenix.components.metrics.Event
import org.mozilla.fenix.databinding.OnboardingFinishBinding
import org.mozilla.fenix.ext.components
import org.mozilla.fenix.home.sessioncontrol.OnboardingInteractor

class OnboardingFinishViewHolder(
    view: View,
    private val interactor: OnboardingInteractor,
) : RecyclerView.ViewHolder(view) {

    init {
        val binding = OnboardingFinishBinding.bind(view)
        binding.finishButton.setOnClickListener {
            interactor.onStartBrowsingClicked()

            //add decentr default search engint DDG
            val searchEngineId = "ddg"
            val engine = requireNotNull(
                it.context.components.core.store.state.search.searchEngines.find { searchEngine ->
                    searchEngine.id == searchEngineId
                }
            )
            it.context.components.useCases.searchUseCases.selectSearchEngine(engine)
            it.context.components.analytics.metrics.track(Event.OnboardingFinish)
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.onboarding_finish
    }
}

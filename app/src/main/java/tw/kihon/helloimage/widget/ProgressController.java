/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tw.kihon.helloimage.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;

import tw.kihon.helloimage.util.Utils;


/**
 * Controller that displays empty view and handles animation appropriately.
 */
public final class ProgressController {

    private static final int ANIMATION_DURATION = 300;
    private static final boolean USE_TRANSITION_FRAMEWORK = Utils.isLOrLater();

    private final Transition mMainViewTransition;
    private final ViewGroup mMainLayout;
    private final ViewGroup mProgressView;
    private boolean mIsLoading = true;

    /**
     * Constructor of the controller.
     *
     * @param mainLayout  The view that should be displayed when empty view is hidden.
     * @param progressBar The view that should be displayed when main view is empty.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ProgressController(View mainLayout, View progressBar) {
        mMainLayout = (ViewGroup) mainLayout;
        mProgressView = (ViewGroup) progressBar;
        if (USE_TRANSITION_FRAMEWORK) {
            mMainViewTransition = new TransitionSet()
                    .setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
                    .addTarget(mMainLayout)
                    .addTransition(new Fade(Fade.OUT))
                    .addTransition(new Fade(Fade.IN))
                    .setDuration(ANIMATION_DURATION);
        } else {
            mMainViewTransition = null;
        }
    }

    public Transition getMainViewTransition() {
        return mMainViewTransition;
    }

    /**
     * Sets the state for the controller. If it's empty, it will display the empty view.
     *
     * @param isLoading Whether or not the controller should transition into empty state.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setLoading(boolean isLoading) {
        if (mIsLoading == isLoading) {
            return;
        }
        mIsLoading = isLoading;
        // State changed, perform transition.
        if (USE_TRANSITION_FRAMEWORK) {
            TransitionManager.beginDelayedTransition(mProgressView, mMainViewTransition);
        }
        mProgressView.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
        mMainLayout.setVisibility(mIsLoading ? View.GONE : View.VISIBLE);
    }
}

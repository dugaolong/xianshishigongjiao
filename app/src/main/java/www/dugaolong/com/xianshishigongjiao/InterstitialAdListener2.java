package www.dugaolong.com.xianshishigongjiao;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;

import java.util.Map;

/**
 * Listener for receiving notifications during the lifecycle of an interstitial.
 */
public interface InterstitialAdListener2 {
    /**
     * Callback to signal that a request to fetch an ad (by calling {@link #load()}
     * failed. The status code indicating the reason for failure is available as a
     * parameter.
     * You should call {@link #load()} again to request a fresh ad.
     *
     * @param ad
     * @param status
     */
    void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus status);

    /**
     * Called to indicate that an ad is available in response to a request for an ad (by
     * calling {@link #load()}.
     * <p class="note"><strong>Note</strong> This does not indicate that the ad can be shown
     * yet. Your code should show an ad <strong>after</strong> the
     * {@link #onAdLoadSucceeded(InMobiInterstitial)} method is called. Alternately, if you do not
     * want to handle this event, you must test if the ad is ready to be shown by checking
     * the result of calling the {@link #isReady()} method.</p>
     *
     * @param ad
     */
    void onAdReceived(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that an ad was loaded and it can now be shown.
     * This will always be called <strong>after</strong> the {@link #onAdReceived(InMobiInterstitial)}
     * callback.
     *
     * @param ad
     */
    void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that rewards have been unlocked.
     *
     * @param ad
     * @param rewards
     */
    void onAdRewardActionCompleted(InMobiInterstitial inMobiInterstitial, Map<Object, Object> rewards);

    /**
     * Called to indicate that a request to show an ad (by calling {@link #show()} failed.
     * You should call {@link #load()} to request for a fresh ad.
     *
     * @param ad
     */
    void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that the ad will be launching a fullscreen overlay.
     *
     * @param ad
     */
    void onAdWillDisplay(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that the fullscreen overlay is now the topmost screen.
     *
     * @param ad
     */
    void onAdDisplayed(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that an ad interaction was observed.
     *
     * @param ad
     * @param params
     */
    void onAdInteraction(InMobiInterstitial inMobiInterstitial, Map<Object, Object> params);

    /**
     * Called to indicate that the fullscreen overlay opened by the ad was closed.
     *
     * @param ad
     */
    void onAdDismissed(InMobiInterstitial inMobiInterstitial);

    /**
     * Called to indicate that the user may leave the application on account
     * of interacting with the ad.
     *
     * @param ad
     */
    void onUserLeftApplication(InMobiInterstitial inMobiInterstitial);
}

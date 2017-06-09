package com.mattaniahbeezy.handysiddur.watchfaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.mattaniahbeezy.handysiddur.R;
import com.mattaniahbeezy.handysiddur.utilities.DateFormatter;
import com.mattaniahbeezy.handysiddur.utilities.LocationPermissionUtility;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.HebrewDate;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.Zman;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.ZmanimCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class AnalogWatchface extends CanvasWatchFaceService {
    private static final String TAG = "AnalogWatchFaceService";

    /**
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        static final int MSG_UPDATE_TIME = 0;

        long nightTime;
        long lateAfternoonTime;
        long afternoonTime;
        long daytime;
        long morningTime;
        long earlyMorningTime;

        int earlyMorningColor;
        int morningColor;
        int daytimeColor;
        int afternoonColor;
        int laterAfternoonColor;
        int nighttimeColor;

        Paint mBackgroundColor;
        Paint mSplotchPaint;
        Paint mSplotchOutline;
        Paint mShadowPaint;
        Paint mHourPaint;
        Paint mMinutePaint;
        Paint mSecondPaint;
        Paint mTickPaint;
        Paint mZmanPaint;
        Paint mUpcomingTextPaint;
        boolean mMute;
        Calendar mTime = Calendar.getInstance();

        /**
         * Handler to update the time once a second in interactive mode.
         */
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.setTimeZone(TimeZone.getDefault());
                mTime.setTimeInMillis(System.currentTimeMillis());
            }
        };
        boolean mRegisteredTimeZoneReceiver = false;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;
        boolean mBurnInProtect;

        @Override
        public void onCreate(SurfaceHolder holder) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onCreate");
            }
            super.onCreate(holder);

            if(LocationPermissionUtility.hasSavedLocation(AnalogWatchface.this)) {
                ZmanimCalculator.getInstance().setLocation(LocationPermissionUtility.getSavedLocation(AnalogWatchface.this));
            }

            setWatchFaceStyle(new WatchFaceStyle.Builder(AnalogWatchface.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            setHighlightTimes();

            mBackgroundColor = new Paint();
            mBackgroundColor.setAlpha(255);
            mBackgroundColor.setColor(ContextCompat.getColor(AnalogWatchface.this, R.color.background_material_dark));
            mBackgroundColor.setAntiAlias(true);

            mSplotchPaint = new Paint();
            mSplotchPaint.setStrokeWidth(7f);
            mSplotchPaint.setAntiAlias(true);
            mSplotchPaint.setStyle(Paint.Style.FILL);

            mSplotchOutline = new Paint();
            mSplotchOutline.setAntiAlias(true);
            mSplotchOutline.setStrokeWidth(3f);
            mSplotchOutline.setStyle(Paint.Style.STROKE);


            mShadowPaint = new Paint();
            mShadowPaint.setARGB(145, 200, 200, 200);
            mShadowPaint.setAntiAlias(true);

            mZmanPaint = new Paint();
            mZmanPaint.setARGB(255, 200, 200, 200);
            mZmanPaint.setStrokeWidth(2.f);
            mZmanPaint.setAntiAlias(true);
            mZmanPaint.setStrokeCap(Paint.Cap.ROUND);

            int handColor = ContextCompat.getColor(AnalogWatchface.this, R.color.background_floating_material_light);

            mSplotchOutline.setColor(handColor);

            mHourPaint = new Paint();
            mHourPaint.setAlpha(255);
            mHourPaint.setColor(handColor);
            mHourPaint.setStrokeWidth(8.f);
            mHourPaint.setAntiAlias(true);
            mHourPaint.setStrokeCap(Paint.Cap.ROUND);
            mHourPaint.setAntiAlias(true);

            mMinutePaint = new Paint();
            mMinutePaint.setAlpha(255);
            mMinutePaint.setColor(handColor);
            mMinutePaint.setStrokeWidth(5.f);
            mMinutePaint.setAntiAlias(true);
            mMinutePaint.setStrokeCap(Paint.Cap.ROUND);
            mMinutePaint.setAntiAlias(true);

            mUpcomingTextPaint = new Paint();
            mUpcomingTextPaint.setAlpha(255);
            mUpcomingTextPaint.setColor(handColor);
            mUpcomingTextPaint.setStrokeWidth(5.f);
            mUpcomingTextPaint.setAntiAlias(true);
            mUpcomingTextPaint.setStrokeCap(Paint.Cap.ROUND);
            mUpcomingTextPaint.setAntiAlias(true);
            mUpcomingTextPaint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            mUpcomingTextPaint.setTextSize(14);
            mUpcomingTextPaint.setShadowLayer(1, 0, 0, Color.BLACK);

            mSecondPaint = new Paint();
            mSecondPaint.setARGB(255, 255, 0, 0);
            mSecondPaint.setStrokeWidth(2.f);
            mSecondPaint.setAntiAlias(true);
            mSecondPaint.setStrokeCap(Paint.Cap.ROUND);

            mTickPaint = new Paint();
            mTickPaint.setARGB(100, 255, 255, 255);
            mTickPaint.setStrokeWidth(2.f);
            mTickPaint.setAntiAlias(true);


            earlyMorningColor = ContextCompat.getColor(AnalogWatchface.this, R.color.earlyMorningColor);
            morningColor = ContextCompat.getColor(AnalogWatchface.this, R.color.morningColor);
            daytimeColor = ContextCompat.getColor(AnalogWatchface.this, R.color.daytimeColor);
            afternoonColor = ContextCompat.getColor(AnalogWatchface.this, R.color.afternoonColor);
            laterAfternoonColor = ContextCompat.getColor(AnalogWatchface.this, R.color.lateAfternoonColor);
            nighttimeColor = ContextCompat.getColor(AnalogWatchface.this, R.color.nighttimeColor);
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtect = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onPropertiesChanged: low-bit ambient = " + mLowBitAmbient);
            }
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onTimeTick: ambient = " + isInAmbientMode());
            }
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            }
            if (mLowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
                mHourPaint.setAntiAlias(antiAlias);
                mMinutePaint.setAntiAlias(antiAlias);
                mSecondPaint.setAntiAlias(antiAlias);
                mTickPaint.setAntiAlias(antiAlias);
                mSplotchPaint.setAntiAlias(antiAlias);
                mShadowPaint.setAntiAlias(antiAlias);
            }


            mBackgroundColor.setColor(ContextCompat.getColor(AnalogWatchface.this, inAmbientMode ? R.color.black : R.color.background_material_dark));

            mShadowPaint.setAlpha(inAmbientMode ? 0 : 147);

            invalidate();

            // Whether the timer should be running depends on whether we're in ambient mode (as well
            // as whether we're visible), so we may need to start or stop the timer.
            updateTimer();
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);
            boolean inMuteMode = (interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE);
            if (mMute != inMuteMode) {
                mMute = inMuteMode;
                mHourPaint.setAlpha(inMuteMode ? 100 : 255);
                mMinutePaint.setAlpha(inMuteMode ? 100 : 255);
                mSecondPaint.setAlpha(inMuteMode ? 80 : 255);
                invalidate();
            }
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setTimeInMillis(System.currentTimeMillis());
            mSplotchPaint.setColor(getHighlightColor());

            int width = bounds.width();
            int height = bounds.height();
            float centerX = width / 2f;
            float centerY = height / 2f;
            int splotchRadiu = width / 16;
            int shadowRadius = (width / 5);

            /*Draw background*/
            canvas.drawRect(width, height, 0, 0, mBackgroundColor);

            /*Draw the Shadow */
            canvas.drawCircle(centerX, centerY, shadowRadius, mShadowPaint);

            // Draw the ticks.
            float innerTickRadius = centerX - 10;
            float outerTickRadius = centerX;
            for (int tickIndex = 0; tickIndex < 12; tickIndex++) {
                float tickRot = (float) (tickIndex * Math.PI * 2 / 12);
                float innerX = (float) Math.sin(tickRot) * innerTickRadius;
                float innerY = (float) -Math.cos(tickRot) * innerTickRadius;
                float outerX = (float) Math.sin(tickRot) * outerTickRadius;
                float outerY = (float) -Math.cos(tickRot) * outerTickRadius;

                if ((((mTime.get(Calendar.MINUTE) > 30) && tickIndex == 3)) || (mTime.get(Calendar.MINUTE) <= 30 && tickIndex == 9))
                    continue;


                canvas.drawLine(centerX + innerX, centerY + innerY,
                        centerX + outerX, centerY + outerY, mTickPaint);
            }

            float secRot = mTime.get(Calendar.SECOND) / 30f * (float) Math.PI;
            int minutes = mTime.get(Calendar.MINUTE);
            float minRot = minutes / 30f * (float) Math.PI;
            float hrRot = ((mTime.get(Calendar.HOUR_OF_DAY) + (minutes / 60f)) / 6f) * (float) Math.PI;


            float handModifier = (shadowRadius / 3);
            float secLength = (float) (shadowRadius + handModifier * 1.5);
            float minLength = shadowRadius + shadowRadius;
            float hrLength = shadowRadius + (shadowRadius / 3);

            if (!isInAmbientMode()) {
                float secX = (float) Math.sin(secRot) * secLength;
                float secY = (float) -Math.cos(secRot) * secLength;
                canvas.drawLine(centerX, centerY, centerX + secX, centerY + secY, mSecondPaint);
            }


            float minX = (float) Math.sin(minRot) * minLength;
            float minY = (float) -Math.cos(minRot) * minLength;
            canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY, mMinutePaint);

            float hrX = (float) Math.sin(hrRot) * hrLength;
            float hrY = (float) -Math.cos(hrRot) * hrLength;
            canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY, mHourPaint);


            canvas.drawCircle(centerX, centerY, splotchRadiu, isInAmbientMode() && mBurnInProtect ? mBackgroundColor : mSplotchPaint);
            canvas.drawCircle(centerX, centerY, splotchRadiu, mSplotchOutline);

            if (ZmanimCalculator.getInstance().getUpcomingZman() == null)
                return;

            //Draw the Dates
            String hebrewDate = HebrewDate.getInstance().formatNumberToGematria(HebrewDate.getTimeSensitiveInstance().getHebrewDayOfMonth());
            String englishDate = String.valueOf(mTime.get(Calendar.DAY_OF_MONTH));
            float dateX = 10;
            if (mTime.get(Calendar.MINUTE) > 30)
                dateX = width - 10 - mUpcomingTextPaint.measureText(hebrewDate);
            mUpcomingTextPaint.setTextSize(16);
            canvas.drawText(hebrewDate, dateX, centerY, mUpcomingTextPaint);

            Rect englishRect = new Rect();
            mUpcomingTextPaint.getTextBounds(englishDate, 0, englishDate.length(), englishRect);
            canvas.drawText(englishDate, dateX, centerY + englishRect.height() + 3, mUpcomingTextPaint);


            /*Draw the Zman hand*/

            long shaaZmanis = ZmanimCalculator.getInstance().getShaaZmanisGra();
            long lengthOfDay = shaaZmanis * 12;

            float zmanRot = (float) (getTimeSinceZero() * Math.PI * 2 / lengthOfDay);
            zmanRot += Math.PI;
            float zmanX = (float) Math.sin(zmanRot) * splotchRadiu;
            float zmanY = (float) -Math.cos(zmanRot) * splotchRadiu;
            canvas.drawCircle(zmanX + centerX, zmanY + centerY, splotchRadiu / 3, mSplotchPaint);
            canvas.drawCircle(zmanX + centerX, zmanY + centerY, splotchRadiu / 3, mSplotchOutline);


//Draw upcoming Zman
            int insetSquareSide = (int) Math.hypot(splotchRadiu, splotchRadiu);
            Zman approachingZman = ZmanimCalculator.getInstance().getUpcomingZman();
            String title = approachingZman.hebrewName();
            Date approachingZmanTime = ZmanimCalculator.getInstance().getZmanTime(approachingZman);
            String time = DateFormatter.getInstance().formatTime(approachingZmanTime);
            title += " " + time;

            double shaddowHpyot = Math.hypot(hrLength, hrLength);
            double yOffset = centerY + Math.sqrt(Math.pow(shaddowHpyot, 2) - Math.pow(shadowRadius, 2));
            yOffset = centerY + splotchRadiu * 2;

            Rect titleBounds = new Rect();
            setTextSizeForWidth(mUpcomingTextPaint, (float) shaddowHpyot, title);
            mUpcomingTextPaint.getTextBounds(title, 0, title.length(), titleBounds);
            int titleHeight = titleBounds.height();
            canvas.drawText(title, centerX - (mUpcomingTextPaint.measureText(title) / 2), (float) (yOffset + titleHeight), mUpcomingTextPaint);
        }


        private void setTextSizeForWidth(Paint paint, float desiredWidth,
                                         String text) {

            // Pick a reasonably large value for the test. Larger values produce
            // more accurate results, but may cause problems with hardware
            // acceleration. But there are workarounds for that, too; refer to
            // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
            final float testTextSize = 48f;

            // Get the bounds of the text, using our testTextSize.
            paint.setTextSize(testTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            // Calculate the desired size as a proportion of our testTextSize.
            float desiredTextSize = testTextSize * desiredWidth / bounds.width();

            // Set the paint for that size.
            paint.setTextSize(desiredTextSize);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.setTimeZone(TimeZone.getDefault());
                mTime.setTimeInMillis(System.currentTimeMillis());
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            AnalogWatchface.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            AnalogWatchface.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "updateTimer");
            }
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private long getTimeSinceZero() {
            Date now = new Date();
            Date shkia = ZmanimCalculator.getInstance().getZmanTime(Zman.SHKIA);
            Date sunrise = ZmanimCalculator.getInstance().getZmanTime(Zman.HANEITZ);
            if (shkia == null)
                return mTime.getTimeInMillis();
            long currentTime = mTime.getTimeInMillis();
            if (shkia.before(now))
                return currentTime - shkia.getTime();
            if (!now.after(sunrise)) {
                Calendar yesterdayShkia = Calendar.getInstance();
                yesterdayShkia.setTime(shkia);
                yesterdayShkia.add(Calendar.HOUR_OF_DAY, -24);
                return currentTime - yesterdayShkia.getTime().getTime();
            }
            return currentTime - sunrise.getTime();
        }

        private void setHighlightTimes() {
            if (ZmanimCalculator.getInstance().getZmanTime(Zman.SHKIA) == null)
                return;
            nightTime = ZmanimCalculator.getInstance().getZmanTime(Zman.TZEIS).getTime();
            lateAfternoonTime = ZmanimCalculator.getInstance().getZmanTime(Zman.PLAG_HAMINCHA).getTime();
            afternoonTime = ZmanimCalculator.getInstance().getZmanTime(Zman.MINCHA_KETANA).getTime();
            daytime = ZmanimCalculator.getInstance().getZmanTime(Zman.TEFILA_GRA).getTime();
            morningTime = ZmanimCalculator.getInstance().getZmanTime(Zman.HANEITZ).getTime();
            earlyMorningTime = ZmanimCalculator.getInstance().getZmanTime(Zman.ALOS).getTime();
        }

        private int getHighlightColor() {
            long currentTime = mTime.getTimeInMillis();
            if (currentTime > nightTime)
                return nighttimeColor;
            if (currentTime > lateAfternoonTime)
                return laterAfternoonColor;
            if (currentTime > afternoonTime)
                return afternoonColor;
            if (currentTime > daytime)
                return daytimeColor;
            if (currentTime > morningTime)
                return morningColor;
            if (currentTime > earlyMorningTime)
                return earlyMorningColor;
            return nighttimeColor;
        }

    }
}
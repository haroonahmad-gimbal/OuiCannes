package com.gimbal.kotlin.ouicannes.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.GimbalDebugger;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.PrivacyManager;
import com.gimbal.android.Visit;
import com.gimbal.kotlin.ouicannes.MainActivity;
import com.gimbal.kotlin.ouicannes.R;
import com.gimbal.kotlin.ouicannes.data.model.EventType;
import com.gimbal.kotlin.ouicannes.data.model.GimbalEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GimbalIntegration {
    private static final String GIMBAL_APP_API_KEY = "68b29fb0-ec63-4227-bab5-3ec394c1873f";


    private Application app;
    public Context     appContext;

    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;

    private static GimbalIntegration instance;

    public static GimbalIntegration init(Application app) {
        if (instance == null) {
            instance = new GimbalIntegration(app);
        }
        return instance;
    }

    public static GimbalIntegration instance() {
        if (instance == null) {
            throw new IllegalStateException("Gimbal integration not initialized from Application");
        }
        return instance;
    }

    private GimbalIntegration(Application app) {
        this.app = app;
        this.appContext = app.getApplicationContext();
    }



    public void onCreate() {
        Gimbal.setApiKey(app, GIMBAL_APP_API_KEY);
        GimbalDebugger.enablePlaceLogging();
        GimbalDebugger.enableStatusLogging();

        if (PrivacyManager.getInstance().getUserConsent(PrivacyManager.ConsentType.PLACES_CONSENT) == PrivacyManager.ConsentState.CONSENT_UNKNOWN) {

            // checks to see if GDPR consent is required to enable Gimbal place monitoring
            if (PrivacyManager.getInstance().getGdprConsentRequirement() == PrivacyManager.GdprConsentRequirement.REQUIREMENT_UNKNOWN) {
                // The Gimbal SDK hasn't yet been able to contact it's server to determine the devices location
                // This could be due to not yet having called Gimbal.setApiKey([Application], "...") or
                // because a network is not available - you will need to check again later
            }
            else if (PrivacyManager.getInstance().getGdprConsentRequirement() == PrivacyManager.GdprConsentRequirement.REQUIRED) {
                PrivacyManager.getInstance().setUserConsent(PrivacyManager.ConsentType.PLACES_CONSENT, PrivacyManager.ConsentState.CONSENT_GRANTED);
            }
            else {
                PrivacyManager.getInstance().setUserConsent(PrivacyManager.ConsentType.PLACES_CONSENT, PrivacyManager.ConsentState.CONSENT_GRANTED);
            }
        }
        // Setup PlaceEventListener
        placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {

                Log.d("Gimbal","Place enter");
                Intent intent = new Intent(appContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 1, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setSmallIcon(R.drawable.we_cannes_logo_lockuo)
                        .setContentTitle("You Entered a Place!")
                        .setContentText("Dwell here for 5 minutes to earn 5 points")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);
                notificationManager.notify(1, builder.build());
            }

            @Override
            public void onVisitStartWithDelay(Visit visit, int delayTimeInSeconds) {

            }

            @Override
            public void onVisitEnd(Visit visit) {
                String strPoints =  visit.getPlace().getAttributes().getValue("Points");
                int points = Integer.parseInt(strPoints);
                saveEvent( new GimbalEvent(EventType.PLACE_EXIT,visit.getPlace().getName(),visit.getDwellTimeInMillis(),points));
               int earnedPoints = GimbalPlacesRepo.calculateAndSetPoints(appContext,visit.getDwellTimeInMillis(),points);
               Utils.showTokensEarnedDialog(appContext,earnedPoints);
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);

        // Setup CommunicationListener
        communicationListener = new CommunicationListener() {
            @Override
            public Notification.Builder prepareCommunicationForDisplay(Communication communication,
                                                                       Visit visit, int notificationId) {

                // If you want a custom notification create and return it here
                return null;
            }


            @Override
            public void onNotificationClicked(List<Communication> communications) {
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);
    }

    public void saveEvent(GimbalEvent event){
        List<GimbalEvent> events = GimbalPlacesRepo.getGimbalVisits(appContext);
      if (events == null){
          events = new ArrayList<GimbalEvent>();
      }
      events.add(event);
      GimbalPlacesRepo.saveGimbalEvents(appContext, events);
    }

    public void onTerminate() {
        PlaceManager.getInstance().removeListener(placeEventListener);
        CommunicationManager.getInstance().removeListener(communicationListener);
    }
}

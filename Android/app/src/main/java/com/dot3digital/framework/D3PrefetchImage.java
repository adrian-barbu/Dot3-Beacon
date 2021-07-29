package com.dot3digital.framework;

import android.content.Context;

import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForZone;
import com.dot3digital.framework.model.Fleet;
import com.dot3digital.framework.model.Zone;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * @description     D3 Prefetch Image Engine
 *
 * @modified        Stelian
 */
public class D3PrefetchImage
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    /**
     * Define IGet Interface
     */
    public interface IGet
    {
        void onSuccess(ArrayList<String> imageUrls, ArrayList<BaseModel> datas);
        void onFailed(String reason);
    }

    /**
     * prefetchAllZoneImagesWithProgress
     *
     * @param iPrefetchAllZones
     */
    public static void prefetchAllZoneImagesWithProgress(final IGet iPrefetchAllZones) {
        D3Core.getFirebaseRef()
            .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ZONES_URL)
            .addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> imageUrls = new ArrayList<String>();
                        ArrayList<BaseModel> zones = new ArrayList<BaseModel>();

                        if (dataSnapshot != null) {
                            for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                Zone zone = new Zone(dss.getKey());
                                if (zone.parseSnapshot(dss)) {
                                    zones.add(zone);

                                    // catch image url
                                    String zoneImageUrl = zone.getImage();
                                    if (zoneImageUrl != null && !zoneImageUrl.isEmpty())
                                        imageUrls.add(zoneImageUrl);
                                }
                            }
                        }

                        // Call callback
                        if (iPrefetchAllZones != null)
                            iPrefetchAllZones.onSuccess(imageUrls, zones);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // System.out.println("The read failed: " + firebaseError.getMessage());

                        // Call callback
                        if (iPrefetchAllZones != null)
                            iPrefetchAllZones.onFailed(firebaseError.getMessage());
                    }
                });
    }


    /**
     * prefetchAllZoneEntriesImagesWithProgress
     *
     * @param iPrefetchAllZoneEntries
     */
    public static void prefetchAllZoneEntriesImagesWithProgress(final ArrayList<BaseModel> zones, final IGet iPrefetchAllZoneEntries) {
        D3Core.getFirebaseRef()
            .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ENTRIES_FOR_ZONE)
            .orderByChild("deleted").startAt().endAt(false) // Sorting + filtering.
            .addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> imageUrls = new ArrayList<String>();

                            if (dataSnapshot != null) {
                                for (DataSnapshot dssTop : dataSnapshot.getChildren()) {
                                    // Check this zone entry included in main zone
                                    String key = dssTop.getKey();
                                    if (isContainedInZones(key, zones)) {
                                        // Get child entries
                                        for (DataSnapshot dss : dssTop.getChildren()) {
                                            EntryForZone entry = new EntryForZone(dss.getKey());
                                            if (entry.parseSnapshot(dss)) {
                                                // entries.add(entry);
                                                String entryImageUrl = entry.getImage();
                                                if (entryImageUrl != null && !entryImageUrl.isEmpty())
                                                    imageUrls.add(entryImageUrl);
                                            }
                                        }
                                    }
                                }
                            }

                            // Call callback
                            if (iPrefetchAllZoneEntries != null)
                                iPrefetchAllZoneEntries.onSuccess(imageUrls, null);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            //System.out.println("The read failed: " + firebaseError.getMessage());

                            // Call callback
                            if (iPrefetchAllZoneEntries != null)
                                iPrefetchAllZoneEntries.onFailed(firebaseError.getMessage());
                        }
                    });
    }

    static boolean isContainedInZones(String zoneKey, ArrayList<BaseModel> zones) {
        if (zones == null)
            return false;

        boolean result = false;

        for (BaseModel data : zones) {
            Zone zone = (Zone) data;
            if (zoneKey.equals(zone.getNodeKey()))
            {
                result = true;
                break;
            }
        }

        return result;
    }
}

package com.dot3digital.framework.beaconing;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.dot3digital.framework.model.Fleet;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description     Bese Beacon Manager
 *
 *                  This routine will manage power & some of beacon settings
 *
 * @author          Stelian
 */

public class BaseBeaconService implements BeaconConsumer {
    // Variables

    Context mContext;                       // Context
    BackgroundPowerSaver mPowerSaver;       // Power Saver

    protected BeaconManager mBeaconManager;   // Beacon Manager
    protected Beacon mLastBeacon;             // Last Detected Beacon
    protected List<Region> mRegions;          // Beacon Regions
    protected List<Fleet> mFleets;            // Fleets (will be used to filter UUID)

    public BaseBeaconService(Context context)
    {
        mContext = context;
        mPowerSaver = new BackgroundPowerSaver(mContext);

        mRegions = new ArrayList<>();
    }

    /**
     * Set Context Of Beacon Manager
     *
     * Context can be changed while the app change its foreground activity.
     *
     * @param context
     */
    public void setContext(Context context)
    {
        mContext = context;
    }

    /**
     * Get Last Beacon
     *
     * @return
     */
    public Beacon getLastBeacon() {
        return mLastBeacon;
    }

    //////////////////////////////////////////////////////////
    /////////////////// Beacon Management ////////////////////
    //////////////////////////////////////////////////////////

    /**
     * Create Beacon Manager
     */
    public BeaconManager createBeaconManager()
    {
        if (mBeaconManager == null) {
            mBeaconManager = BeaconManager.getInstanceForApplication(mContext);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        }

        return mBeaconManager;
    }

    /**
     * On Resume Event Handler
     *
     * Maybe service will be run in foreground
     */
    public void onResume() {
        if (mBeaconManager.isBound(this)) {
            mBeaconManager.setBackgroundMode(false);
        }
    }

    /**
     * On Pause Event Handler
     *
     * Maybe service will be run in background
     */
    public void onPause() {
        if (mBeaconManager.isBound(this)) {
            mBeaconManager.setBackgroundMode(true);
        }
    }

    /**
     * On Destroy Event Handler
     *
     * Maybe service will be unbind because the app is not running
     */
    public void onDestroy() {
        mBeaconManager.unbind(this);
    }

    /**
     * Set Region For Detection Beacons
     *
     * This routine will be called whenever fleet array is updated.
     *
     * @param fleets
     */
    public void setRegion(List<Fleet> fleets)
    {
        if (mBeaconManager == null)
            createBeaconManager();

        /*
        // If new regions is set, then clear previous regions first.

        for (Region region : mRegions)
            removeRegion(region);

        mRegions.clear();

        // Make Region From Fleets
        mRegions = makeRegions(fleets);

        // If beacon service is already started, then update region as new
        if (mBeaconManager.isAnyConsumerBound())
            setRegion();
        */

        mFleets = fleets;
    }

    protected void setRegion()
    {
        if (mRegions == null || mRegions.size() == 0)
            return;

        // Add new regions

        for (Region region : mRegions)
            addRegion(region);
    }

    /**
     * Make Regions From Fleets
     *
     * @param fleets
     * @return
     */
    private List<Region> makeRegions(List<Fleet> fleets)
    {
        ArrayList<Region> regions = new ArrayList<Region>();
        for (Fleet fleet : fleets) {
            try {
                regions.add(new Region(fleet.getName(), Identifier.parse(fleet.getUUID()), null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return regions;
    }

    /**
     * Add Region
     *
     * @param region
     */
    protected void addRegion(Region region) {
        if (mBeaconManager == null)
            return;

        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove Region
     *
     * @param region
     */
    protected void removeRegion(Region region) {
        if (mBeaconManager == null)
            return;

        try {
            mBeaconManager.stopRangingBeaconsInRegion(region);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////
    /////////////////// BeaconConsumer Callback////////////////////
    ///////////////////////////////////////////////////////////////

    @Override
    public void onBeaconServiceConnect() {
        // Will be overrided in extended class
    }

    @Override
    public Context getApplicationContext() {
        return mContext;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        getApplicationContext().unbindService(conn);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return getApplicationContext().bindService(service, conn, flags);
    }

    /**
     * Find Nearest Beacon
     *
     * Also they will check if each beacon is allowed in this garden based on fleet
     * @param beacons
     * @return
     */
    protected Beacon getNearestBeacon(Collection<Beacon> beacons) {
        double nearest = -1;
        double dist;

        Beacon nearestBeacon = null;
        for (Beacon beacon : beacons) {
            // Check beacon
            if (!isAllowedBeacon(beacon))
                continue;

            // Compare
            dist = beacon.getDistance();

            if (nearest == -1 || dist < nearest) {
                nearest = dist;
                nearestBeacon = beacon;
            }
        }

        return nearestBeacon;
    }

    /**
     * Check if beacon is allowed in this garden
     *
     * @param beacon
     * @return
     */
    protected boolean isAllowedBeacon(Beacon beacon) {
        boolean allow = false;

        String beaconUuid = beacon.getId1().toString();
        for (Fleet fleet : mFleets) {
            if (fleet.getUUID().equalsIgnoreCase(beaconUuid)) {
                allow = true;
                break;
            }
        }

        return allow;
    }
}

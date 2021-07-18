package com.example.jiolauncher.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.example.jiolauncher.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppDetails {

    private final Context context;
    private static AppDetails appDetails = null;
    public static AppDetails getInstance(Context context)
    {
        if (appDetails == null)
            appDetails = new AppDetails(context);

        return appDetails;
    }
    private AppDetails(Context context) {
        this.context=context;
    }

    public List<AppInfo> AppDetail() {

        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon

        //ArrayList<AppInfo> appsList = getInstalledApps(false);


        PackageManager pm = context.getPackageManager();
        ArrayList<AppInfo> appsList = new ArrayList<AppInfo>();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("1", "Installed package :" + packageInfo.packageName);
            Log.d("1", "Source dir : " + packageInfo.sourceDir);
            Log.d("1", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));

            AppInfo app = new AppInfo();
            app.label = packageInfo.loadLabel(pm);
            app.packageName = packageInfo.packageName;//activityInfo.packageName;
            app.icon = packageInfo.loadIcon(pm);//activityInfo.loadIcon(pm);
            app.mainActivity=packageInfo.name;
          /*  if(packageInfo.name.contains(app.packageName)) {
                app.mainActivity=packageInfo.name.replace(app.packageName,"");
            }*/
            PackageInfo info = null;
            try {
                info = pm.getPackageInfo(
                        context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            app.versionName = info.versionName;
            app.versionCode=info.versionCode;
            if(pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                app.mainActivity=pm.getLaunchIntentForPackage(packageInfo.packageName).getComponent().getClassName();
                if( app.mainActivity.toString().contains(app.packageName)) {
                    app.mainActivity= app.mainActivity.toString().replace(app.packageName+".","");
                }
                appsList.add(app);
            }

        }


        /*Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:allApps) {
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm);
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            app.mainActivity=ri.activityInfo.name;
            if(ri.activityInfo.name.contains(app.packageName)) {
                app.mainActivity=ri.activityInfo.name.replace(app.packageName,"");
            }
            PackageInfo info = null;
            try {
                info = pm.getPackageInfo(
                        context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            app.versionName = info.versionName;
            app.versionCode=info.versionCode;

            appsList.add(app);
        }*/
        Collections.sort(appsList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return  o1.label.toString().compareTo(o2.label.toString());
            }
        });
        return appsList;

    }

    private ArrayList<AppInfo> getPackages() {
        ArrayList<AppInfo> apps = getInstalledApps(true); /* false = no system packages */
      /*  final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i);
        }*/
        return apps;
    }

    private ArrayList<AppInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppInfo> res = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();

        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            AppInfo newInfo = new AppInfo();
            newInfo.label = p.applicationInfo.loadLabel(pm).toString();
            newInfo.packageName = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(pm);
            res.add(newInfo);
        }
        return res;
    }
}

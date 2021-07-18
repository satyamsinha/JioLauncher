package com.example.jiolauncher.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jiolauncher.R;
import com.example.jiolauncher.model.AppInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private static List<AppInfo> appsList= new ArrayList<>();

    public void setAppList(List<AppInfo> appLists) {
        this.appsList= appLists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtMainActivity;
        private final TextView txtVersion;
        private final TextView txtVersionCode;
        private final TextView txtPackage;
        public TextView textView;
        public ImageView img;


        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView = (TextView) itemView.findViewById(R.id.txt_app_name);
            img = (ImageView) itemView.findViewById(R.id.img_app);
            txtMainActivity = (TextView) itemView.findViewById(R.id.txt_main_activity);
            txtVersion = (TextView) itemView.findViewById(R.id.txt_version);
            txtVersionCode = (TextView) itemView.findViewById(R.id.txt_version_code);
            txtPackage = (TextView) itemView.findViewById(R.id.txt_package);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).packageName.toString());
            context.startActivity(launchIntent);
            Toast.makeText(v.getContext(), appsList.get(pos).label.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views

        String appLabel = appsList.get(i).label.toString();
        String appPackage = appsList.get(i).packageName.toString();
        Drawable appIcon = appsList.get(i).icon;

        TextView textView = viewHolder.textView;
        textView.setText( "App Name : "+appLabel );
        viewHolder.txtPackage.setText("PackageName :"+appsList.get(i).packageName);
        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);
        viewHolder.txtMainActivity.setText("ActivityName :"+appsList.get(i).mainActivity);
        viewHolder.txtVersion.setText("Version Name :" +appsList.get(i).versionName);
        viewHolder.txtVersionCode.setText("VersionCode :"+appsList.get(i).versionCode);
    }

    @Override
    public int getItemCount() {
        //This method needs to be overridden so that Androids knows how many items
        //will be making it into the list
        return appsList.size();
    }

    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

}
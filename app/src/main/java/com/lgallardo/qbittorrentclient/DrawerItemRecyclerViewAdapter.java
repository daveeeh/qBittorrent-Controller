/*
 *   Copyright (c) 2014-2015 Luis M. Gallardo D.
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the GNU Lesser General Public License v3.0
 *   which accompanies this distribution, and is available at
 *   http://www.gnu.org/licenses/lgpl.html
 *
 */

package com.lgallardo.qbittorrentclient;

/**
 * Created by lgallard on 28/08/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class DrawerItemRecyclerViewAdapter extends RecyclerView.Adapter<DrawerItemRecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ITEM_ACTIVE = 2;
    private static final int TYPE_SERVER = 3;
    private static final int TYPE_SERVER_ACTIVE = 4;
    private static final int TYPE_SERVERS = 5;
    private static final int TYPE_CATEGORY = 6;
    private static final int TYPE_CATEGORY_ACTIVE = 7;
    private static final int TYPE_CATEGORIES = 8;


    // All items
    public static ArrayList<DrawerItem> items;

    // SUb items
    public static ArrayList<DrawerItem> serverItems;
    public static ArrayList<DrawerItem> actionItems;
    public static ArrayList<DrawerItem> settingsItems;
    public static ArrayList<DrawerItem> categoryItems;

    public static int actionPosition = 0;

    private static MainActivity mainActivity;
    private static int drawerOffset = 1;

    private Context context;


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        int positionInItems = -1;

        // New
        ImageView imageViewIcon;
        TextView textViewName;
        TextView textViewArrow;

        // Creating ViewHolder Constructor with View and viewType As a parameter
        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            Holderid = 0;
            if (ViewType != TYPE_HEADER) {
                itemView.setClickable(true);
                itemView.setOnClickListener(this);
                Holderid = 1;
            }

//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - ViewType: " + ViewType);
//            if (ViewType == TYPE_SERVER || ViewType == TYPE_SERVER_ACTIVE) {
//                drawerOffset = drawerOffset + 1;
//            }

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewArrow = itemView.findViewById(R.id.textViewDrawerArrow);

        }


        // In order to track the item position in RecyclerView
        // Handle item click and set the selection
        @Override
        public void onClick(View view) {

//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick - serverItems size: " + DrawerItemRecyclerViewAdapter.serverItems.size());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick - actionItems size: " + DrawerItemRecyclerViewAdapter.actionItems.size());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick - settingsItems size: " + DrawerItemRecyclerViewAdapter.settingsItems.size());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick - categoryItems size: " + DrawerItemRecyclerViewAdapter.categoryItems.size());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick - items size: " + DrawerItemRecyclerViewAdapter.items.size());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - offSetPosition: " + (drawerOffset));
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - getLayoutPosition: " + getLayoutPosition());
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - positionInItems: " + positionInItems);


            DrawerItem drawerItem;

            // Check and toggle servers
            if (getLayoutPosition() == 1) {

                drawerItem = items.get(0);

//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - serverItems.size:  " + serverItems.size());
//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - items.size:  " + items.size());
//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - drawerItem.name:  " + drawerItem.name);
//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - drawerItem.getType():  " + drawerItem.getType());
//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - toggled Servers ");

                if (drawerItem.isActive()) {

//                    Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Servers Category active");

                    // Remove all server items
                    removeServerItems();
                    drawerItem.setActive(false);

                    drawerOffset = 1;


                } else {

//                    Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Servers Category inactive");

                    // Insert all server items
                    for (int i = 0; i < serverItems.size(); i++) {

                        DrawerItem item = serverItems.get(i);

                        if (item.getType() == TYPE_SERVER || item.getType() == TYPE_SERVER_ACTIVE) {
                            items.add(i, serverItems.get(i));
                            notifyItemInserted(i + 1);
                        }
                    }

                    drawerItem.setActive(true);
                    drawerItem.setArrow("▲");
                    notifyItemChanged(1);
                    drawerOffset = serverItems.size();
                }


                items.set(0, drawerItem);

            } else {


                // Get action position
                actionPosition = getLayoutPosition() - drawerOffset;

                int layoutPosition = getLayoutPosition();

                drawerItem = items.get(getLayoutPosition() - 1);

                // Get Action
//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Action => " + drawerItem.getAction());

                // Disable all items

                int lastActionPosition = 1;

                for (int i = 0; i < items.size(); i++) {
                    DrawerItem item = items.get(i);


                    if ((drawerItem.getType() == TYPE_ITEM || drawerItem.getType() == TYPE_ITEM_ACTIVE) &&
                            (item.getType() == TYPE_ITEM || item.getType() == TYPE_ITEM_ACTIVE)) {

                        // Get las action position selected

                        if (item.isActive()) {
                            lastActionPosition = i;
                        }
                        item.setActive(false);
                    }

                    if ((drawerItem.getType() == TYPE_SERVER || drawerItem.getType() == TYPE_SERVER_ACTIVE) &&
                            (item.getType() == TYPE_SERVER || item.getType() == TYPE_SERVER_ACTIVE)) {
                        item.setActive(false);
                    }

//                    if ((drawerItem.getType() == TYPE_CATEGORY || drawerItem.getType() == TYPE_CATEGORY_ACTIVE) &&
//                            (item.getType() == TYPE_CATEGORY || item.getType() == TYPE_CATEGORY_ACTIVE)) {
//                        item.setActive(false);
//                    }

                    items.set(i, item);
                }

                // Mark new item as active
//                drawerItem.setActive(true);
//                items.set(layoutPosition - 1, drawerItem);


                // Perform Action

                // Change current server


                if (drawerItem.getAction().equals("changeCurrentServer")) {

//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer ");


                    if (MainActivity.packageName.equals("com.lgallardo.qbittorrentclient") && items.indexOf(drawerItem) > 1) {

//                        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - items.indexOf(drawerItem): " + items.indexOf(drawerItem));


                        mainActivity.genericOkDialog(R.string.settings_qbittorrent_pro_title, R.string.settings_qbittorrent_pro_message);

                        // Disable server selection
                        drawerItem.setActive(false);
                        items.set(layoutPosition - 1, drawerItem);
                        notifyItemChanged(layoutPosition);

                        // Force first server
                        DrawerItem item = items.get(1);
                        item.setActive(true);
                        items.set(1, item);
                        notifyItemChanged(1);


                    } else {

                        drawerItem.setActive(true);
                        items.set(layoutPosition - 1, drawerItem);
                        notifyItemChanged(layoutPosition);

                        int currentServerValue = serverItems.indexOf(drawerItem);

                        if (currentServerValue < 0) {
                            currentServerValue = 0;
                        }

//                        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - currentServerValue: " + currentServerValue);


                        changeCurrentServer(currentServerValue);

//                    mainActivity.refreshCurrent();


                    }
                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }


                // Refresh All
                if (drawerItem.getAction().equals("refreshAll")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("all", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Refresh Downloading
                if (drawerItem.getAction().equals("refreshDownloading")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("downloading", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Refresh Completed
                if (drawerItem.getAction().equals("refreshCompleted")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("completed", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }


                // Refresh Seeding
                if (drawerItem.getAction().equals("refreshSeeding")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("seeding", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }
                // Refresh Pause
                if (drawerItem.getAction().equals("refreshPaused")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("pause", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }


                // Refresh Active
                if (drawerItem.getAction().equals("refreshActive")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("active", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }


                // Refresh Inactive
                if (drawerItem.getAction().equals("refreshInactive")) {

                    drawerItem.setActive(true);
                    items.set(layoutPosition - 1, drawerItem);
                    notifyItemChanged(layoutPosition);
                    mainActivity.refreshFromDrawerAction("inactive", drawerItem.name);

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Open Settings
                if (drawerItem.getAction().equals("openSettings")) {

                    // Set last action position
                    activeLastActionPosition(lastActionPosition);
                    mainActivity.openSettings();

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }


                // Open Options
                if (drawerItem.getAction().equals("openOptions")) {

                    // Set last action position
                    //activeLastActionPosition(lastActionPosition);


                    //mainActivity.getAndOpenOptions();

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Get Pro
                if (drawerItem.getAction().equals("getPro")) {

                    // Set the last action position
                    activeLastActionPosition(lastActionPosition);
                    mainActivity.getPRO();

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Open Help
                if (drawerItem.getAction().equals("openHelp")) {

                    DrawerItem item = items.get(lastActionPosition);
                    item.setActive(true);
                    items.set(lastActionPosition, item);
                    mainActivity.openHelp();

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);
                }

                // Clicked on categories
                if (drawerItem.getAction().equals("categories")) {

                    if (drawerItem.isActive()) {

                        // Set as inactive
                        drawerItem.setActive(false);
                        items.set(getLayoutPosition() - 1, drawerItem);

                        // Remove all category items
                        removeCategoryItems();

                    } else {

                        // Set as active
                        drawerItem.setArrow("▲");
                        notifyItemChanged(11);
                        drawerItem.setActive(true);
                        items.set(getLayoutPosition() - 1, drawerItem);

                        // Insert all category items
                        for (int i = 0; i < categoryItems.size(); i++) {

                            DrawerItem item = categoryItems.get(i);

                            if (item.getType() == TYPE_CATEGORY || item.getType() == TYPE_CATEGORY_ACTIVE) {
                                items.add(items.size(), item);
                                notifyItemInserted(items.size());
                            }
                        }
                    }

                    // Scroll drawer
                    mainActivity.mRecyclerView.scrollToPosition(items.size());
                }

                // Clicked on category
                if (drawerItem.getAction().equals("category")) {

                    if (drawerItem.name.equals(mainActivity.getResources().getString(R.string.drawer_category_all))) {
                        mainActivity.saveLastCategory(mainActivity.getResources().getString(R.string.drawer_category_all));
                    } else {
                        mainActivity.saveLastCategory(drawerItem.name);
                    }

//                    Log.d("Debug", "[DrawerItemRecyclerViewAdapter] category: " + drawerItem.name);

                    mainActivity.setSelectionAndTitle(mainActivity.currentState);

                    mainActivity.refreshCurrent();

                    // Close drawer
                    mainActivity.drawerLayout.closeDrawer(mainActivity.mRecyclerView);


                }


                // Remove all server items
                removeServerItems();

                // Toggle servers
                drawerItem = items.get(0);
                drawerItem.setActive(false);
                items.set(0, drawerItem);

                drawerOffset = 1;

                // Load banner
                mainActivity.loadBanner();

            }
        }

    }


    DrawerItemRecyclerViewAdapter(Context context, MainActivity mainActivity, ArrayList<DrawerItem> serverItems, ArrayList<DrawerItem> actionItems, ArrayList<DrawerItem> settingsItems, ArrayList<DrawerItem> categoryItems) {

        this.mainActivity = mainActivity;
        this.context = context;


        // All items
        DrawerItemRecyclerViewAdapter.serverItems = serverItems;
        DrawerItemRecyclerViewAdapter.actionItems = actionItems;
        DrawerItemRecyclerViewAdapter.settingsItems = settingsItems;
        DrawerItemRecyclerViewAdapter.categoryItems = categoryItems;

        DrawerItemRecyclerViewAdapter.items = new ArrayList<DrawerItem>();

        // Add items
        DrawerItemRecyclerViewAdapter.items.addAll(serverItems);
        DrawerItemRecyclerViewAdapter.items.addAll(actionItems);
        DrawerItemRecyclerViewAdapter.items.addAll(settingsItems);

        if (categoryItems != null) {
            DrawerItemRecyclerViewAdapter.items.addAll(categoryItems);
        } else {
            DrawerItemRecyclerViewAdapter.categoryItems = new ArrayList<DrawerItem>();
        }

//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - Constructor - serverItems size: " + DrawerItemRecyclerViewAdapter.serverItems.size());
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - Constructor - actionItems size: " + DrawerItemRecyclerViewAdapter.actionItems.size());
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - Constructor - settingsItems size: " + DrawerItemRecyclerViewAdapter.settingsItems.size());
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - Constructor - categoryItems size: " + DrawerItemRecyclerViewAdapter.categoryItems.size());
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - Constructor - items size: " + DrawerItemRecyclerViewAdapter.items.size());

        drawerOffset = 1;

        DrawerItem drawerItem;

//        // Add server items to array
//        for (int i = 0; i < items.size(); i++) {
//            ObjectDrawerItem item = items.get(i);
//
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Adding to items: " + items.get(i).name);
//
//            if (item.getType() == TYPE_SERVER || item.getType() == TYPE_SERVER_ACTIVE) {
//                serverItems.add(item);
//            }
//        }


        // Remove all server items
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {

            DrawerItem item = (DrawerItem) iterator.next();

//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Analysing: " + item.name);
//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Action is: " + item.getAction());

            if (item.getType() == TYPE_SERVER || item.getType() == TYPE_SERVER_ACTIVE) {

//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Removing: " + item.name);
                iterator.remove();
            }
        }
    }


    private void removeServerItems() {
        // Remove all server items
        ListIterator iterator = items.listIterator();

        while (iterator.hasNext()) {

            DrawerItem item = (DrawerItem) iterator.next();

//            Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Analysing: " + item.name);

            if (item.getType() == TYPE_SERVERS) {
                item.setArrow("▼");
                notifyItemChanged(iterator.nextIndex());
            }

            if (item.getType() == TYPE_SERVER || item.getType() == TYPE_SERVER_ACTIVE) {

//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Removing: " + item.name);
                iterator.remove();
                notifyItemRemoved(iterator.nextIndex() + 1);

            }
        }
    }

    private void removeCategoryItems() {
        // Remove all server items
        ListIterator iterator = items.listIterator();

        while (iterator.hasNext()) {

            DrawerItem item = (DrawerItem) iterator.next();

//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] : Removing item: " + item.name);

            if (item.getType() == TYPE_CATEGORIES) {
                item.setArrow("▼");
                notifyItemChanged(iterator.nextIndex());
            }

            if (item.getType() == TYPE_CATEGORY || item.getType() == TYPE_CATEGORY_ACTIVE) {

//                Log.d("Debug", "DrawerItemRecyclerViewAdapter - OnClick() - Removing: " + item.name);
                iterator.remove();
                notifyItemRemoved(iterator.nextIndex() + 1);

            }
        }
    }

    private void activeLastActionPosition(int lastActionPosition) {

        DrawerItem item = items.get(lastActionPosition);
        item.setActive(true);
        items.set(lastActionPosition, item);

    }

    private void changeCurrentServer(int currentServerValue) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);


        // Get values from selected server
        String hostname = sharedPrefs.getString("hostname" + currentServerValue, "");
        String subfolder = sharedPrefs.getString("subfolder" + currentServerValue, "");
        String protocol;

        String port = sharedPrefs.getString("port" + currentServerValue, "8080");

        String username = sharedPrefs.getString("username" + currentServerValue, "NULL");
        String password = sharedPrefs.getString("password" + currentServerValue, "NULL");

        boolean https = sharedPrefs.getBoolean("https" + currentServerValue, false);

        String serverValue = sharedPrefs.getString("currentServer", "1");

        // Check https
        if (https) {
            protocol = "https";
        } else {
            protocol = "http";
        }


        // Debug
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - serverValue: " + serverValue);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - changeCurrentServer: " + currentServerValue);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - hostname: " + hostname);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - subfolder: " + subfolder);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - protocol: " + protocol);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - port: " + port);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - https: " + https);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - username: " + username);
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - changeCurrentServer - password: " + password);


        // Save values
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Save key-values
        editor.putString("currentServer", "" + currentServerValue);
        editor.putString("hostname", hostname);
        editor.putString("subfolder", subfolder);
        editor.putString("protocol", protocol);
        editor.putString("port", port);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("qbCookie", "");


        // Commit changes
        editor.apply();


        mainActivity.changeCurrentServer();
    }

    public void refreshDrawer(ArrayList<DrawerItem> serverItems, ArrayList<DrawerItem> actionItems, ArrayList<DrawerItem> settingsItems, ArrayList<DrawerItem> categoryItems) {

        DrawerItemRecyclerViewAdapter.serverItems = serverItems;
        DrawerItemRecyclerViewAdapter.actionItems = actionItems;
        DrawerItemRecyclerViewAdapter.settingsItems = settingsItems;


        DrawerItemRecyclerViewAdapter.items = new ArrayList<DrawerItem>();

        // Add items
        DrawerItemRecyclerViewAdapter.items.addAll(serverItems);
        DrawerItemRecyclerViewAdapter.items.addAll(actionItems);
        DrawerItemRecyclerViewAdapter.items.addAll(settingsItems);

        if (categoryItems != null) {
            DrawerItemRecyclerViewAdapter.items.addAll(categoryItems);
        }

        // Refresh
        notifyDataSetChanged();

    }

    public void refreshDrawer() {

        DrawerItemRecyclerViewAdapter.serverItems = serverItems;


        DrawerItemRecyclerViewAdapter.items = new ArrayList<DrawerItem>();

        // Add items
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.serverItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.actionItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.settingsItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.categoryItems);

        // Close servers
        removeServerItems();

        // Refresh
        notifyDataSetChanged();

    }

    public void refreshDrawerServers(ArrayList<DrawerItem> serverItems) {

        DrawerItemRecyclerViewAdapter.serverItems = serverItems;


        DrawerItemRecyclerViewAdapter.items = new ArrayList<DrawerItem>();

        // Add items
        DrawerItemRecyclerViewAdapter.items.addAll(serverItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.actionItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.settingsItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.categoryItems);

        // Close servers
        removeServerItems();

        // Refresh
        notifyDataSetChanged();

    }

    public void refreshDrawerCategories(ArrayList<DrawerItem> categoryItems) {

        DrawerItemRecyclerViewAdapter.categoryItems = categoryItems;


        DrawerItemRecyclerViewAdapter.items = new ArrayList<DrawerItem>();

        // Add items
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.serverItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.actionItems);
        DrawerItemRecyclerViewAdapter.items.addAll(DrawerItemRecyclerViewAdapter.settingsItems);
        DrawerItemRecyclerViewAdapter.items.addAll(categoryItems);

        // Close servers
        removeServerItems();

        // Refresh
        notifyDataSetChanged();

    }


    //Below first we override the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public DrawerItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        Log.d("Debug", "[DrawerItemRecyclerViewAdapter] OnClick() - ViewType: " + viewType);
        if (viewType == TYPE_SERVER || viewType == TYPE_SERVER_ACTIVE) {
            drawerOffset = drawerOffset + 1;
        }

        //inflate your layout and pass it to view holder
        if (viewType == TYPE_SERVERS) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_servers_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
            return vhItem; // Returning the created object

        } else if (viewType == TYPE_CATEGORIES) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_servers_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
            return vhItem; // Returning the created object


        } else if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_ITEM_ACTIVE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_active, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_SERVER || viewType == TYPE_CATEGORY) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_subitem_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_SERVER_ACTIVE || viewType == TYPE_CATEGORY_ACTIVE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_subitem_active_row, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }

        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerItemRecyclerViewAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image

            DrawerItem item = items.get(position - 1);
            holder.imageViewIcon.setImageResource(item.icon);
            holder.textViewName.setText(item.name);
            holder.positionInItems = (position - 1);
            if(holder.textViewArrow!=null)
                holder.textViewArrow.setText(item.arrow);

        } else {
            // header
            return;
        }
    }


    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        // Return the number of items in the list (header + item actions)
        return items.size() + 1;
    }


    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {

//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - items.size(): " + items.size());
//        Log.d("Debug", "[DrawerItemRecyclerViewAdapter] position: " + position);

        if (isPositionHeader(position)) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_HEADER");
            return TYPE_HEADER;
        }

        if (items.get(position - 1).getType() == TYPE_ITEM && items.get(position - 1).isActive()) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_ITEM_ACTIVE");
            return TYPE_ITEM_ACTIVE;
        }

        if (items.get(position - 1).getType() == TYPE_SERVERS && !(items.get(position - 1).isActive())) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_SERVERS");
            return TYPE_SERVERS;
        }

        if (items.get(position - 1).getType() == TYPE_SERVERS && items.get(position - 1).isActive()) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_SERVERS");
            return TYPE_SERVERS;
        }

        if (items.get(position - 1).getType() == TYPE_CATEGORIES && !(items.get(position - 1).isActive())) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_CATEGORIES");
            return TYPE_CATEGORIES;
        }

        if (items.get(position - 1).getType() == TYPE_CATEGORIES && items.get(position - 1).isActive()) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_CATEGORIES");
            return TYPE_CATEGORIES;
        }


        if (items.get(position - 1).getType() == TYPE_SERVER && !(items.get(position - 1).isActive())) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_SERVER");
            return TYPE_SERVER;
        }

        if (items.get(position - 1).getType() == TYPE_SERVER && items.get(position - 1).isActive()) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_SERVER_ACTIVE");
            return TYPE_SERVER_ACTIVE;
        }


        if (items.get(position - 1).getType() == TYPE_CATEGORY && !(items.get(position - 1).isActive())) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_CATEGORY");
            return TYPE_CATEGORY;
        }

        if (items.get(position - 1).getType() == TYPE_CATEGORY && items.get(position - 1).isActive()) {
//            Log.d("Debug", "[DrawerItemRecyclerViewAdapter] TYPE_CATEGORY_ACTIVE");
            return TYPE_CATEGORY_ACTIVE;
        }

        // Default
//        Log.d("Debug", "DrawerItemRecyclerViewAdapter - TYPE_ITEM");
        return TYPE_ITEM;

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

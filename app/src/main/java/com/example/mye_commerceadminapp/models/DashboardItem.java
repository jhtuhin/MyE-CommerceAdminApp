package com.example.mye_commerceadminapp.models;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.utils.AdminConstants;

import java.util.ArrayList;
import java.util.List;

public class DashboardItem {
    private int icon;
    private String itemName;

    public DashboardItem(int icon,String itemName) {
        this.icon=icon;
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static List<DashboardItem> getDashboardItems(){
        final ArrayList<DashboardItem> items=new ArrayList<>();
        items.add(new DashboardItem(R.drawable.ic_add_product, AdminConstants.Item.ADD_PRODUCT));
        items.add(new DashboardItem(R.drawable.ic_category, AdminConstants.Item.ADD_CATEGORY));
        items.add(new DashboardItem(R.drawable.ic_view_products, AdminConstants.Item.VIEW_PRODUCT));
        items.add(new DashboardItem(R.drawable.ic_view_user, AdminConstants.Item.VIEW_USERS));
        items.add(new DashboardItem(R.drawable.ic_view_orders, AdminConstants.Item.VIEW_ORDERS));
        items.add(new DashboardItem(R.drawable.ic_report, AdminConstants.Item.VIEW_REPORTS));
        items.add(new DashboardItem(R.drawable.ic_settings, AdminConstants.Item.SETTINGS));
        return items;
    }
}

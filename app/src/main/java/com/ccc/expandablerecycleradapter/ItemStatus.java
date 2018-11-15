package com.ccc.expandablerecycleradapter;

/**
 * Created by ccc on 2018/11/13.
 *
 */
public class ItemStatus {

    private int viewType;
    private int groupItemIndex = 0;
    private int subItemIndex = -1;

    public ItemStatus() {

    }

    public ItemStatus(int viewType, int groupItemIndex, int subItemIndex) {
        this.viewType = viewType;
        this.groupItemIndex = groupItemIndex;
        this.subItemIndex = subItemIndex;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getGroupItemIndex() {
        return groupItemIndex;
    }

    public void setGroupItemIndex(int groupItemIndex) {
        this.groupItemIndex = groupItemIndex;
    }

    public int getSubItemIndex() {
        return subItemIndex;
    }

    public void setSubItemIndex(int subItemIndex) {
        this.subItemIndex = subItemIndex;
    }
}

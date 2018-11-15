package com.ccc.expandablerecycleradapter;

import java.util.List;

/**
 * Created by ccc on 2018/11/15.
 *
 */
public interface ItemBean {
    List getSubItems();
    boolean isExpand();
}

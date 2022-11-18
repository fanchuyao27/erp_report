package com.sfp.utils;

import java.io.Serializable;
import java.util.List;

public class MenuInfo implements Serializable {

    public String title;
    public String icon;
    public String href;
    public String target;
    public List<MenuInfo> child;

    public MenuInfo() {
    }

    public MenuInfo(String title, String icon, String href, String target, List<MenuInfo> child) {
        this.title = title;
        this.icon = icon;
        this.href = href;
        this.target = target;
        this.child = child;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<MenuInfo> getChild() {
        return child;
    }

    public void setChild(List<MenuInfo> child) {
        this.child = child;
    }
}

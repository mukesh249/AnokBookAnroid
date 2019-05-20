package com.anokbook.Models;

import java.io.Serializable;

public class ShowingCategoriesModel implements Serializable {
    String name,check,id;
    private boolean isSelected = false;

    public String getName() {
        return name;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}

package com.scriptsbundle.adforest.modelsList;

import java.io.Serializable;

public class subcatDiloglist implements Serializable {

    public String id;
    public String title;
    public String name;
    public String image;


    public String image_url;
    private boolean hasSub;
    private boolean hasCustom;
    private boolean isShow;
    private boolean isBidding;

    public void setImage(String img){
        this.image=img;
    }
    public String getImage(){
        return image;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    private boolean isPaid;

    public boolean isBidding() {
        return isBidding;
    }

    public void setBidding(boolean bidding) {
        isBidding = bidding;
    }


    public boolean isHasCustom() {
        return hasCustom;
    }

    public void setHasCustom(boolean hasCustom) {
        this.hasCustom = hasCustom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasSub() {
        return hasSub;
    }

    public void setHasSub(boolean hasSub) {
        this.hasSub = hasSub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}

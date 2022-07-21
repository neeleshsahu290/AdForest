package com.scriptsbundle.adforest.modelsList;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ChatMessage {


    private  String mediatype;
    private int  is_media;
    private String body, sender, receiver,file_format;
    private boolean isMine;
    private String date;
    private String image;
    private  String image12;
    private String type;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> file;

    public ArrayList<String> getImages() {

        return images;
    }


    public void setImages(ArrayList<String> images) {
      //  this.images = images;
        this.images= images;
    }

    public ArrayList<String> getFile() {
        return file;
    }

    public void setFile(ArrayList<String> file) {
        this.body = file.get(0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getmediaType() {
        return mediatype;
    }

    public void setmediaType(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getfile_format() {
        return file_format;
    }

    public void setfile_format(String file_format) {
        this.file_format = file_format;
    }

    public int get_is_media() {
        return is_media;
    }

    public void set_is_media(int is_media) {
        this.is_media = is_media;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image12;
    }

    public void setImage(String image) {
        this.image12 = image;
    }


}

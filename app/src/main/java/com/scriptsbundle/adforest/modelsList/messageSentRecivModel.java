package com.scriptsbundle.adforest.modelsList;

public class messageSentRecivModel {

    private String name;
    private String active;
    private String topic;
    private String price;
    private String tumbnail;
    private String id;
    private String sender_id;
    private String receiver_id;
    private String isBlock;
    private String type;
    private boolean isMessageRead;
    private  String messageunread;
    private String posttitle;
    private boolean isblockcondition;

    public void setPosttitle(String posttitle){
        this.posttitle=posttitle;
    }
    public String getposttitle(){
        return posttitle;

    }
    public boolean getisblockcondition(){
        return isblockcondition;
    }
    public void  setIsblockcondition(boolean isblockcondition){
        this.isblockcondition=isblockcondition;

    }


    public  void  setMessageunread(String msgunread){
        this.messageunread= msgunread;
    }
    public String getmsgunread(){
        return messageunread;
    }


    public String getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(String isBlock) {
        this.isBlock = isBlock;
    }
    public boolean isMessageRead() {
        return isMessageRead;
    }

    public void setMessageRead(boolean messageRead) {
        isMessageRead = messageRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTumbnail() {
        return tumbnail;
    }

    public void setTumbnail(String tumbnail) {
        this.tumbnail = tumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.example.h2h;

public class Consignment {
    private String id;
    private String senderAddress;
    private  String time;

    String assignedto;
    private String receiverAddress;
    private String receiverName;

    private String latitude;

    private String longitude;
    private String receiverContact;
    private String pickupDateTime;
    String rider;
    String price;
    String weight;
    private String itemCategory;
    private String itemQuantity;
    private String itemDescription;

    private String url;
    String userid;
    String status;
    Consignment(){

    }

    public Consignment(
            String senderAddress,
            String receiverAddress,
            String receiverName,
            String receiverContact,
            String status,
            String pickupDateTime,
            String time,
            String itemCategory,
            String itemQuantity,
            String itemDescription,
            String id,
            String latitude,
            String longitude,
            String url,
            String rider,
            String price,
            String weight,
            String assignedto,
            String userid
    )

    {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
        this.receiverContact = receiverContact;
        this.pickupDateTime = pickupDateTime;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemDescription = itemDescription;
        this.status=status;
        this.time = time;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.rider = rider;
        this.price = price;
        this.weight = weight;
        this.assignedto = assignedto;
        this.userid=userid;


    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(String assignedto) {
        this.assignedto = assignedto;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setTime(){
        this.time = time;
    }
    public String getTime() {
        return time;
    }
    public String getStatus(){
        return status;
    }


    public void setStatus(String status){
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


    public void setId(String id) {
        this.id = id;
    }

    // Getters and setters for all fields
    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}

package com.smartcampus.model;

public class Equipment {

    private int equipmentId;
    private String equipmentName;
    private String category;
    private int quantity;
    private int availableQuantity;
    private String conditionStatus;

    public Equipment() {
    }

    public Equipment(int equipmentId, String equipmentName,
                     String category, int quantity,
                     int availableQuantity,
                     String conditionStatus) {

        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.category = category;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.conditionStatus = conditionStatus;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(String conditionStatus) {
        this.conditionStatus = conditionStatus;
    }
}
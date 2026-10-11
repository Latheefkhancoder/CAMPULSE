package com.smartcampus.model;

public class Resource {

    private int resourceId;
    private String resourceName;
    private String resourceType;
    private String location;
    private int capacity;
    private String availabilityStatus;

    public Resource() {
    }

    public Resource(int resourceId, String resourceName,
                    String resourceType, String location,
                    int capacity, String availabilityStatus) {

        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.location = location;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
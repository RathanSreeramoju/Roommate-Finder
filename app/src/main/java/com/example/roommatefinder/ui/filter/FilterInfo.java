package com.example.roommatefinder.ui.filter;


public class FilterInfo {
    private String  selectedInputType;
    private String  selectedBedRooms;
    private String  selectedPrice;
    private String  selectedLocation;

    public FilterInfo(){

    }

    public FilterInfo(String selectedInputType, String selectedBedRooms, String selectedPrice, String selectedLocation) {
        this.selectedInputType = selectedInputType;
        this.selectedBedRooms = selectedBedRooms;
        this.selectedPrice = selectedPrice;
        this.selectedLocation = selectedLocation;
    }

    public String getSelectedInputType() {
        return selectedInputType;
    }

    public String getSelectedBedRooms() {
        return selectedBedRooms;
    }

    public String getSelectedPrice() {
        return selectedPrice;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }
}


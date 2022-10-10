package com.example.cityassistant;


public class checkTheme {

    Double lat, lng;
    String nameOfPlace;
    String placeId;
    String imageUri;
    String userwhoPosted;
    String date;
    String typeOfInstitution;

    public checkTheme() {

    }

    public checkTheme(Double lat, Double lng, String nameOfPlace, String typeOfInstitution,
                      String placeId, String imageUri, String userwhoPosted, String date) {
        this.lat = lat;
        this.lng = lng;
        this.nameOfPlace = nameOfPlace;
        this.typeOfInstitution = typeOfInstitution;
        this.placeId = placeId;
        this.imageUri = imageUri;
        this.userwhoPosted = userwhoPosted;
        this.date = date;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public void setNameOfPlace(String nameOfPlace) {
        this.nameOfPlace = nameOfPlace;
    }

    public String getTypeOfInstitution() {
        return typeOfInstitution;
    }

    public void setTypeOfInstitution(String typeOfInstitution) {
        this.typeOfInstitution = typeOfInstitution;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserwhoPosted() {
        return userwhoPosted;
    }

    public void setUserwhoPosted(String userwhoPosted) {
        this.userwhoPosted = userwhoPosted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

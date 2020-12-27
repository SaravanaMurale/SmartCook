package com.clj.blesample.model;

import android.graphics.Bitmap;

public class StoreImageDTO {


    private String imageName;
    private Bitmap image;

    public StoreImageDTO( String imageName, Bitmap image) {
        this.imageName = imageName;
        this.image = image;
    }



    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

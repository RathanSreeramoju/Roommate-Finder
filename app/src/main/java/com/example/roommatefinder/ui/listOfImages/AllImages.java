package com.example.roommatefinder.ui.listOfImages;

import android.net.Uri;

public class AllImages {
    private Uri uri;

    public AllImages(){

    }
    public AllImages(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}

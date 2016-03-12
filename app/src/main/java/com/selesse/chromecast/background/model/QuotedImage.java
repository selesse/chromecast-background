package com.selesse.chromecast.background.model;

import com.google.common.base.MoreObjects;

public class QuotedImage {
    private final String text;
    private final String imgUrl;

    public QuotedImage(String text, String imgUrl) {
        this.text = text;
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("imgUrl", imgUrl)
                .toString();
    }
}

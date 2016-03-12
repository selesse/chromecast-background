package com.selesse.chromecast.background;

import com.google.common.collect.Lists;
import com.selesse.chromecast.background.images.ImageCaptioner;
import com.selesse.chromecast.background.model.QuotedImage;
import com.selesse.chromecast.background.reddit.RedditConnector;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        RedditConnector redditClient = loginToReddit();
        List<Submission> showerThoughts = redditClient.getPosts("ShowerThoughts");
        List<Submission> earthPorn = redditClient.getImgurPosts("EarthPorn");

        List<QuotedImage> quotedImages = Lists.newArrayList();
        for (int i = 0; i < showerThoughts.size(); i++) {
            Submission showerThought = showerThoughts.get(i);
            Submission prettyImage = earthPorn.get(i);

            QuotedImage quotedImage = new QuotedImage(showerThought.getTitle(), prettyImage.getUrl());
            quotedImages.add(quotedImage);
        }

        ImageCaptioner imageCaptioner = new ImageCaptioner();
        for (QuotedImage quotedImage : quotedImages) {
            File captionedImage = imageCaptioner.captionImage(quotedImage);

            LOGGER.info("Captioned image written to {}", captionedImage.getAbsolutePath());
        }
    }

    private static RedditConnector loginToReddit() {
        RedditConnector redditClient = new RedditConnector();
        try {
            redditClient.login();
        } catch (OAuthException e) {
            LOGGER.error("Could not authenticate to Reddit with the client", e);
            throw new RuntimeException(e);
        }
        return redditClient;
    }
}

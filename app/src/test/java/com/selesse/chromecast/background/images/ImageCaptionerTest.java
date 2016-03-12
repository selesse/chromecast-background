package com.selesse.chromecast.background.images;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@Ignore(value = "This is a manual test. How could I verify image transformations? :/")
public class ImageCaptionerTest {
    private static final String SAMPLE_BIG_TEXT =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Phasellus ornare purus ut lorem iaculis, venenatis scelerisque ipsum efficitur. " +
                    "Cras a porta risus. Vivamus non elit ante. Suspendisse et egestas tortor. " +
                    "Aliquam viverra hendrerit diam, in vulputate neque egestas eget. Quisque commodo sit sed.";
    private static final String SAMPLE_MEDIUM_TEXT =
            "This is a medium amount of text to display on an image. It should fit pretty okay.";
    private static final String SAMPLE_SMALL_TEXT =
            "I like turtles.";

    private final File WORKING_DIRECTORY = getLandscapeImageFile().getParentFile();

    @Test
    public void testCanCaptionImage() throws Exception {
        ImageCaptioner imageCaptioner = new ImageCaptioner();
        File landscapeImageFile = getLandscapeImageFile();
        imageCaptioner.captionImage(
                SAMPLE_BIG_TEXT, landscapeImageFile, new File(WORKING_DIRECTORY, "big-landscape.png")
        );
        imageCaptioner.captionImage(
                SAMPLE_MEDIUM_TEXT, landscapeImageFile, new File(WORKING_DIRECTORY, "medium-landscape.png")
        );
        imageCaptioner.captionImage(
                SAMPLE_SMALL_TEXT, landscapeImageFile, new File(WORKING_DIRECTORY, "small-landscape.png")
        );
    }

    @Test
    public void testCanCaptionPortraitImage() throws Exception {
        ImageCaptioner imageCaptioner = new ImageCaptioner();
        File portraitImageFile = getPortraitImageFile();
        imageCaptioner.captionImage(
                SAMPLE_BIG_TEXT, portraitImageFile, new File(WORKING_DIRECTORY, "big-portrait.png")
        );
        imageCaptioner.captionImage(
                SAMPLE_MEDIUM_TEXT, portraitImageFile, new File(WORKING_DIRECTORY, "medium-portrait.png")
        );
        imageCaptioner.captionImage(
                SAMPLE_SMALL_TEXT, portraitImageFile, new File(WORKING_DIRECTORY, "small-portrait.png")
        );
    }

    @SuppressWarnings("ConstantConditions")
    public File getLandscapeImageFile() {
        String inputFilePath = getClass().getClassLoader().getResource("sample-input-landscape.jpg").getFile();
        return new File(inputFilePath);
    }

    @SuppressWarnings("ConstantConditions")
    public File getPortraitImageFile() {
        String inputFilePath = getClass().getClassLoader().getResource("sample-input-portrait.jpg").getFile();
        return new File(inputFilePath);
    }
}
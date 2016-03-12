package com.selesse.chromecast.background.images;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class ImageCaptioner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCaptioner.class);

    public void captionImage(String caption, File imagePath, File outputFile) throws IOException, FontFormatException {
        BufferedImage image = ImageIO.read(imagePath);

        Graphics graphics = image.getGraphics();
        Graphics2D graphics2d = (Graphics2D) graphics;

        int width = image.getWidth();
        int height = image.getHeight();
        float fontSize = height / 30f;
        LOGGER.info("Image was {} x {}, setting font size to {}", width, height, fontSize);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        Font robotoFont = createRobotoFont(fontSize);

        AttributedString attributedString = new AttributedString(caption, robotoFont.getAttributes());
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext fontRenderContext = graphics2d.getFontRenderContext();
        LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(paragraph, fontRenderContext);
        lineBreakMeasurer.setPosition(paragraphStart);

        float percentageWidthOccupiedByText = 0.80f;
        float breakWidth = width * percentageWidthOccupiedByText;
        // The 1.5f is completely arbitrary, but it looks okay.
        float estimatedLinesOfText = caption.length() / ((width / fontSize) * 1.5f);
        // For every estimated line of text, shift the text center up by a bit.
        // The 8000 factor is arbitrary, but looks OK.
        float drawPosY = height * 0.5f - (height * estimatedLinesOfText * (fontSize / 8000f));

        while (lineBreakMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineBreakMeasurer.nextLayout(breakWidth);
            float drawPosX = width * ((1f - percentageWidthOccupiedByText) / 2f);
            drawPosY += layout.getAscent();

            graphics2d.setColor(Color.black);
            int shadowSize = 2;
            layout.draw(graphics2d, drawPosX + shadowSize, drawPosY + shadowSize);
            layout.draw(graphics2d, drawPosX - shadowSize, drawPosY + shadowSize);
            layout.draw(graphics2d, drawPosX + shadowSize, drawPosY - shadowSize);
            layout.draw(graphics2d, drawPosX - shadowSize, drawPosY - shadowSize);
            graphics2d.setColor(Color.white);
            layout.draw(graphics2d, drawPosX, drawPosY);

            drawPosY += layout.getDescent() + layout.getLeading();
        }

        ImageIO.write(image, "png", outputFile);
    }

    private Font createRobotoFont(float fontSize) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.PLAIN, new File(Resources.getResource("Roboto-Regular.ttf").getFile()));
        return font.deriveFont(fontSize);
    }
}

package xyz.Dot.ui;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import xyz.Dot.Client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CFont {

    private int fontHeight;

    public DynamicTexture texture;
    public BufferedImage bufTexture;
    public final HashMap<Character, FontData> charMap = new HashMap<>();
    public boolean loadOK = false;
    private static final Logger logger = LogManager.getLogger();

    public CFont(Font font, int charSize) {
        new Thread(()-> cfontthread(font,charSize)).start();
    }

    public void cfontthread(Font font, int charSize){
        logger.info("[Dot] " + font +  "启动!");
        long starttime;
        float time;
        starttime = System.nanoTime();

        for (int i = 0; i < charSize; i++)
            charMap.put((char) i, new FontData());
        this.fontHeight = -1;

        int imgSize;
        if (font.getFamily().equals("MiSans")) {
            if (font.getSize() == 12) {
                imgSize = 4500;
            } else if (font.getSize() == 16) {
                imgSize = 5500;
            } else if (font.getSize() == 20) {
                imgSize = 6500;
            } else if (font.getSize() == 36) {
                imgSize = 11000;
            } else {
                imgSize = 8196;
            }

        } else {
            imgSize = 8196;
        }

        this.bufTexture = getFontTexture(font, charSize >= 65535 ? imgSize : 256);

        loadOK = true;

        time = ((System.nanoTime() - starttime) / 1000000f);
        logger.info("[Dot] LoadOK font " + font + time + "ms");
    }

    public void drawChar(FontData data, float x, float y) {
        try {
            drawQuad(x, y, data.width, data.height, data.storedX, data.storedY, bufTexture.getWidth(), data.width, data.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStringHeight(String str) {
        return (fontHeight - 8) / 2;
    }

    public int getStringHeight() {
        return (fontHeight - 8) / 2;
    }

    public int getStringWidth(String text) {
        int width = 0;
        for (char c : text.toCharArray()) if (c < charMap.size()) width += charMap.get(c).width - 8;
        return width / 2;
    }

    private BufferedImage getFontTexture(Font font, int imgSize) {
        final BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);

        final Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setFont(font);

        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);

        g.setColor(Color.WHITE);

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final FontMetrics fontMetrics = g.getFontMetrics();

        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;

        for (Map.Entry<Character, FontData> data : charMap.entrySet()) {

            final Character c = data.getKey();
            final FontData charData = data.getValue();

            final Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(c), g);

            charData.width = (dimensions.getBounds().width + 8);
            charData.height = dimensions.getBounds().height;

            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }

            if (charData.height > charHeight)
                charHeight = charData.height;

            charData.storedX = positionX;
            charData.storedY = positionY;

            if (charData.height > fontHeight)
                fontHeight = charData.height;

            g.drawString(String.valueOf(c), positionX + 2, positionY + fontMetrics.getAscent());

            positionX += charData.width;
        }
        return bufferedImage;
    }

    public void drawQuad(float x, float y, float width, float height, float srcX, float srcY, int imgSize, float srcWidth, float srcHeight) {
        float renderSRCX = srcX / imgSize;
        float renderSRCY = srcY / imgSize;
        float renderSRCWidth = srcWidth / imgSize;
        float renderSRCHeight = srcHeight / imgSize;
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x + width, y + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);
        GL11.glEnd();
    }
}

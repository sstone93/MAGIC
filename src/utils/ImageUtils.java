package utils;

//ImageUtils.java

/**
 * @author https://flyingdogz.wordpress.com/2008/02/11/image-rotate-in-java-2-easier-to-use/
 */
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageUtils {

 public static BufferedImage tilt(BufferedImage image, double angle) {
     double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
     int w = image.getWidth(), h = image.getHeight();
     int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
     GraphicsConfiguration gc = getDefaultConfiguration();
     BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
     Graphics2D g = result.createGraphics();
     g.translate((neww-w)/2, (newh-h)/2);
     g.rotate(angle, w/2, h/2);
     g.drawRenderedImage(image, null);
     g.dispose();
     return result;
 }

 public static GraphicsConfiguration getDefaultConfiguration() {
     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     GraphicsDevice gd = ge.getDefaultScreenDevice();
     return gd.getDefaultConfiguration();
 }
}
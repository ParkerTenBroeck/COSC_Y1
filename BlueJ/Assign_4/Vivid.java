import Media.*;
import java.awt.Color;
/**
 * @author Parker TenBroeck 7376726
 * Assignment 4
 */
public class Vivid
{
    public Vivid()
    {
        PictureDisplayer d = new PictureDisplayer();
        Picture pic = new Picture();
        d.placePicture(pic);
        d.waitForUser();
        Vivid.vividPicturePalette(pic);
    }
    
    /**
     * Make the provided Picture vivid
     * 
     * @param pic   the picture to make vivid
     */
    public static void vividPicturePalette(Picture pic) {
        // for ever pixel in the image
        for(Pixel pixel = pic.next(); pic.hasNext(); pixel = pic.next()){
            // get the pixels current color
            Color pixelColor = pixel.getColor();
            // transform the color according to the PDF
            pixelColor = Vivid.vividPalette(pixelColor);
            // set pixel to new color
            pixel.setColor(pixelColor);
        }
    }
    
    /**
     *  Reduce the palette of a color
     *
     * @param  original  the color to preform palette reduction on
     * @return   a new color that is more 'vivid'
     */
    public static Color vividPalette(Color original) {
        // get pixels r,g,b componenets
        int r = original.getRed();
        int g = original.getGreen();
        int b = original.getBlue();
        // if either g or b is greater than r make r 0
        if (r < g || r < b){
            r = 0;
        }
        // if either r or b is greater than g make g 0
        if (g < r || g < b){
            g = 0;
        }
        // if either r or g is greater than b make b 0
        if (b < r || b < g){
            b = 0;
        }
        // return a new color with the new values
        return new Color(r, g, b);
    }
}

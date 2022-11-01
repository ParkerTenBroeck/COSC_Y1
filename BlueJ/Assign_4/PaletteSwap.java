import Media.*;
import java.awt.Color;
/**
 * @author Parker TenBroeck 7376726
 * Assignment 4
 */
public class PaletteSwap
{

    public PaletteSwap()
    {
        PictureDisplayer d = new PictureDisplayer();
        Picture pic = new Picture();
        d.placePicture(pic);
        d.waitForUser();
        PaletteSwap.recudePicturePalette(pic);
    }
    
    /**
     * Reduce the pallete of a whole image
     * 
     * @param pic   the picture to preform palette reduction on
     */
    public static void recudePicturePalette(Picture pic) {
        // for ever pixel in the image
        for(Pixel pixel = pic.next(); pic.hasNext(); pixel = pic.next()){
            // get the pixels current color
            Color pixelColor = pixel.getColor();
            // transform the color according to the PDF
            pixelColor = PaletteSwap.reducePalette(pixelColor);
            // set pixel to new color
            pixel.setColor(pixelColor);        
        }
    }
    
    /**
     *  Reduce the palette of a color
     *
     * @param  original  the color to preform palette reduction on
     * @return   a new color that has a recuded palette
     */
    public static Color reducePalette(Color original) {
        int[] palette = new int[]{0,85,170,255};
        return new Color(
            palette[original.getRed() / 64],
            palette[original.getGreen() / 64],
            palette[original.getBlue() / 64]
            );
    }
}

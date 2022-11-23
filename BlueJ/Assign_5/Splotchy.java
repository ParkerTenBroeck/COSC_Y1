import Media.*;
import java.awt.Color;

/**
 * @author Parker TenBroeck 7376726
 * Assignment 5
 */
public class Splotchy
{
    public Splotchy()
    {
        // create a new picture displayer and picture
        PictureDisplayer d = new PictureDisplayer();
        Picture pic = new Picture();

        d.placePicture(pic);
        d.waitForUser();
        // splotcherize the picture with 100_000 splotches
        Picture newPic = Splotchy.splotcherize(pic, 100_000);
        d.placePicture(newPic);
        
        d.waitForUser();
        // do the blending acording to the prof
        //Splotchy.blending(newPic);
        
        // this is the way I did it before seeing the profs email
        // blend the picture with a blending radius of three and show the new picture
        d.placePicture(Splotchy.blending2(newPic, 3));
    }

    /**
     * Make a new image thats a splotchy version of pic
     *
     * @param pic        the picture to base the new picture off
     * @param splotches  the number of splotches to make
     * @return           the new splotchy picture
     */
    public static Picture splotcherize(Picture pic, int splotches){
        // height and width of given pic
        int width = pic.getWidth();
        int height = pic.getHeight();
        
        // create a new pic with the same size
        Picture newPic = new Picture(width, height);
    
        // loop for #splotches times
        for(int i = 0; i < splotches; i ++){
            // random x and y coord within the image bounds
            int x = (int)(width * Math.random()); 
            int y = (int)(height * Math.random()); 
            
            // get the color at the sampled coord
            Color sample = pic.getPixel(x,y).getColor();
            // draw the splotch on the new picture with the sampled color
            Splotchy.drawSplotch(newPic, sample, x,y);
        }
        return newPic;
    }
    
    /**
     * Draw a splotch on target with color at x,y
     * 
     * Note: the splotch is not drawn starting from any corner but
     * instead it is drawn such that x and y are inside the splotch and
     * all of the splotch is visible
     * 
     * @param target   the target image to draw the splotch in
     * @param color    the color to make the splotch
     * @param x        the x coord where the splotch should -kinda- be
     * @param y        the y coord where the splotch should -kinda- be
     */
    public static void drawSplotch(Picture target, Color color,int x, int y){
        // height and width of target image
        int width = target.getWidth();
        int height = target.getHeight();
        
        // height and width of the splotch
        int splotchWidth = 7;
        int splotchHeight = 7;
        
        // where the splotch starts (the top left corner of the square)
        
        // xs will always be between zero and width - splotchWidth
        int xs = x - (splotchWidth * x - 3) / (width-1);
        // ys will always be between zero and height - splotchHeight
        int ys = y - (splotchHeight * y - 3) / (height-1);
        
        // loop through ys to ys + splotchHeight
        // this will each y coord in this splotchs height
        for(int j = ys; j < ys + splotchHeight; j++){
            
            // loop through xs to xs + splotchWidth
            // this will each x coord in this splotchs width
            for(int i = xs; i < xs + splotchWidth; i++){
                // get the pixel at i and j and set its color to the new splotch color
                target.getPixel(i,j).setColor(color);
            }   
        }
    }
    
    /**
     * Preform a blur effect on pic 
     * This methods preforms the bluring algorithm specified by the professor
     * 
     * @param pic      the picture to preform blending on
     * @return         a new picture made from blurring pic
     */
    public static void blending(Picture pic) {
        // height and width of given pic
        int width = pic.getWidth();
        int height = pic.getHeight();
        
        
        // for all the y coords in the image
        for(int j = 0; j < height; j ++){
            // for all the x coords in the image
            for(int i = 0; i < width; i++){
                // find the average pixel at i,j with a given radius in the old pic
                Splotchy.blendAt(pic, i, j);
            }   
        }
    }
    
    /**
     * Preforms blending on the pixel in a 7x7 box around the anchor point
     * Pixels out of bounds will not be drawn to 
     * 
     * @param pic   the picture to preform the pixel blend on
     * @param x     the x coord of the anchor pixel
     * @param y     the y coord of the anchor pixel
     */
    public static void blendAt(Picture pic, int x, int y){
        // get the width and height of the picture;
        int height = pic.getHeight();
        int width = pic.getWidth();
        
        // get the color at the anchor point
        Color anchor = pic.getPixel(x, y).getColor();
        
        // for each x offset in the radius
        for(int i = -6; i <= 0; i ++){
        // if x+i is out of the pictures bounds continue to next x coord
            if ((x+i < 0) || (x+i>=width)){
                continue;   
            }
            
            // for each y offset in the radius
            for(int j = -6; j <= 0; j ++){
                // if y+j is out of the pictures bounds continue to the next y coord
                if ((j+y<0) || (j+y>=height)) {
                    continue;
                }
                // get the pixel at some coord in the radius
                Pixel pixel = pic.getPixel(i+x, j+y);
                // get its color
                Color c = pixel.getColor();
                // average its red,green,blue components with the anchor color
                int red = (anchor.getRed() + c.getRed()) / 2;
                int green = (anchor.getGreen() + c.getRed()) / 2;
                int blue = (anchor.getBlue() + c.getRed()) / 2;
                // set the pixel to this new color
                pixel.setColor(new Color(red, green, blue));
                
            }
        }
    }
    
    /**
     * Preform a blur effect on pic 
     * bluring with radius 
     * This method preforms a blur that takes into account the 
     * non linear color space
     * 
     * @param pic      the picture to preform blending on
     * @param radius   the radius of the blending
     * @return         a new picture made from blurring pic
     */
    public static Picture blending2(Picture pic, int radius){
        // height and width of given pic
        int width = pic.getWidth();
        int height = pic.getHeight();
        
        // create a new pic with the same size
        Picture newPic = new Picture(width, height);
        
        // for all the y coords in the image
        for(int j = 0; j < height; j ++){
            // for all the x coords in the image
            for(int i = 0; i < width; i++){
                 // find the average pixel at i,j with a given radius in the old pic
                Color average = Splotchy.calculateColorAverageWithinRadius(pic, i, j, radius);
                // set the pixel at i,j to the calculated average color
                newPic.getPixel(i,j).setColor(average);
                
            }   
        }
        
        return newPic;
    }
    
    /**
     * This method will calculate the average color at x and y within a given radius
     * in the picture
     * 
     * @param pic    the picture 
     * @param x      the x coord of the center average point
     * @param y      the y coord of the center average point
     * @param radius the radius around the center point to calculate the average color
     * @return       the average color calculated
     */
    public static Color calculateColorAverageWithinRadius(Picture pic, int x, int y, int radius){
        // get the width and height of the picture;
        int height = pic.getHeight();
        int width = pic.getWidth();
        
        int num = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        
        // for each x offset in the radius
        for(int i = -radius; i <= radius; i ++){
            // if x+i is out of the pictures bounds continue to next x coord
            if ((x+i < 0) || (x+i>=width)){
                continue;   
            }
            
            // for each y offset in the radius
            for(int j = -radius; j <= radius; j ++){
                // if y+j is out of the pictures bounds continue to the next y coord
                if ((j+y<0) || (j+y>=height)) {
                    continue;
                }
                //|| (j * j > radius*radius)
                //|| (i * i > radius*radius)
                
                // get the pixel at i+x,j+y
                Color c = pic.getPixel(i+x,j+y).getColor();
                
                // add the square of each color component to a rolling sum
                // we square the values because each color component is stored in a non linear format
                red += c.getRed() * c.getRed();
                green += c.getGreen() * c.getGreen();
                blue += c.getBlue() * c.getBlue();
                
                // increment the average count number 
                num ++;
            }
        }
        // divide each component by the number of componented sumed together to find the average
        // then take the square root to find the compressed representation again
        red = (int)Math.sqrt(red / (double)num);
        green = (int)Math.sqrt(green / (double)num);
        blue = (int)Math.sqrt(blue / (double)num);
        
        // create a new color with red,green,blue componenets
        Color average = new Color(red, green, blue);
        return average;
    }
}

import Media.*;
/**
 * @author Parker TenBroeck 7376726
 */
public class Garden
{
    Turtle turtle;
    TurtleDisplayer display;

    public Garden()
    {
        this.turtle = new Turtle();
        this.display = new TurtleDisplayer(this.turtle);
        this.turtle.setSpeed(0);
        drawGarden();
    }
    
    public void drawPetal() {
        this.turtle.penDown();
        this.turtle.left(Math.PI);
        this.turtle.left(Math.PI / 4.0);
        this.turtle.forward(Math.sqrt(15*15*2));
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(30.0);
        this.turtle.left(Math.PI + 2.0 * Math.PI / 6.0);
        this.turtle.forward(30.0);
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(Math.sqrt(15*15*2));
        this.turtle.left(Math.PI / 4.0);
        this.turtle.penUp();
    }
    
    public void drawFlower() {
        int petalNumber = (int)(Math.random() * 4) + 4;
        
        double angle = Math.PI * 2.0 / petalNumber;
        for (int i = 0; i < petalNumber; i ++){
            this.drawPetal();
            this.turtle.left(angle);
        }
    }
    
    public void drawGarden(){

        double height = 300;
        double width = 300;
        double rad = Math.sin(Math.PI / 3.0) * 30 + 15;
        
        for (int x = 0; x < 4; x ++){
            for (int y = 0; y < 4; y ++){
                double mx = x * ((width - rad * 2) / 3.0) + rad - width / 2.0;
                double my = y * ((height - rad * 2) / 3.0) + rad - height / 2.0;
                this.turtle.moveTo(mx, my);
                this.drawFlower();
            }
        }
    }
}

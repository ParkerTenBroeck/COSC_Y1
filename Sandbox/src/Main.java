import BasicIO.ASCIIDataFile;
import BasicIO.ASCIIDisplayer;
import BasicIO.BasicForm;
import BasicIO.GeneralCanvas;
import Media.Picture;
import Media.Turtle;
import Media.TurtleDisplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception{
        Turtle yertle = new Turtle();
        TurtleDisplayer display = new TurtleDisplayer(yertle, 720, 480);

        yertle.penDown();
        yertle.right(Math.PI/2);
        yertle.forward(100);
        yertle.left(Math.PI);
        yertle.forward(50);
        yertle.right(Math.PI/2);
        yertle.forward(50);
        yertle.right(Math.PI/2);
        yertle.forward(50);
        yertle.left(Math.PI/2);
        yertle.penUp();
        yertle.forward(50);
        yertle.left(Math.PI/2);
        yertle.penDown();
        yertle.forward(50);
        yertle.penUp();
        yertle.forward(25);
        yertle.penDown();
        yertle.forward(25);


        Thread.sleep(1000);



        BasicForm basicForm;
        GeneralCanvas canvas;
        BufferedImage image;
        Picture colorPicture;
        JFrame frame;
        // all this below is reflection madness don't look at it for too long, or you'll lose your mind >:)
        {
            {
                {
                    Field basicFormField = TurtleDisplayer.class.getDeclaredField("bf");
                    basicFormField.setAccessible(true);
                    basicForm = (BasicForm) basicFormField.get(display);
                }
                {
                    Field pcolorField = TurtleDisplayer.class.getDeclaredField("pcolor");
                    pcolorField.setAccessible(true);
                    colorPicture = (Picture) pcolorField.get(display);
                }
                {
                    Field anglepanelField = Turtle.class.getDeclaredField("canvas");
                    anglepanelField.setAccessible(true);
                    canvas = (GeneralCanvas) anglepanelField.get(yertle);
                }
                {
                    Field imageField = GeneralCanvas.class.getDeclaredField("img");
                    imageField.setAccessible(true);
                    image = (BufferedImage) imageField.get(canvas);
                }
            }
            Object form;

            {
                Field test = basicForm.getClass().getDeclaredField("form");
                test.setAccessible(true);
                form = test.get(basicForm);
            }

            {
                Field test = form.getClass().getDeclaredField("frame");
                test.setAccessible(true);
                frame = (JFrame) test.get(form);
            }
        }

        SomethingStupid stupid = new SomethingStupid();


        Graphics g = image.getGraphics();


        Thread t = new Thread(() -> {
            Random rand = new Random(System.currentTimeMillis());
            yertle.setSpeed(0);
            int loops = 0;
            while (!stupid.theStupidThing){
                for(int i = 0; i < rand.nextInt(7) + 1; i ++){
                    g.setColor(new Color(rand.nextInt()));
                    int y = rand.nextInt(image.getHeight());
                    int x = rand.nextInt(image.getWidth());

                    int h = rand.nextInt(20) + 10;
                    int w = rand.nextInt(20) + 10;
                    g.fillRect(x,y,w,h);
                }
                yertle.right(0.07);
                Color cycleColor = Color.getHSBColor((loops / 100.0f) % 1.0f, 1.0f, 1.0f);
                for (int y = 0; y < colorPicture.getHeight(); y++)
                {
                    for (int x = 0; x < colorPicture.getWidth(); x++)
                    {
                        colorPicture.getPixel(x,y).setColor(cycleColor);
                    }
                }

                crankySleep(20);
                loops += 1;
            }
        });
        t.start();

        basicForm.setTitle("My Window now :)");

        JRootPane root_pane = (JRootPane) frame.getComponent(0);

        KeyRememberer test = new KeyRememberer();
        frame.addKeyListener(test);

        //14, 19, 20
        Random ran = new Random(27);
        Thread.sleep(400);
        recursive_removal_fun(frame, root_pane, 200, ran);

        root_pane.removeAll();
        root_pane.add(canvas);
        Dimension size = new Dimension(image.getWidth(), image.getHeight() + frame.getInsets().top);
        frame.setMinimumSize(size);
        frame.setMaximumSize(size);
        frame.setSize(size);

        stupid.theStupidThing = true;
        t.join();
        g.setColor(Color.WHITE);
        g.fillRect(0,0, image.getWidth(), image.getHeight());

        root_pane.setDoubleBuffered(true);
        frame.pack();
        frame.repaint();
        frame.requestFocus();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        frame.setTitle("");
        String fullTitle = "Asteroids V1.1";
        for (char c: fullTitle.toCharArray()){
            frame.setTitle(frame.getTitle() + c);
            crankySleep(300);
        }

        {

            int tmp1 = image.getWidth() / 9;
            int[] tmp = new int[image.getHeight()];
            for(int i = 0; i < tmp.length; i ++){
                tmp[i] = ran.nextInt(tmp1);
            }
            g.setColor(Color.BLACK);

            for(int i = 0; i < image.getWidth() / 2 + tmp1; i ++){
                for(int y = 0; y < image.getHeight(); y ++){
                    int x1 = i - tmp[y];
                    int x2 = image.getWidth() - i - tmp[y] + tmp1;
                    if (x1 >= 0 && x1 < image.getWidth()){
                        g.drawLine(x1, y, x1, y);
                    }
                    if (x2 >= 0 && x2 < image.getWidth()){
                        g.drawLine(x2, y, x2, y);
                    }
                }
                frame.repaint();
                crankySleep(10);
            }
        }


        runVm(image, frame, test);
    }

    // my ide doesn't like sleep in loops (who would have guessed)
    // maybe that's why its so cranky
    public static void crankySleep(long millis){
        try{
            Thread.sleep(millis);
        }catch (Exception ignore){

        }
    }

    public static void runVm(BufferedImage image, JFrame rootFrame, KeyRememberer keys) throws Exception{
        VirtualMachine vm = new VirtualMachine(); //create vm
        vm.memory = new int[2 << 25]; //allocate memory (256 MiB)
        vm.v_interface = new BrockVirtualInterface(image, rootFrame, keys); //construct interface (syscalls)

        //this program executes 6,442,254,338 instructions and can be used to bench the VM
        //00: lui $2, 0x7FFF
        //04: add $1, $0, $0
        //08: addi $1, $1, 1
        //0C: beq $2, $1, 0x14
        //10: j 0x8
        //14: syscall 0
        //byte[] bytes = new byte[] {0x3c,0x02,0x7F, (byte)0xFF, 0x00, 0x00, 0x08, 0x20, 0x20, 0x21, 0x00, 0x01, 0x10, 0x22, 0x00, 0x01, 0x08, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x0C};

        //load binary into memory
        byte[] bytes = Files.readAllBytes(new File("../Test1_rust/mips/bin/tmp.bin").toPath());
        for(int i = 0; i < bytes.length; i ++){
            vm.setByte(i, bytes[i]);
        }
        while (true){
            long startTime = System.nanoTime();
            vm.run();
            long endTime = System.nanoTime();
            System.out.printf("VM Execution time: %.8fs%n", (endTime - startTime) / 1000000000.0);

            while (true){
                if (keys.isKeyCharPressed('r')){
                    vm.reset();
                    for(int i = 0; i < bytes.length; i ++){
                        vm.setByte(i, bytes[i]);
                    }
                    for(int i = (bytes.length + 3) / 4; i < vm.memory.length; i ++){
                        vm.memory[i] = 0;
                    }
                    break;
                }else if (keys.isKeyCharPressed('e')){
                    System.out.println("Exiting");
                    return;
                }
                crankySleep(1);
            }

        }

    }

    // OooOOoOoOO hacker
    private static void recursive_removal_fun(JFrame main, JComponent root_component, int sleep, Random rand){

        Component[] components = root_component.getComponents();

        for (int i = 0; i < components.length; ++i) {
            int index = rand.nextInt(components.length - i);
            Component tmp = components[components.length - 1 - i];
            components[components.length - 1 - i] = components[index];
            components[index] = tmp;
        }

        for(Component c: components){
            if (c instanceof JComponent){
                JComponent jc = (JComponent) c;
                if (jc.getComponentCount() < 1){
                    root_component.remove(jc);
                }else{
                    recursive_removal_fun(main, jc, sleep, rand);
                    root_component.remove(jc);
                }

                main.repaint();
            }else{
                root_component.remove(c);
            }
            try{
                Thread.sleep(sleep);
            }catch (Exception ignore){

            }
        }

    }

    //Not too familiar with javas thread safety / locks n stuff, so I'm cheating the sake of laziness
    private static class SomethingStupid{
        //For some reason in the land of OO and memory unsafety the one thing that java says no to is
        //shared mutable primitives
        boolean theStupidThing = false;
    }

    public static class KeyRememberer /* great name if I do say so */ implements KeyListener{

        private final ArrayList<Character> pressed = new ArrayList<>();

        public boolean isKeyCharPressed(char key){
            return pressed.contains(key);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            pressed.remove((Character) e.getKeyChar()); // just to be sure that we are unique + im lazy + ratio
            pressed.add(e.getKeyChar());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressed.remove((Character) e.getKeyChar());
        }
    }
}
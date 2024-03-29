import Media.Turtle;
import Media.TurtleDisplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.Iterator;
import java.util.Random;

public class BrockVirtualInterface implements VirtualMachine.VirtualInterface {

    // Id of zero indicates null
    int next_id = 1;
    HashMap<Integer, Object> objMap = new HashMap<>();


    long lastSlept;
    Random rand = new Random(System.currentTimeMillis());

    JFrame rootFrame;
    BufferedImage image;
    boolean processing = false;
    Main.KeyRememberer keys;

    public BrockVirtualInterface(BufferedImage image, JFrame rootFrame, Main.KeyRememberer keys){
        this.image = image;
        this.keys = keys;
        this.rootFrame = rootFrame;
    }

    private int insert_object(Object obj){
        if (this.next_id == 0){
            this.next_id++;
        }
        Object old_o = this.objMap.put(this.next_id, obj);
        while (old_o != null) {
            this.objMap.put(this.next_id, old_o);
            this.next_id += 1;
            old_o = this.objMap.put(this.next_id, obj);
        }

        return next_id++;
    }

    private Object remove_object(int id){
        return objMap.remove(id);
    }

    private Object get_object(int id){
        return objMap.get(id);
    }

    @Override
    public void system_call(VirtualMachine emu, int call_id) {
        switch (call_id){
            case 0:
                emu.running = false;
                break;
            case 1:
                System.out.print(emu.registers[4]);
                break;
            case 2:
                try{
                    emu.reset();
                    for(int i = 0; i < emu.memory.length; i ++){
                        emu.memory[i] = 0;
                    }
                    byte[] bytes = Files.readAllBytes(new File("tmp.bin").toPath());
                    for(int i = 0; i < bytes.length; i ++){
                        emu.setByte(i, bytes[i]);
                    }
                }catch (Exception ignore){}
                break;
            case 4:
                int address = emu.registers[4];
                for(int i = 0; i < 500; i ++){
                    byte b = emu.getByte(address);
                    if (b == 0){
                        break;
                    }
                    System.out.print((char)b);
                    address += 1;
                }
                break;
            case 5:
                System.out.print((char)emu.registers[4]);
                break;
            case 50:
                try{
                    Thread.sleep(emu.registers[4]);
                }catch (Exception ignore){

                }
                break;
            case 51:
                try{
                    long now = System.nanoTime();
                    long tmp = (now - this.lastSlept);
                    long sleep = (((long)emu.registers[4]) * 1000000L) - tmp;
                    if (sleep < 0){
                        sleep = 0;
                    }
                    Thread.sleep(sleep / 1000000,(int)(sleep % 1000000));
                    this.lastSlept = System.nanoTime();
                }catch (Exception ignore){

                }
                break;

            case 60:
                {
                    long now = System.nanoTime();
                    emu.registers[3] = (int)(now >> 32);
                    emu.registers[2] = (int)now;
                }
                break;
            case 99:
                emu.registers[2] = rand.nextInt(1 + emu.registers[5] - emu.registers[4]) + emu.registers[4];
                break;

            // JVM things
            case 100:
                {
                    //free object
                    this.remove_object(emu.registers[4]);
                }
                break;
            case 101:
                {
                    Object o = this.get_object(emu.registers[4]);
                    Class clazz = o.getClass();
                    int id = this.insert_object(clazz);
                    emu.registers[2] = id;
                }
                break;
            case 102:
                {
                    Class clazz = this.get_object(emu.registers[4]).getClass();
                    int id = this.insert_object(clazz);
                    emu.registers[2] = id;
                }
                break;
            case 103:
                {
                    Class clazz = (Class)this.get_object(emu.registers[4]);
                    String name = clazz.getName();
                    int id = this.insert_object(name);
                    emu.registers[2] = id;
                    emu.registers[3] = name.length();
                }
                break;

            case 104:
                {
                    String string = (String)this.get_object(emu.registers[4]);
                    int start = emu.registers[5];
                    int end = start + emu.registers[6];
                    emu.registers[2] = 0;
                    for (byte b: string.getBytes()){
                        emu.setByte(start, b);
                        emu.registers[2] += 1;
                        start += 1;
                        if (start == end) {
                            break;
                        }
                    }

                }
                break;

            case 105:
                {
                    Object o = this.get_object(emu.registers[4]);
                    String name = o.toString();
                    int id = this.insert_object(name);
                    emu.registers[2] = id;
                    emu.registers[3] = name.length();
                }
                break;

            case 106:
                {
                    String string = (String)this.get_object(emu.registers[4]);
                    emu.registers[2] = string.length();
                }
                break;

            case 107:
                {
                    Object[] arr = (Object[])this.get_object(emu.registers[4]);
                    emu.registers[2] = arr.length;
                }
            break;

            case 108:
                {
                    Object[] arr = (Object[])this.remove_object(emu.registers[4]);
                    int start = emu.registers[5];
                    int end = start + (4 * emu.registers[6]);
                    emu.registers[2] = 0;
                    for(Object o: arr){
                        emu.setWord(start, this.insert_object(o));
                        if (start >= end) {
                            break;
                        }
                        emu.registers[2] += 1;
                        start += 4;
                    }
                }
                break;

            case 109:
                {
                    Class clazz = (Class)this.get_object(emu.registers[4]);
                    Field[] fields = clazz.getDeclaredFields();
                    int id = this.insert_object(fields);
                    emu.registers[2] = id;
                    emu.registers[3] = fields.length;
                }
                break;


            case 110:
            {
                Field field = (Field)this.get_object(emu.registers[4]);

//                emu.registers[2] = id;
//                emu.registers[3] = fields.length;
            }
            break;

            // Turtle specific things
            case 200:
                {
                    Turtle turtle = new Turtle();
                    int id = this.insert_object(turtle);
                    emu.registers[2] = id;
                }
                break;
            case 201:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    turtle.setSpeed(emu.registers[5]);
                }
                break;
            case 202:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    turtle.penDown();
                }
                break;
            case 203:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    turtle.penUp();
                }
                break;
            case 204:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    long l = (((long)emu.registers[5]) & 0xFFFFFFFFL) | (((long)emu.registers[6]) << 32);
                    double f = java.lang.Double.longBitsToDouble(l);
                    turtle.forward(f);
                }
                break;

            case 205:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    long l = (((long)emu.registers[5]) & 0xFFFFFFFFL) | (((long)emu.registers[6]) << 32);
                    double f = java.lang.Double.longBitsToDouble(l);
                    turtle.left(f);
                }
                break;

            case 206:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    long l = (((long)emu.registers[5]) & 0xFFFFFFFFL) | (((long)emu.registers[6]) << 32);
                    double f = java.lang.Double.longBitsToDouble(l);
                    turtle.right(f);
                }
                break;

            case 207:
            {
                Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                //turtle.
            }
            break;

            // TurtleDisplayer things
            case 300:
                {
                    Turtle turtle = (Turtle)this.get_object(emu.registers[4]);
                    TurtleDisplayer display = new TurtleDisplayer(turtle);
                    int id = this.insert_object(display);
                    emu.registers[2] = id;
                 }
                break;
            case 301:
                {
                    TurtleDisplayer display = (TurtleDisplayer)this.get_object(emu.registers[4]);
                    display.close();
                }
                break;

            case 400:
                this.processDrawCall(emu);
                break;
            case 401:
                emu.registers[2] = this.keys.isKeyCharPressed((char)emu.registers[4]) ? 1 : 0;
                break;
            case 402:
                emu.registers[2] = this.image.getWidth();
                emu.registers[3] = this.image.getHeight();
                break;
            default:
                throw new RuntimeException("Invalid System Call: " + call_id);
        }
    }

    private void processDrawCall(VirtualMachine emu) {
        ArrayList<Byte> data = new ArrayList(emu.registers[5]);
        for (int i = 0; i < emu.registers[5]; i ++){
            data.add(emu.getByte(i + emu.registers[4]));
        }

        while(this.processing){
            try{
                Thread.sleep(1);
            }catch (Exception ignore){

            }
        }

        Thread t = new Thread(() -> {
            this.processing = true;
            Graphics g = this.image.getGraphics();
            Iterator<Byte> iter = data.iterator();

            while (iter.hasNext()){
                switch (iter.next()){
                    case 1:
                    {
                        int x = shotFromByteIter(iter);
                        int y = shotFromByteIter(iter);
                        g.drawLine(x,y,x,y);
                    }
                    break;
                    case 2:
                        {
                            int x1 = shotFromByteIter(iter);
                            int y1 = shotFromByteIter(iter);
                            int x2 = shotFromByteIter(iter);
                            int y2 = shotFromByteIter(iter);
                            g.drawPolyline(new int[]{x1,x2,x2}, new int[]{y1,y2,y2}, 3);

                        }
                        break;
                    case 3:
                        g.fillRect(0,0, image.getWidth(), image.getHeight());
                        break;
                    case 4:
                        {
                            int x = shotFromByteIter(iter);
                            int y = shotFromByteIter(iter);
                            int len = shotFromByteIter(iter);
                            byte[] str = new byte[len];
                            for(int i = 0; i < str.length; i ++){
                                str[i] = iter.next();
                            }
                            g.drawBytes(str, 0, len, x, y);
                        }
                        break;
                    case 5:
                        g.drawRect(shotFromByteIter(iter), shotFromByteIter(iter), shotFromByteIter(iter), shotFromByteIter(iter));
                        break;
                    case 6:
                        g.fillRect(shotFromByteIter(iter), shotFromByteIter(iter), shotFromByteIter(iter), shotFromByteIter(iter));
                        break;
                    case 7:
                        g.setColor(new Color(
                                unsignedByteFromIter(iter),
                                unsignedByteFromIter(iter),
                                unsignedByteFromIter(iter),
                                unsignedByteFromIter(iter)
                        ));
                        break;
                    case 8:
                        {
                            int len = shotFromByteIter(iter);
                            int[] x = new int[len];
                            int[] y = new int[len];
                            for(int i = 0; i < len; i ++){
                                x[i] = shotFromByteIter(iter);
                                y[i] = shotFromByteIter(iter);
                            }
                            g.drawPolygon(x, y, len);
                        }
                        break;
                    case 9:
                        {
                            int len = shotFromByteIter(iter);
                            int[] x = new int[len];
                            int[] y = new int[len];
                            for(int i = 0; i < len; i ++){
                                x[i] = shotFromByteIter(iter);
                                y[i] = shotFromByteIter(iter);
                            }
                            g.fillPolygon(x, y, len);
                        }
                    break;
                    case 10:
                        {
                            int len = shotFromByteIter(iter);
                            int[] x = new int[len];
                            int[] y = new int[len];
                            for(int i = 0; i < len; i ++){
                                x[i] = shotFromByteIter(iter);
                                y[i] = shotFromByteIter(iter);
                            }
                            g.drawPolyline(x, y, len);
                        }
                        break;

                    case 11:
                    {
                        int x = shotFromByteIter(iter);
                        int y = shotFromByteIter(iter);
                        int w = shotFromByteIter(iter);
                        int h = shotFromByteIter(iter);
                        x -= w;
                        y -= h;
                        w *= 2;
                        h *= 2;
                        g.drawOval(x, y, w, h);
                    }
                    break;

                    default:
                        break;
                }
            }
            this.rootFrame.repaint();
            this.processing = false;
        });
        t.setName("Draw Call");
        t.run();

    }

    private static int unsignedByteFromIter(Iterator<Byte> iter){
        return (((int)iter.next()) & 0xFF);
    }
    private static int shotFromByteIter(Iterator<Byte> iter){
        int t = ((int)iter.next()) << 8;//its signed :)
        t |= (((int)iter.next()) & 0xFF);
        return t;
    }

    @Override
    public void breakpoint(VirtualMachine emu, int call_id) {
        throw new RuntimeException("bruh");
    }
}

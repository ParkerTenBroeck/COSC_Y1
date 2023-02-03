import BasicIO.ASCIIDataFile;
import BasicIO.ASCIIDisplayer;
import BasicIO.ASCIIPrompter;

public class Main {
    public static void main(String[] args) {
        variableNumbers();
    }

    private static void BRUH(){

    }
    private static void dummyCode(){
        int[] bruh = {1,2,3,4};
        for(int i = 0; i < bruh.length; i ++){
            System.out.println(i + ": " + bruh[i]);
        }
    }

    private static void variableNumbers() {
        int[] data = new int[100];
        int numValues;
        ASCIIPrompter in = new ASCIIPrompter();
        ASCIIDisplayer out = new ASCIIDisplayer();

        out.writeLine("Inter values (<=0 to stop)");

        for(numValues = 0; numValues < data.length; numValues ++){
            int read = in.readInt();
            if (read <= 0 ){
                break;
            }
            data[numValues] = read;
            out.writeLine(numValues + ":\t" + data[numValues]);
        }

        int total = 0;
        for(int i = 0; i < numValues; i ++){
            total += data[i];
        }
        out.writeLine("------");
        out.writeLine("num values:\t" + numValues);
        out.writeLine("total:\t\t" + total);
        out.writeLine("average:\t\t" + total/numValues);



        out.close();
        in.close();

    }

    private static void averaging4() {
        ASCIIDataFile input = new ASCIIDataFile("/home/may/Documents/GitHub/Cosc Assignments/COSC1P03/Assign_1/percipitation.txt");
        ASCIIDisplayer output = new ASCIIDisplayer();

        int numValues = 0;
        double[] values = new double[1];
        double average = 0;
        double in;

        in = input.readDouble();
        while(!input.isEOF()){
            output.writeLine(numValues + "\t> " + in);
            average += in;
            numValues += 1;
            values[numValues] = in;
            in = input.readDouble();
        }
        output.writeLine("---------");
        output.writeLine("Sum:\t\t" + average);
        average = average / numValues;
        output.writeLine("Average:\t" + average);

        input.close();
        output.close();
    }

    private static void averaging3() {
        ASCIIDataFile input = new ASCIIDataFile("/home/may/Documents/GitHub/Cosc Assignments/COSC1P03/Assign_1/percipitation.txt");
        ASCIIDisplayer output = new ASCIIDisplayer();

        int numValues = 12;
        double[] reading = new double[numValues];
        double average = 0;

        for(int i = 0; i < numValues; i ++){
            reading[i] = input.readDouble();
            output.writeLine(" = " + i + "\t:" + reading[i]);
            average += reading[i];
        }
        output.writeLine("---------");
        output.writeLine("Sum:\t\t" + average);
        average = average / numValues;
        output.writeLine("Average:\t" + average);

        for(int i = 0; i<reading.length; i ++){
            output.writeString(" ="+i+":\t"+reading[i]);
            if(reading[i] < average)
                output.writeLine("\t-");
            else if(reading[i] > average)
                output.writeLine("\t+");
            else
                output.writeLine("\t=");
        }

        input.close();
        output.close();
    }

    private static void averaging2() {
        ASCIIDataFile input = new ASCIIDataFile("/home/may/Documents/GitHub/Cosc Assignments/COSC1P03/Assign_1/percipitation.txt");
        ASCIIDisplayer output = new ASCIIDisplayer();

        int numValues = 0;
        double current;
        double average = 0;

        current = input.readDouble();
        while(!input.isEOF()){
            numValues += 1;
            output.writeLine(numValues + "\t> " + current);
            average += current;
            current = input.readDouble();
        }
        output.writeLine("---------");
        output.writeLine("Sum:\t\t" + average);
        average = average / numValues;
        output.writeLine("Average:\t" + average);

        input.close();
        output.close();
    }

    private static void averaging() {
        int numValues;
        int current;
        int average = 0;
        ASCIIPrompter prompt = new ASCIIPrompter();
        ASCIIDisplayer display = new ASCIIDisplayer();
        prompt.setLabel("# value");
        numValues = prompt.readInt();
        prompt.setLabel("Value?");
        for(int i = 0; i < numValues;i++){
            current = prompt.readInt();
            display.writeLine("> " + current);
            average += current;
        }
        average = average / numValues;
        display.writeLine("---------");
        display.writeLine("Average:\t" + average);
    }
}
/*

* Guy Vignoli

* Login-id: gvignoli

* 701168004

* Assignment 1

* 9/23/15

 */

package encryption;

import java.io.*;

import java.math.BigInteger;

/**

 *

 * @author guy vignoli

 */

public class Encrypt {

    /**

     * @param args the command line arguments

     */

    static String path = System.getProperty("user.dir");

    

    public static void main(String[] args) {

        System.out.println(path);

        String inputText = readFile();

        String key = readKey();

        String shifted = "";

        String parityS = "";

        //System.out.println(inputText);

        String stripText = preproc(inputText);

        String vigEnc = vigEnc(stripText, key);

        String padded = pad(vigEnc);

        System.out.println("\nPadding step...\n" + padded);

        //System.out.println(padded.length()/16);

       

        System.out.println("\nShifting step...");

        int pointer = 0;

        for(int i = 0; i<padded.length()/16; i++){//breaking up into chunks of 4` ..check this with longer text!!!

            

            shifted += padded.substring(i*16, (i*16)+4);//gets every fourth set that does not need rotated and adds it to the final string

            pointer = i*16+4;

            

            for(int j=1; j<4; j++){

               //circ(j,padded.substring(pointer,pointer + 4));//inefficient math to get the sets of four that need rotated

               shifted += circ(j,padded.substring(pointer,pointer + 4));//inefficient math to get the sets of four that need rotated

               pointer += 4;// moving my pointer within this loop get get the sets of 4 chars that need to be shifted

                //this was really inefficient...obviously

                

            }

            

        }

        System.out.println(shifted);

        parityS = parity(shifted); //filling string with all chars after being perity bitted...

        /*

        //Proving that rgf_mul works and so does the rest tof that step.

        int tmp1 = RGF_mul(215, 2);

        int tmp2 = RGF_mul(212, 3);

        System.out.println("***"+(tmp1^tmp2^72^89));

        */

       

        mixCol(parityS);

        

        

    }

    public static void mixCol(String parityS){

        System.out.println("\n\n"+"MixCol step...");

        int count = 0;

        int[][] mix = new int[][]{

            {2,3,1,1},

            {1,2,3,1},

            {1,1,2,3},

            {3,1,1,2}};

        //holds the multiplication matrix

        int[][] in = new int[4][1];//hold the input matirx to multiply by 

        int[][]finalIn = new int[4][1];

        for(int row = 0; row<4; row++){//building the input matrix to multiply by

            in[row][0] = Integer.parseInt(parityS.substring(count, count+2),16);

            count = count+8;//stepping through the giant string parityS

        }

        int j = 0;

        int xor = 0;

        

        for(int y =0; y<4; y++){//looping through each matric and doing the multiplication..only does correctly on some nodes.

            xor = RGF_mul(in[0][0],mix[y][0]) ^ RGF_mul(in[1][0],mix[y][1]) ^ RGF_mul(in[2][0],mix[y][2]) ^ 

RGF_mul(in[3][0],mix[y][3]);

            System.out.println(Integer.toString(xor,16)+ " ");

                  

        }

   

    }

    

    

    public static String parity(String shifted){

        System.out.println("\nParity bit...");

        String parityBit = "";

        String parityStr = ""; //string to hold final string of all chars run through this

        int count = 0;

        int hex = 0;

        int t =0;

        String hexStr = "";

        for(int i = 0; i<shifted.length(); i++){

            char temp = shifted.charAt(i);

            String bin = Integer.toString(temp,2);

            count = bin.length() - bin.replace("1","").length(); //finding number of "1"'s 

            if(count%2 != 0 ){

                bin = "1"+bin;

            }

            hex = Integer.parseInt(bin,2); //converting binary string to binary int

            hexStr = Integer.toString(hex,16); //converting binary int to hex str

            parityStr += hexStr;

            //RGF_mul(hex, 2);

            if(i%4 == 0 && i!=0){//Printing output in pretty 4x4 blocks

                System.out.println("\n");

                if(i!=0 && i%16 == 0)

                  System.out.println("\n");

            }

            System.out.print(hexStr + " ");

            

            

       

        }

        return parityStr;

    }

    

    public static int RGF_mul(int x, int y){//x= number to multiply... y= 3 || 2

        String xor = "00011011";

        String shiftx = "";

        int t = 0;

        int shiftx2=0;

        int finalx=0;

        if(y==1){ //the above always gets ran..but this step will just take the above and add another "int x" to bult by 3

            

            return x; 

          }

          shiftx = Integer.toBinaryString(x<<1).substring(1);//bit shift 1 to multiply by 2

      

      

          if(y==3){ //the above always gets ran..but this step will just take the above and add another "int x" to bult by 3

              shiftx = Integer.toBinaryString(Integer.parseInt(shiftx,2) ^ x);//taking the number multiplied by 2 and adding itself to it to get a mult 3 result.

              

          }

          //checking for MSB bit to be 1...if so do an XOR

          if(Integer.toBinaryString(x).charAt(0) == '1'){

     

              t =  Integer.parseInt(xor,2);//converts x from hex to binary string

              finalx = Integer.parseInt(shiftx,2) ^ t;//binx has now been XOR with 00011011

          }

          else{

              finalx = Integer.parseInt(shiftx,2);

          }

          return finalx;

      }

        

       

    public static String circ(int n, String sub){// does the rotation on set of 4 chars 

        String circShift = "";

        String temp1 = "";//creatin temp variable to hold a substring

        String temp2 = "";//same here

        temp1 = sub.substring(0,n);//getting first char and saving it to temp var

        temp2 = sub.substring(n,4);//getting the rest that needs to be shifted

        circShift = temp2+temp1;//concatinating for 'shift'

      return circShift;//returning the final shifted string lof lenght 4

    }

   

    

    public static String pad(String ciphText){

        //checking for initial size, ciphText might not need padded

        if(ciphText.length()%16 ==0){

            return ciphText;//Return original ciphtext because it doesnt need padded

        }

        //System.out.print(ciphText);

        return pad(ciphText+="A");// using recursion to add padding

    }

    

    public static String vigEnc(String stripText, String key){

        System.out.println("\nVigener Cipher encryption...");

        String ciphText = "";

        int tempIndex=0;

        int keyIndex = 0;

        int offset = 65; //offsetting ascii table to make A=0 ... Z=25

        for(int i=0; i<stripText.length(); i++){

            

            tempIndex = (int)stripText.charAt(i) - offset;//getting location of each char in plaintext

            if(i%(key.length()) == 0){ //some modulous math to find the end of key length in the plain text..

                keyIndex = 0;//once it finds the end of key length, reset key pointer to 0, starts the key reading over

            }

            tempIndex += (int)key.charAt(keyIndex) -offset; //adding the location of key to plaintext and subtracting offset to go from ascii to the 0-26 numbering

            tempIndex = tempIndex%26; // after adding each index, mod 26 to get num of cipher char

            ciphText += Character.toString((char)(tempIndex+offset));//converting back to ascii so java can convert to chars

            keyIndex++;//moving through the key

        }   

        System.out.println(ciphText);

        return ciphText;

    }

    

    public static String preproc(String plainText){

        System.out.println("\nPreprocessing Step...");

        plainText = plainText.replaceAll("\\s+","");// looking for whitespaces using regex and removes it

        plainText = plainText.replaceAll("\\p{P}","");// looks for all punctuation using regex and removes it

        System.out.println(plainText); //test print of preprocessing steps

        return plainText;

        

    }

    public static String readKey(){

      String keyFile = "key.txt";

        String keyText = "";

        String line = "";

    try {

            

            FileReader keyReader = new FileReader(keyFile);

            BufferedReader bufferedKey = 

                new BufferedReader(keyReader);

            

            while((line = bufferedKey.readLine()) != null) {

                keyText += line;

            }    

            bufferedKey.close();         

        }

        catch(FileNotFoundException ex) {

            System.out.println(

                "Unable to open file '" + 

                keyFile + "'");

        }

        catch(IOException ex) {

            System.out.println(

                "Error reading file '" 

                + keyFile + "'");                  

        }

          return keyText;

    }

    

    public static String readFile(){

        String input = path+"\\input.txt";

        String line = "";

        String plainText = "";

    

          try {

               FileReader fileReader = new FileReader(input);

            BufferedReader bufferedReader = 

                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                plainText += line;//getting plaintext file contents

            }   

            // Always close files.

            bufferedReader.close();         

        }

        catch(FileNotFoundException ex) {

            System.out.println(

                "Unable to open file '" + 

                input + "'");

        }

        catch(IOException ex) {

            System.out.println(

                "Error reading file '" 

                + input + "'");                  

        }

          return plainText;

    }

    

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;
import java.io.*;

/**
 *
 * @author guy
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
        //System.out.println(inputText);
        String stripText = preproc(inputText);
        vigEnc(stripText);
    }
    public static String vigEnc(String stripText){
        String ciphText = "";
        int offset = 65; //offsetting ascii table to make A=0 ... Z=25
        //System.out.println((int)stripText.charAt(0)); //just checking ascii conversion
        for(int i=0; i<=stripText.length(); i++){
            
        }
        return ciphText;
    }
    
    public static String preproc(String plainText){
        System.out.println("Preprocessing Step...");
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
                //System.out.println(key);//getting key
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
                //System.out.println(line);
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

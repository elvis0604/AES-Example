import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class AliceProgramAES 
{
	final static int numRun = 100;
    public static String encrypt(String key, String initVector, String value) 
    {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            //System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));//Debugging

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    static String readFile(String path, Charset encoding) throws IOException 
 	{
 		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 System.out.println("File Read Successfully!");//Debugging
 		 return new String(encoded, encoding);
 	}
     
	static void writeFile(String fileName, String cipherText)
	{
		 try
		 {
			 File file = new File(fileName);
			 FileUtils.writeStringToFile(file, cipherText, Charset.defaultCharset());
			 System.out.println("File Written Successfully!");//Debugging
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	}
	
	public static boolean checkFile(String filePath)
	{
		File f = new File(filePath);
		return (f.exists() && !f.isDirectory());
	}
	
    public static void runTime(float runtime)
    {
        System.out.println("Run time: " + runtime + " ms");
        System.out.println("Average run time: " + runtime / numRun + " ms");
    }
	
    public static void main(String[] args) throws IOException 
    {
    	try
		{
	        if (args.length != 1) 
	        {
	            System.err.println("Usage: java AESEncrypt <string message>");
	        } else 
	        {
	        	String filepath = "C:\\Users\\anhtu\\eclipse-workspace\\AES-Example\\secretKey.txt";
				if(checkFile(filepath))  //check if file exist; return true if exist
				{ 
		        	//Our secret message
			    	String orginalMessage = args[0];
		        	String encryptedStr = "";
			        String initVector = "RandomInitVector"; // 16 bytes IV
			        
			        final String key = readFile("secretKey.txt", StandardCharsets.UTF_8);
		        	
			        long startTime = System.currentTimeMillis();
		        	for(int i = 0; i < numRun; i++)
		        	{
				        encryptedStr = encrypt(key, initVector, orginalMessage);
		        	}
		        	long stopTime = System.currentTimeMillis();
		            float elapsedTime = stopTime - startTime;
		            
		        	//Write cipher to file
		        	writeFile("ctext.txt", encryptedStr);
		        	
		        	//Calculating runtime
			        runTime(elapsedTime);
				} else
				{
					System.out.println("secretKey.txt doesn't exist. Please create one");
				}
	        }
		} catch (IOException e) {
			 e.printStackTrace();
		}
    }
}

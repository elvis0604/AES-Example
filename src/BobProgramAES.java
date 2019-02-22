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

public class BobProgramAES
{
	final static int numRun = 100;
	public static String decrypt(String key, String initVector, String encrypted) 
	{
		try{
	        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	
	        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
	
	        return new String(original);
		    } catch (Exception ex) 
		{
		    ex.printStackTrace();
	    }
	    return null;
	}	
	
    static String readFile(String path, Charset encoding) throws IOException 
 	{
 		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 System.out.println("File Read Successfully!");
 		 return new String(encoded, encoding);
 	}
     
	static void writeFile(String fileName, String cipherText)
	{
		 try
		 {
			 File file = new File(fileName);
			 FileUtils.writeStringToFile(file, cipherText, Charset.defaultCharset());
			 System.out.println("File Written Successfully!");
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
        	String filepath = "C:\\Users\\anhtu\\eclipse-workspace\\AES-Example\\ctext.txt";
			if(checkFile(filepath))
			{
				String decryptedStr = "";
				String initVector = "RandomInitVector"; // 16 bytes IV to pass in for decryption
				
				//Reading file
				String cipherText = readFile("ctext.txt", StandardCharsets.UTF_8);
				final String key = readFile("secretKey.txt", StandardCharsets.UTF_8);
				
				//Decrypting
				long startTime = System.currentTimeMillis();
		    	for(int i = 0; i < numRun; i++)
		    	{
					decryptedStr = decrypt(key, initVector, cipherText);
		    	}
		    	long stopTime = System.currentTimeMillis();
		        float elapsedTime = stopTime - startTime;
		    	System.out.println(decryptedStr);

		        //Calculating runtime
		        runTime(elapsedTime);
			} else
			{
				System.out.println("ctext.txt doesn't exist. Please create one");
			}
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}

}

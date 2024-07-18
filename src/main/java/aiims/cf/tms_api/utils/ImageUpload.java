package aiims.cf.tms_api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUpload {

	 public static boolean saveBase64Image(String base64Image, String saveLocation, String fileName) throws IOException {
	        try {
//	        	
//	        	System.out.println("base64Image : "+base64Image);
	        	
	        	String[] base64StrArray = base64Image.split(",");
	        	
//	        	
	        	
//	        	System.out.println("base64StrArray 0&1 : "+base64StrArray[0]+" | "+base64StrArray[1]);
	            // Decoding Base64 string to bytes
	            byte[] imageBytes = Base64.getDecoder().decode(base64StrArray[1]);
	            
	            // Saving the image to a file
	            String filePath = saveLocation + fileName;
	            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
	            outputStream.write(imageBytes);
	            outputStream.close();
	            return true; 
//	            return "Image saved successfully!";
	        } catch (IOException ioe) {       
	            throw new IOException("Could not save image file: ", ioe);
	        }
			
	    }
}

package models.util;

import java.io.IOException;
import java.io.RandomAccessFile;

public class File2String 
{	
	public static String getString(String fileName)
	{
		RandomAccessFile raf;
		String str = "";
		String line;
		try {
			raf = new RandomAccessFile(fileName, "r");
			
			while( (line = raf.readLine()) != null )
				str += line + "\n";
			
			raf.close();
		} catch(IOException e) { e.printStackTrace(); }
	
		return str;
	}	
}

package com.example;
import java.util.*; 
import java.nio.charset.StandardCharsets; 
import java.nio.file.*; 
import java.io.*; 
public class ReadFileIntoList 
{ 
  public static List<String> readFileInList(String fileName) 
  { 
  
    List<String> lines = Collections.emptyList(); 
    try
    { 
      lines = 
       Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
    } 
  
    catch (IOException e) 
    { 
  
      // do something 
      e.printStackTrace(); 
    } 
    return lines; 
  } 
  public static void main(String[] args) 
  { 
    List l = readFileInList("/Users/vns/Desktop/iptab24oct.txt"); 
  
    Iterator<String> itr = l.iterator(); 
    int i=0;
    while (itr.hasNext()) {
    	 i=i+1;
    	 String txtline=itr.next();
    	//System.out.println(txtline);
    //	 boolean isFound = txtline.indexOf("-A ") !=-1? true: false;
    	 boolean isFound = txtline.contains("-A PREROUTING -s");
    	 if(isFound==true) {
    		 System.out.print(txtline.substring(txtline.indexOf("-s")+3 , txtline.indexOf("/32")));
    		 System.out.print(" "+txtline.substring(txtline.indexOf("--dport")+8 , txtline.indexOf("-j")));
    		 System.out.print(" "+txtline.substring(txtline.indexOf("--to-ports")+11 ));
    		 System.out.println(" "+txtline); 
    	 }
      
    }
  } 
} 

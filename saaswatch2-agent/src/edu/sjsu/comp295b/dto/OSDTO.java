package edu.sjsu.comp295b.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "os")
public class OSDTO {

	 public String arch; 
	 public int availableProcessors; 
     public String name;
     public double systemLoadAverage; 
     public double version;
     
     public long timestamp;
}

package edu.sjsu.comp295b.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "debug")
public class DebugDTO {

	public String data;
	public String className;
	
	public long timestamp;
}

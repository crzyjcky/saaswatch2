package edu.sjsu.comp295b.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "connection")
public class ConnectionDTO {

	public String url;
	public boolean isUp;
	
	public long timestamp;
}

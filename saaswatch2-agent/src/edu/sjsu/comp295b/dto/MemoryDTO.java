package edu.sjsu.comp295b.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "memory")
public class MemoryDTO {

	public long heapCommitted;
	public long heapInit;
	public long heapMax;
	public long heapUsed;
	public long nonHeapCommitted;
	public long nonHeapInit;
	public long nonHeapMax;
	public long nonHeapUsed;
	
	public long timestamp;
}

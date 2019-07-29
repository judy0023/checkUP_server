package kr.ac.hansung.cse.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

public class TotalResult {
	
	private String cancer_name;
	
	private String cancer_info;	

	private String inspection_item;

	private String inspection_reason;
	
	private String [] hos_name = new String[10];
	
	private String [] hos_addr = new String[10];
	
	private String [] hos_no = new String[10];
	
	
}

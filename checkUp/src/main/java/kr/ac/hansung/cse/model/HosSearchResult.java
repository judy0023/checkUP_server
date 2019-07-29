package kr.ac.hansung.cse.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HosSearchResult {
	
	private String hos_name[] = new String [30];
	
	private String hos_addr[] = new String [30];
	
	private String hos_no[] = new String [30];

}

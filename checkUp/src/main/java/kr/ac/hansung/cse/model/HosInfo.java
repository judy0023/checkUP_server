package kr.ac.hansung.cse.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HosInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1646041920476970657L;

	// 병원 주소
	private String hos_addr;
	
	// 병원 번호
	private String hos_number;
	
	// 병원 이름
	private String hos_name;
	
	// 유방암 검진 여부
	private String bcCharg;
	
	// 대장암 검진 여부
	private String ccCharg;
	
	// 자궁경부암 검진 여부
	private String cvChrg;
	
	// 위암 검진 여부
	private String stmcaChrg;

	// 간암 검진 여부
	private String lvcaChrg;
	
	// 일반 검진 여부
	private String grenChrg;
	
	// 영유아 검진 여부
	private String ichkChart;
	
	// 구강 검진 여부
	private String mchkChrg;
		
}

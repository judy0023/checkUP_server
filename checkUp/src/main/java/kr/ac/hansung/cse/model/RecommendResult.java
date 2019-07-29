package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecommendResult implements Serializable  {
	
	private static final long serialVersionUID = -3251995999219918603L;

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "num")
	private int num;
	
	@Column(name = "inspection_item")
	private String inspection_item;
	
	@Column(name = "inspection_reason")
	private String inspection_reason;	
			
}

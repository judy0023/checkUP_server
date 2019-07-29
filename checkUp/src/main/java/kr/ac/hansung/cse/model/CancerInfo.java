package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="cancer")
public class CancerInfo implements Serializable  {
	
	private static final long serialVersionUID = -4415623141890741209L;

	@Id
	@Column(name = "num")
	private Long cancer_num;
	
	@Column(name = "name")
	private String cancer_name;
	
	@Column(name = "info")
	private String cancer_info;	
		
	
}

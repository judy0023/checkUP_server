package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="recommend_result")
public class Condition implements Serializable {

	private static final long serialVersionUID = 815570464049760792L;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "num")
	private int num;
	
	@Column(name = "name")
	private String name;	
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "minimum_age")
	private String minimum_age;
	
	@Column(name = "maximum_age")
	private String maximum_age;
	
	@Column(name = "family_history")
	private String family_history;
	
	@Column(name = "related_disease")
	private String related_disease;
	
	@Column(name = "smoking_day")
	private String smoking_day;
	
	@Column(name = "no_smoking_day")
	private String no_smoking_day;
	
	@Column(name = "cycle")
	private String cycle;	

}

package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Customer implements Serializable {

	private static final long serialVersionUID = -3317295692023149262L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "no")
	private Long no;

	// 이름
	@Column(name = "name")
	private String name;

	// 성별
	@Column(name = "gender")
	private int gender;

	// 나이
	@Column(name = "age")
	private int age;

	// 흡연 경험 여부
	@Column(name = "smoking")
	private int smoking;

	// ---------------------- 흡연 기간 ----------------------
	// 담배 갑 개수
	@Column(name = "smoking_day_cut")
	private int smoking_day_cut;

	// 몇 년
	@Column(name = "smoking_day")
	private int smoking_day;
	// ----------------------------------------------------

	// 금연 기간 - 년수
	@Column(name = "noSmoking_day")
	private int noSmoking_day;

	// 성생활유무
	@Column(name = "sexlife")
	private int sexlife;

	// ------------------ (본인)관련질환 유무 ------------------
	// 갑상선암
	@Column(name = "tcancer")
	private int tcancer;

	// 간암
	@Column(name = "lcancer")
	private int lcancer;

	// 위암
	@Column(name = "scancer")
	private int scancer;

	// 대장암
	@Column(name = "cocancer")
	private int cocancer;

	// 자궁경부암
	@Column(name = "ccancer")
	private int ccancer;

	// 유방암
	@Column(name = "bcancer")
	private int bcancer;
	// ---------------------------------------------------

	// -------------------(가족)관련질환 유무 -----------------

	// 갑상선암
	private int tcancer_F;

	// 위암
	private int scancer_F;

	// 대장암
	private int cocancer_F;

	// 유방암
	private int bcancer_F;

	// ---------------------------------------------------

	// --------------------- 검사주기 ----------------------

	// 갑상선암 - 갑상선암 초음파
	private int tcancer_A;

	// 간암 - 간초음파
	@Column(name = "lcancer_A")
	private int lcancer_A;

	// 대장암 - 분변장혈검사
	@Column(name = "cocancer_A")
	private int cocancer_A;

	// 대장암 - 대장내시경
	@Column(name = "cocancer_B")
	private int cocancer_B;

	// 위암 - 위장조염검사
	@Column(name = "scancer_A")
	private int scancer_A;

	// 위암 - 위내시경
	@Column(name = "scancer_B")
	private int scancer_B;

	// 유방암 - 유방촬영
	@Column(name = "bcancer_A")
	private int bcancer_A;

	// 자궁경부암 - 자궁 경부 세포검사
	@Column(name = "ccancer_A")
	private int ccancer_A;

	// 자궁경부암 - 인유두종 바이러스 검사
	@Column(name = "ccancer_B")
	private int ccancer_B;
	
	// 폐암 - 저선량 흉부 CT
	@Column(name = "lucancer_A")
	private int lucancer_A;
	
	// 시/도
	@Transient
	private int city;
	
	// 시/군/구
	@Transient
	private int price;
	
	@Transient
	private int hosRecCheck;	

	// ---------------------------------------------------

		public Customer(String name, int gender, int age, int smoking, int smoking_day_cut, int smoking_day,
			int noSmoking_day, int sexlife, int tcancer, int lcancer, int scancer, int cocancer, int ccancer,
			int bcancer, int tcancer_F, int scancer_F, int cocancer_F, int bcancer_F, int tcancer_A, int lcancer_A,
			int cocancer_A, int cocancer_B, int scancer_A, int scancer_B, int bcancer_A, int ccancer_A, int ccancer_B) {

		this.name = name;
		this.gender = gender;
		this.age = age;
		this.smoking = smoking;
		this.smoking_day_cut = smoking_day_cut;
		this.smoking_day = smoking_day;
		this.noSmoking_day = noSmoking_day;
		this.sexlife = sexlife;
		this.tcancer = tcancer;
		this.lcancer = lcancer;
		this.scancer = scancer;
		this.cocancer = cocancer;
		this.ccancer = ccancer;
		this.bcancer = bcancer;
		this.tcancer_F = tcancer_F;
		this.scancer_F = scancer_F;
		this.cocancer_F = cocancer_F;
		this.bcancer_F = bcancer_F;
		this.tcancer_A = tcancer_A;
		this.lcancer_A = lcancer_A;
		this.cocancer_A = cocancer_A;
		this.cocancer_B = cocancer_B;
		this.scancer_A = scancer_A;
		this.scancer_B = scancer_B;
		this.bcancer_A = bcancer_A;
		this.ccancer_A = ccancer_A;
		this.ccancer_B = ccancer_B;
	}

}

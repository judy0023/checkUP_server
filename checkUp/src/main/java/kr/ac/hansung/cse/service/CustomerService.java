package kr.ac.hansung.cse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.hansung.cse.model.CancerInfo;
import kr.ac.hansung.cse.model.Condition;
import kr.ac.hansung.cse.model.Customer;
import kr.ac.hansung.cse.model.RecommendResult;
import kr.ac.hansung.cse.model.TotalResult;
import kr.ac.hansung.cse.repo.CancerInfoRepository;
import kr.ac.hansung.cse.repo.ConditionRepository;
import kr.ac.hansung.cse.repo.CustomerRepository;
import kr.ac.hansung.cse.repo.RecommendResultRepository;

@Service
public class CustomerService {

	Customer customer; // 사용자에게 받은 문진표
	Condition condition; // db에 저장 해 놓은 조건
	Vector<Integer> v; // 처리 결과의 id 저장
	
	RecommendResult recommendResult; // 검진 결과 저장
	CancerInfo cancerInfo; // 암 정보 저장
	

	@Autowired
	RecommendResultRepository recommendResultRepo;
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ConditionRepository conditionRepo;
	
	@Autowired
	CancerInfoRepository cancerInfoRepo;
	
	public void saveCustomerService(Customer customer) {
		customerRepository.save(customer);
	}

	public List<TotalResult> recommendService(long id) {
		
		customer = customerRepository.findOne(id);
		
		v = new Vector<Integer>();

		List<Condition> list = new ArrayList<>();
		Iterable<Condition> conditions = conditionRepo.findAll();

		conditions.forEach(list::add);
		// db의 조건들을 list에 불러오기
		
		for (int i = 0; i < 17; i++) {
			condition = list.get(i);
				

			if (Integer.parseInt(condition.getGender()) > customer.getGender())
				continue;
			 

			if ((condition.getMinimum_age() == null)
					|| (Integer.parseInt(condition.getMinimum_age()) <= customer.getAge())) {
				
				if ((condition.getMaximum_age() == null)
						|| (Integer.parseInt(condition.getMaximum_age()) > customer.getAge())) {
					
					int num = condition.getNum();
					
					switch (num) {
					case 1:
						if (Integer.parseInt(condition.getRelated_disease()) == customer.getLcancer()) {
							if (agechange(Double.parseDouble(condition.getCycle())) < customer.getLcancer_A())
								v.addElement((int) (long) (condition.getId()));
						}
						break;
					case 2:
						if (Integer.parseInt(condition.getFamily_history()) == customer.getCocancer_F()) {
							
							String condition_cycle = condition.getCycle();
							if(condition_cycle != null && (condition_cycle.length()!=0)) {
								int cycle = checkcycle_cancer(customer.getCocancer_A(), customer.getCocancer_B());
								System.out.println(agechange(Double.parseDouble(condition_cycle)) +", "+ cycle);
								if (agechange(Double.parseDouble(condition_cycle)) < cycle)
									v.addElement((int) (long) (condition.getId()));
							}
							else
								v.addElement((int) (long) (condition.getId()));							
						}
						break;
					case 3:
						if (Integer.parseInt(condition.getRelated_disease()) == customer.getScancer()) {
							if (Integer.parseInt(condition.getFamily_history()) == customer.getScancer_F()) {
								int cycle = checkcycle_cancer(customer.getScancer_A(), customer.getScancer_B());
								System.out.println(agechange(Double.parseDouble(condition.getCycle())) +", "+ cycle);
								if (agechange(Double.parseDouble(condition.getCycle())) < cycle)
									v.addElement((int) (long) (condition.getId()));
							}

						}
						break;
					case 4: // 폐암
						System.out.println("==condition.getSmoking_day==" + condition.getSmoking_day());
						if (Integer.parseInt(condition.getSmoking_day()) <= (customer.getSmoking_day()
								* customer.getSmoking_day_cut())) {
							System.out.println("customer.getNoSmoking_day() = " + customer.getNoSmoking_day());
							
							// 주기 조건 추가
							if (agechange(Double.parseDouble(condition.getCycle())) < customer.getLucancer_A()) {
								if (condition.getNo_smoking_day() == null) { // 금연조건이 없을경우

									if (customer.getNoSmoking_day() == 0) { // 금연을 안할 경우
										//System.out.println("111");
										v.addElement((int) (long) (condition.getId()));
									}
								} else { // 금연조건이 있을경우

									if (customer.getNoSmoking_day() > 0 && customer.getNoSmoking_day() <= 15) {
										//System.out.println("222");
										v.addElement((int) (long) (condition.getId()));
									}
								}
							}
						}
						break;
					case 5: // 유방암
						if (Integer.parseInt(condition.getFamily_history()) == customer.getBcancer_F()) {
							if (agechange(Double.parseDouble(condition.getCycle())) < customer.getBcancer_A())
								v.addElement((int) (long) (condition.getId()));
						}
						break;
					case 6: // 자궁경부암
						if (Integer.parseInt(condition.getRelated_disease()) == customer.getCcancer()) {
							int cycle = checkcycle_cancer(customer.getCcancer_A(), customer.getCcancer_B());
							if (agechange(Double.parseDouble(condition.getCycle())) < cycle)
								v.addElement((int) (long) (condition.getId()));
						}
						break;
					case 7: // 갑상선암
						if (Integer.parseInt(condition.getRelated_disease()) == customer.getTcancer()) {
							if (Integer.parseInt(condition.getFamily_history()) == customer.getTcancer_F()) {
								if (agechange(Double.parseDouble(condition.getCycle())) < customer.getTcancer_A())
									v.addElement((int) (long) (condition.getId()));
							}
						}
						break;
					}
				}
			}

		}
		
		
		List<RecommendResult> resultList = new ArrayList<>();

		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.elementAt(i) + ", ");
		}

		if (v.size() != 0) {
			for (int i = 0; i < v.size(); i++) {
				resultList.add(recommendResultRepo.findOne((long) v.elementAt(i)));
			}
		} else {
			recommendResult = new RecommendResult();
			recommendResult.setId((long) 0);
			recommendResult.setInspection_item("");
			recommendResult.setInspection_reason("검사가 필요한 항목이 없습니다.");
			resultList.add(recommendResult);
		}
		
		
		//+++++++++++++++++++++++++++++
		
		recommendResult = new RecommendResult();
		cancerInfo = new CancerInfo();
		
		TotalResult totalResult; 
		
		List<TotalResult> totalResultList = new ArrayList<>();
		
		if (v.size() != 0) {
			for (int i = 0; i < v.size(); i++) {		
				
				recommendResult = recommendResultRepo.findOne((long)v.elementAt(i));
				cancerInfo = cancerInfoRepo.findOne((long)recommendResult.getNum());
				totalResult = new TotalResult();
				
				totalResult.setInspection_item(recommendResult.getInspection_item());
				totalResult.setInspection_reason(recommendResult.getInspection_reason());
				totalResult.setCancer_name(cancerInfo.getCancer_name());
				totalResult.setCancer_info(cancerInfo.getCancer_info());
								
				totalResultList.add(totalResult);
				
			}
		} else {
			totalResult = new TotalResult();
			totalResult.setCancer_name("");
			totalResult.setCancer_info("");
			totalResult.setInspection_item("검사가 필요한 항목이 없습니다.");
			totalResult.setInspection_reason("");
			totalResult.setHos_addr(null);
			totalResult.setHos_name(null);
			totalResultList.add(totalResult);
		}		
		
		return totalResultList;
		
	}

	int agechange(double age) {

		if (age<= 0.5)
			age = 1;
		else if (age > 0.5 && age <= 1)
			age = 2;
		else if (age > 1 && age <= 2)
			age = 3;
		else if (age > 2 && age <= 3)
			age = 4;
		else if (age > 3 && age <= 5)
			age = 5;
		else
			age = 6;

		return (int)age;
	}

	public int checkcycle_cancer(int cancer_A, int cancer_B) {

		int longcycle = 0;

		if (cancer_A > cancer_B)
			longcycle = cancer_A;
		else
			longcycle = cancer_B;

		return longcycle;
	}
}

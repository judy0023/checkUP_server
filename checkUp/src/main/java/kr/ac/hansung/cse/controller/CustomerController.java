package kr.ac.hansung.cse.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.TotalResult;
import kr.ac.hansung.cse.model.Customer;
import kr.ac.hansung.cse.service.CustomerService;
import kr.ac.hansung.cse.service.HospitalService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CustomerController {

	static Logger logger = LoggerFactory.getLogger(CustomerController.class);
	long no = 0;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	HospitalService hospitalService;
	
	List<TotalResult> totalResultList = null;
	
	@PostMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TotalResult>> getResults(@RequestBody Customer customer) throws IOException {

		logger.debug("Calling getResults()");
		
		// db에 고객 문진표 저장
		customerService.saveCustomerService(customer);
		
		no = customer.getNo();
		
		// 검진 항목이 존재하면 이유 보내기, 없으면 검진 항목이 없다고 보내기
		totalResultList = customerService.recommendService(no);
		if(totalResultList.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		// consol에 테스트
		/*for (int i = 0; i < totalResultList.size(); i++) {
			System.out.print(totalResultList.get(i).getInspection_item() + ", " + totalResultList.get(i).getInspection_reason());
		}*/
		
		System.out.println("sido : " + customer.getCity() + " " + "gungu : " + customer.getPrice());
		
		// angular(client)쪽에서 병원 추천을 받는다고 했는지 체크
		if(customer.getHosRecCheck() == 1 && !(totalResultList.get(0).getCancer_name()).equals("")) {
			// '41273' 식으로 오면 뒤에 '273'만 빼서 siGunGuCd에 넣기!
			String siDoCd = Integer.toString(customer.getCity());
			String siGunGuCd = Integer.toString(customer.getPrice());
			siGunGuCd = siGunGuCd.substring(siGunGuCd.length()-3, siGunGuCd.length());
						
			// 시도, 시군구 코드를 함수에 넘겨주기
			hospitalService.recommandHosService(siDoCd, siGunGuCd, totalResultList);
			System.out.println("siDoCd, siGunGuCd " + siDoCd + " " + siGunGuCd);
		}
		
		
		
		return new ResponseEntity<List<TotalResult>>(totalResultList, HttpStatus.OK);
		
	}
}

package kr.ac.hansung.cse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.ac.hansung.cse.model.HosInfo;
import kr.ac.hansung.cse.service.HospitalService;

@RestController
@RequestMapping("/api")
public class HosInfoController {
	
	@Autowired
	HospitalService hospitalService;

	HosInfo hosInfo = null;

	@GetMapping(value = "/hos_names/{hos_name}/hos_nos/{hos_no}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HosInfo> getHosInfo(@PathVariable("hos_name") String hos_name,
			@PathVariable("hos_no") String hos_no) {
		
		System.out.println(hos_name + " " + hos_no);
		hosInfo = hospitalService.getInfoService(hos_name, hos_no);

		if (hosInfo == null)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		System.out.println("call getHosInfo...");		

		return new ResponseEntity<HosInfo>(hosInfo, HttpStatus.OK);
	}

}

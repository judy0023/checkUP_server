package kr.ac.hansung.cse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.HosSearchResult;
import kr.ac.hansung.cse.service.HospitalService;

@RestController
@RequestMapping("/api")
public class HosSearchController {
	
	@Autowired
	HospitalService hospitalService;
	
	HosSearchResult hosSearchResult = null;

	// 지역 + 검진 타입 선택 ( 전체 - 0, 일반 - 1, 구강 - 2, 암 - 3, 일반 + 암 - 4, 출장검진 - 5, 영유아 - 6)
	@GetMapping(value = "/siDoCd/{siDoCd}/siGunGuCd/{siGunGuCd}/hchType/{hchType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HosSearchResult> searchHos(@PathVariable("siDoCd") int siDoCd,
			@PathVariable("siGunGuCd") int siGunGuCd, @PathVariable("hchType") int hchType) {
		
		String siDoCdStr = Integer.toString(siDoCd);
		String siGunGuCdStr = Integer.toString(siGunGuCd);
		// '41273' 식으로 오면 뒤에 '273'만 빼서 siGunGuCd에 넣기!
		siGunGuCdStr = siGunGuCdStr.substring(siGunGuCdStr.length() - 3, siGunGuCdStr.length());

		// 시도, 시군구 코드를 함수에 넘겨주기
		hosSearchResult = hospitalService.searchService(siDoCdStr, siGunGuCdStr, Integer.toString(hchType));

		if (hosSearchResult == null)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<HosSearchResult>(hosSearchResult, HttpStatus.OK);
	}

}

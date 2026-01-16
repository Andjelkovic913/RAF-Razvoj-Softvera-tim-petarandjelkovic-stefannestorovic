package org.raflab.studsluzbadesktopclient.services;

import java.util.Arrays;
import java.util.List;


import lombok.AllArgsConstructor;
import org.raflab.studsluzba.dtos.StudProgramDTO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@AllArgsConstructor
public class StudProgramService {

 	private final RestTemplate restTemplate;
	private final String baseUrl;
	
	private final String STUDPROGRAMI_URL_PATH = "/api/studijskiprogram";
	
	private String createURL(String pathEnd) {
		return baseUrl + STUDPROGRAMI_URL_PATH + "/" + pathEnd;
	}
	
	public List<StudProgramDTO> getSudijskiProgramiSorted(){
		StudProgramDTO[] retVal =
				restTemplate.getForObject(createURL("all/sorted"), StudProgramDTO[] .class);

		if (retVal == null) return null;
		return Arrays.asList(retVal);
	}
}

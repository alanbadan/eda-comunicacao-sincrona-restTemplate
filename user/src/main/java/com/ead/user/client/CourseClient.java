package com.ead.user.client;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ead.user.Dto.CourseDto;
import com.ead.user.Dto.ResponsePageDto;
import com.ead.user.servicies.UtilsService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Component //anotacao para ser um Bean do Spring paea ele gerenciar
public class CourseClient { //classe para comunicao sincrona  implementacao da requisiao de microservice user p course
	                      //precisa de uma classe de config para  (comunicacao externa) restTemplate
	
	@Autowired
	RestTemplate restTemplate;
	
	
	@Autowired //injetando para uso do metodo para montar a uri
	UtilsService utilsService;
	
	//montadno a uri sem a inteface Utils
	String REQUEST_URL_COURSE = "http://localhost:8082"; // cahama da uri sem o SERVICE REGISTER
	
	public Page<CourseDto> getAllCourseByUser(UUID userId, Pageable pageable){
		/*
		//MONTANDO A URL SIMPLES SEM DIVIDIR RESPONSABILIDADES eleas serao divididas na intrface UtilService 
		List<CourseDto> serchResult =null;
		String url = REQUEST_URL_COURSE + "/couse?userId" + userId + "&page" + pageable.getPageNumber() + "&sizes" + 
		pageable.getPageSize() + "&sort" + pageable.getSort().toString().replaceAll(": ", ".");
		*/
		
		 //usando o metodo da interface
		List<CourseDto> serchResult =null;
		ResponseEntity<ResponsePageDto<CourseDto>> result = null;
		String url = REQUEST_URL_COURSE + utilsService.createUrlGetAllCouseByUser(userId, pageable);

		try {
			//utilisando um classe ABSTRATA DO  spring CORE  para reposta da paginacao
			                            //definindo a parametrisaçao de cursoDto
			  ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
			//defindo o response entity http
		     result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
			//obtendo o content nesse caso o content
			serchResult = result.getBody().getContent();
			
		}catch (HttpStatusCodeException e) {
//			log.error("Error request /courses {} ", e);
		}
			return result.getBody();
	}
    ///metodo para montar a url para o ms user
	public void deleteUserIntoCourse(UUID userId) {
		String url = REQUEST_URL_COURSE + "/course/user" + userId;
		restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
		
	}


}
 
package com.ead.curso.cliente;

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

import com.ead.curso.dto.CourseUserDto;
import com.ead.curso.dto.ResponsePageDto;
import com.ead.curso.dto.UserDto;
import com.ead.curso.services.UtilsService;

@Component
public class AuthUserClient { //esse metodo esta explicado o microservice UserClient
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	UtilsService utilsService;
	
	String REQUEST_URL_AUTHUSER = "Http://localhost:8087"; //colocar a prota especifica do microservide que sta config no .yaml	
		
	
	public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
        List<UserDto> searchResult = null;
        ResponseEntity<ResponsePageDto<UserDto>> result = null;
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
//        log.debug("Request URL: {} ", url);
//        log.info("Request URL: {} ", url);
        try{
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
 //           log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e){
//            log.error("Error request /courses {} ", e);
        }
//        log.info("Ending request /users courseId {} ", courseId);
        return result.getBody();
    }
	//metdo para requsiatr um userId para o microService user
	public ResponseEntity<UserDto> getOneUserById (UUID userId){ // essa chamada desses metodo tb esta sendo usado no metod de validaco na classe Validator
		String url = REQUEST_URL_AUTHUSER + "/user/" + userId;
		return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class); // null requestEntity pq ainda não precisa nesse metodo(pq não tem autenticacao)
	}
	// metodo para avisar a incricao para course de um usuario
	public void postSubscriptionInCourse(UUID courseId, UUID userID) {
		//contruindo a requisicao via post
		String url = REQUEST_URL_AUTHUSER + "/user/" + userID + "/couser/subscription";		
		// iniciando o Dto que ser popula com os dados
		var courseUserDto = new CourseUserDto();
		courseUserDto.setCourseId(courseId);
		courseUserDto.setUserId(userID);
		restTemplate.postForObject(url, courseUserDto, String.class);
	}
	public void deleteCourseIntoUser(UUID courseId) {
	     //construindo a URL                     //url definida no ms user no metodo delete
		String url = REQUEST_URL_AUTHUSER + "/user/course" + courseId;		
		restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
	}

}

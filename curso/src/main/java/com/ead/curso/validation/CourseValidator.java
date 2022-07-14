package com.ead.curso.validation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import com.ead.curso.cliente.AuthUserClient;
import com.ead.curso.dto.CourseDto;
import com.ead.curso.dto.UserDto;
import com.ead.curso.enuns.UserType;

@Component // monstarando que é um bean gerenciado pelo spring
public class CourseValidator  implements Validator{
	
	@Autowired // definindo um validator default para não dar conflito
	@Qualifier("defaultValidator")//sem essa anotacao da conflito pq ja estamos usando um beanValidator do SrpingMvc config support
	private Validator validator;
	
	@Autowired // para vfazer uma chamada assincrona para user no metodo de validate...
	AuthUserClient authUserClient;
	
	

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override  
	public void validate(Object o, Errors errors) {
		// precisa fazer um casting pq é um Object
		CourseDto courseDto = (CourseDto) o;
		validator.validate(courseDto, errors); // faz a mesma coisa que o @Valid (conferindo todos os atributos do dto)
		if(!errors.hasErrors()) { //se conter erro ele entra no if
			validateUserInstructor(courseDto.getUserInstructor(), errors);
		}
	
	}
	//metedo para fazer uma chamada sincrona  para user e conferir o UserType nessa caso um validando um instrutor
	private void validateUserInstructor(UUID userInstructor, Errors errors) {
		ResponseEntity<UserDto> responseUserInstructor;
		try {
			responseUserInstructor = authUserClient.getOneUserById(userInstructor); // buscando no outro micriservice quem esta vindo como parametro
			if(responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
				errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR OR ADMIN");
				}
			//tratando se o user microservice manda notFound de userId
			}catch (HttpStatusCodeException e) {
				if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
					errors.rejectValue("userInstructor", "UserInstructorError", "INSTRUCTOR Not Found");
			}
		}
		
	}

}

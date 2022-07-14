package com.ead.curso.controller;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.ead.curso.cliente.AuthUserClient;
import com.ead.curso.dto.SubscriptionDto;
import com.ead.curso.dto.UserDto;
import com.ead.curso.enuns.UserStatus;
import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;
import com.ead.curso.services.CourseService;
import com.ead.curso.services.CourseUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2  
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
  //CONTROLLER PARA COMUNICACAO SINCRONA ENTRE USER E COURSE
public class CourseUserController {
	
	@Autowired
	AuthUserClient authuserClient;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	CourseUserService courseUserService;
	
	//Object se retorna pq vc não sabe o retorno que vira (isso deoende do cenario então vc usa o Object)
	
	
	@GetMapping("/course/{courseId}/user")
	public ResponseEntity<Object> getAllCourseByUser(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
			                                                  @PathVariable(value = "courseId") UUID courseId){
		//verifcando se o curso exixte para um usuario pq se deletar o curso a lista retornara vazia e não a necessiadae dess retorno se não haver relacao ( enão a comunicaçao sincrona sem necessidade)
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);//verificando se o curso existe
		if(!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(authuserClient.getAllUsersByCourse(courseId, pageable)); //metodo CLIENT para chamr um user de um determinado curso
		
	}
	//metodo para matricula um aluno em um curso
	@PostMapping("/course/{courseId}/user/subscription")
	public ResponseEntity<Object> saveSubscripitonUserInCourse(@PathVariable(value = "courseId") UUID courseId,
			                                                    @RequestBody @Valid SubscriptionDto subscriptionDto){
		//o response user po causa do try catch
		ResponseEntity<UserDto> responseUser;
		
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);//verificando se o curso existe
		if(!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
		}
		//verificando se o aluno ja faz parte do curso
		if(courseUserService.existByCourseAndCourse(courseModelOptional.get(), subscriptionDto.getUserId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR : subscripiton already exist");
		}
		//veridficar o usuario usando o userCleint
		//precisa fazer a conver~sao usando ObejctMapper ou BeansUtisl ou uasando um set simples ou metod declarado  como no courseUserModel
		//usando nesta exmeplo o metodo criado em CourseNodel
		try {//pq preciva fazer algumas tratativas 
			responseUser = authuserClient.getOneUserById(subscriptionDto.getUserId());
			//verificandi se ele esta ativo 
			if(responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("User is Blocked");
			}	
		}catch (HttpStatusCodeException e ) {
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
			}
			
		}
		/*
		 // esse metodo não garante uma transacao ATOMICA 9se der erro no save enão da o roolback dos dados anteriores 
		// se ele passar pelas verificacoes ele salva
		CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
	}
	*/
		// Metodo ATOMICAO TRANSATIONAL (salva e envia a inscricao par course)
		CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserIntoCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
		
	} //metodo dlete com comunicao sicnrona 
	@DeleteMapping("course/user/{userId}")
	public ResponseEntity<Object> deleteCourseuserByUser(@PathVariable(value = "userId") UUID userId){
		if(!courseUserService.existbyUserId(userId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not Found");
		}
		courseUserService.deleteCourseUserByUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfuly");
	}
	
}

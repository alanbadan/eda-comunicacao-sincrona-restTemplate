package com.ead.curso.services.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.curso.cliente.AuthUserClient;
import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;
import com.ead.curso.repository.CourseUserRepository;
import com.ead.curso.services.CourseUserService;

@Service
public class CourseUserServiceImpl  implements CourseUserService{
	
	
	@Autowired
	CourseUserRepository courseUserRepository;
	
	@Autowired
	AuthUserClient authUserClient;
	
	
	@Override
	public boolean existByCourseAndCourse(CourseModel courseModel, UUID userId) {
		return courseUserRepository.existByCourseAndUserId(courseModel, userId); //tem que criar no repository pq não é um metodo pronto
	}
	
	@Override
	public CourseUserModel save(CourseUserModel courseUserModel) { //jpa não esta funcionando
        return courseUserRepository.save(courseUserModel);
  }
    @Transactional //garantido o rollback se der algo errado
	@Override
	public CourseUserModel saveAndSendSubscriptionUserIntoCourse(CourseUserModel courseUserModel) {
    	//salvando o courseUserModel
    	courseUserModel = courseUserRepository.save(courseUserModel);
    	// metodo avisando o corse do salvamento da incricao
    	authUserClient.postSubscriptionInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserID());
    	return courseUserModel;
	}

	@Override
	public boolean existbyUserId(UUID userId) {
		return courseUserRepository.existByUserId(userId);
	}
    
	@Transactional
	@Override
	public void deleteCourseUserByUser(UUID userId) {
		courseUserRepository.deleteAllByUser(userId);
		
	}
}
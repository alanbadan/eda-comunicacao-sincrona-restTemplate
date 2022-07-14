package com.ead.curso.services;

import java.util.UUID;

import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;

public interface CourseUserService {
	
	//metodo para verificar se um aluno esta ou não matriculado em um curso
	boolean existByCourseAndCourse(CourseModel courseModel, UUID userId);
   
	//essa assonatura de metdo não é atomica
	CourseUserModel save(CourseUserModel courseUserModel);

	CourseUserModel saveAndSendSubscriptionUserIntoCourse(CourseUserModel courseUserModel);

	boolean existbyUserId(UUID userId);

	void deleteCourseUserByUser(UUID userID);

}


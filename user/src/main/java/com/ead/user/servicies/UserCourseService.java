package com.ead.user.servicies;

import java.util.UUID;

import com.ead.user.model.UserCourseModel;
import com.ead.user.model.UserModel;

//implemnat a comunicacao sincrona entre user e course


public interface UserCourseService {

	
	boolean existByUserAndCourseId(UserModel userModel, UUID courseId);

	UserCourseModel save(UserCourseModel userCourseModel);

	boolean existByCourseId(UUID courseId);

	void deleteUserCourseByCourse(UUID courseId);

}

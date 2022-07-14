package com.ead.user.servicies.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ead.user.model.UserCourseModel;
import com.ead.user.model.UserModel;
import com.ead.user.repository.UserCourseRepository;
import com.ead.user.servicies.UserCourseService;

//implemnat a comunicacao sincrona entre user e course
@Service
public class UserCourseServiceImpl implements UserCourseService {
	
	@Autowired
	UserCourseRepository userCourseRepository;

	@Override
	//tem que dclerar dentro do rEPOSITROR pq é customizado
	public boolean existByUserAndCourseId(UserModel userModel, UUID courseId) {
		return userCourseRepository.existByUserAndCourseId(userModel, courseId);
	}

	@Override
	public UserCourseModel save(UserCourseModel userCourseModel) {
		return userCourseRepository.save(userCourseModel);
	}

	@Override
	public boolean existByCourseId(UUID courseId) {
		return userCourseRepository.existByCourseId(courseId);
	}
    
	@Transactional //para ser atomica ( se der algo errado ele volta os dados antigos)
	@Override
	public void deleteUserCourseByCourse(UUID courseId) {
		userCourseRepository.deleteAllByCourseId(courseId);
		
	}

}

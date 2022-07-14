package com.ead.user.servicies.impl;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ead.user.client.CourseClient;
import com.ead.user.model.UserCourseModel;
import com.ead.user.model.UserModel;
import com.ead.user.repository.UserCourseRepository;
import com.ead.user.repository.UserRepository;
import com.ead.user.servicies.UserService;


@Service
public class UserServiceImpl implements UserService{
	

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserCourseRepository userCourseRepository;
	
	@Autowired
	CourseClient courseClient;
	

	@Override
	public List<UserModel> findAll() {
		
		return userRepository.findAll();
	}
	
	@Transactional
	@Override // sempre se atentar para relaão de inconscistencia de dados ( os dois micro services devem deletar sua realçoes)
	           // aqui delecao userId com courseId e no outro lada tn courseId para com userId
	public void delete(UserModel userModel) { //delete em cascata personalizaso ou pode uasr anotações jpa 
		// variavel para verificar se ha relacao usuari com course(ms Course)
		boolean deleteUserCourseIntoCourse = false;
		List<UserCourseModel> userCourseModelList = userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());
				if(!userCourseModelList.isEmpty()) {
					userCourseRepository.deleteAll(userCourseModelList);
				}
		userRepository.delete(userModel);
		if(deleteUserCourseIntoCourse) {
			courseClient.deleteUserIntoCourse(userModel.getUserId());
		}
	}

	@Override
	public Optional<UserModel> findById(UUID userId) {
     	return userRepository.findById(userId);
     	
	}

	@Override
	public void save(UserModel userModel) {
		userRepository.save(userModel);
		
	}

	@Override
	public boolean existByUserName(String userName) {
	
		return userRepository.existsByUserName(userName);//declarado metodo na interface no userrepository para verificar se ha cadastro
	}

	@Override
	public boolean existByEmail(String email) {
		
		return userRepository.existsByEmail(email); // declarado na interface repository
	}

	@Override
	public Page<UserModel> findAll(Specification<UserModel>spec, Pageable pageable) {
	
		return userRepository.findAll(spec, pageable);
	}

}

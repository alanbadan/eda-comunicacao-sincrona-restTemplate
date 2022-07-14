package com.ead.user.servicies;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.user.model.UserModel;

public interface UserService {

	List<UserModel> findAll();

	Optional<UserModel> findById(UUID userId);

	void delete(UserModel userModel);

	void save(UserModel userModel);

	boolean existByUserName(String userName);

	boolean existByEmail(String email);

	Page<UserModel> findAll(Specification<UserModel>spec, Pageable pageable);

	

	

}

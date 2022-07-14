package com.ead.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.user.model.UserCourseModel;
import com.ead.user.model.UserModel;

//intreface para comunicacai sincrona entre user e course
public interface UserCourseRepository extends JpaRepository<UserCourseRepository, UUID> {

	//metodo para verificar se existe uma incrição de um usuario e um curso
	boolean existByUserAndCourseId(UserModel userModel, UUID courseId);
	
	@Query(value = "select * from tb_user_course where user_user_id = :userId", nativeQuery = true)
	List<UserCourseModel> findAllUserCourseIntoUser(@Param("userId") UUID userId);
    
	//metodo para delecao
	boolean existByCourseId(UUID courseId);
	
	//deleta todas as linhas desse course
	void deleteAllByCourseId(UUID courseId);
	
}

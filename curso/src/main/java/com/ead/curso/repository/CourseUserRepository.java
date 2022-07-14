package com.ead.curso.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

	boolean existByCourseAndUserId(CourseModel courseModel, UUID userId);

	@Query(value = "select * from tb_course_user where couser_course_id = :courseId", nativeQuery = true)
	List<CourseUserModel> findAllCourseUserIntoCourse(@Param("courseId") UUID courseId);

	boolean existByUserId(UUID userId);

	void deleteAllByUser(UUID userId);
}

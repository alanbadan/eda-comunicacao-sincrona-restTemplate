package com.ead.curso.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CourseUserDto { //dto para retorno da requisoa Post no metodo save

	private UUID userId;
	private UUID courseId;
	
	
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public UUID getCourseId() {
		return courseId;
	}
	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}
	
}

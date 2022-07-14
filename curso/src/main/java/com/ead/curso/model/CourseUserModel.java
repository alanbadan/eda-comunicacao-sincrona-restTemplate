package com.ead.curso.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
//tabela intermediaria para comnicacao sincrona com user
@Table(name = "tb_course_user")
public class CourseUserModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false)
	private UUID userID; //associacao deum determinado curso com o usuario
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private CourseModel course; // couser cahve estrangeira
	
	// esses contritores com e sem argumentos estão sendo usadaos pq o lombok não esta funcionando senão usaria as anotacao do mesmo
	//construtores para o metodo de conversão Dto  na class CourseModel
	public CourseUserModel() {
		
	}
	public CourseUserModel(UUID id, UUID userID, CourseModel course) {
		super();
		this.id = id;
		this.userID = userID;
		this.course = course;
	}
	//Lombok não funciona
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getUserID() {
		return userID;
	}
	public void setUserID(UUID userID) {
		this.userID = userID;
	}
	public CourseModel getCourse() {
		return course;
	}
	public void setCourse(CourseModel course) {
		this.course = course;
	}

	
	
	
}

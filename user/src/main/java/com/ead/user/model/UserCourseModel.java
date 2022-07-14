package com.ead.user.model;

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
@NoArgsConstructor //por causa do metod de conversao no UserModel
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity                          //tabeçla intermediaria quando um usuario cadsatra e para que curso ele esta cadastrado
@Table(name = "tb_user_course") //tabela para relacionamento para comunicacao sincrino entre as apis de user e course
public class UserCourseModel  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false)
	private UUID courseId; //esse dado vem da tabela de course 
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private UserModel user; //relacionamento da tabela com a tabela userModel(o ususraio pode estar cadastrado em varios cursos)
	                         //mapeando o user como chave estrangeira
	
	
	//construtoer por causa do lombok não esta funcionando
	public UserCourseModel () {
		
	}
	public UserCourseModel(UUID id, UUID courseId, UserModel user) {
		super();
		this.id = id;
		this.courseId = courseId;
		this.user = user;
	}

	
	
}

package com.ead.user.specification;

import java.util.UUID;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.ead.user.model.UserCourseModel;
import com.ead.user.model.UserModel;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate { // classe para especifiacr os filtros dos templates

	//interface pra usar os filtro do specification
//	@Or  // paga um ou outro( email ou status ...)
	@And({             //anotacao and faz combinacao.
	     @Spec(path ="userType", spec = Equal.class), // filtro usertype(enun) ,o tipo retornado o valor extao do filtro (nocaso Equal) 
	     @Spec(path ="userStatus", spec = Equal.class),
	     @Spec(path ="email", spec = Like.class), 
	     @Spec(path = "fullName", spec = Like.class) // perquisa pelo nome 
	})
	public interface UserSpec extends Specification<UserModel>{} // sepcfication do srpingJpa
		
		
	//pesquisa para comunicacao sincrona de course para user um Join simple com cirteriabuilder
	//diferente do outro spec do course , aqui não retorna uma colecao
		public static Specification<UserModel> userCourseId(final UUID courseId){
			return (root, query, cb) ->{
				query.distinct(true);
				Join<UserModel, UserCourseModel> userProd = root.join("userCourse");
				return cb.equal(userProd.get("courseId"), courseId); //o courseId tem que igual ao parameytro do metodo 
				//select * all tb user_model join tb_user_course_model when courseId equal "courseId"; recebido no parametro
			
		};
		
	}
}

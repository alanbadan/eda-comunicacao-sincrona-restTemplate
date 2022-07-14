package com.ead.curso.specification;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;
import com.ead.curso.model.LessonModel;
import com.ead.curso.model.ModuleModel;


import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {
	
	@And({             
	     @Spec(path ="courseLevel", spec = Equal.class),  //sempre usar Equals para enuns
	     @Spec(path ="courseStatus", spec = Equal.class),
	     @Spec(path ="name", spec = Like.class) 
	})
	public interface CourseSpec extends Specification<CourseModel>{} // sepcfication do srpingJpa
			
		
	@Spec(path = "title", spec = Like.class)	
    public interface ModuleSpec extends Specification<ModuleModel>{}
	
	@Spec(path = "title", spec = Like.class)
	public interface LessonSpec extends Specification<LessonModel>{}
	
	
	
	//aula filtros avançados com apis
	//jpa Criteria Builder
	//realcao modulo curso
	//metdod para combinacao de consulta pelo spec um determinado ID com os filros do specification
	 public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {//pesquisando um lista de modulos determinado pelo cursoiD 
	        return (root, query, cb) -> {
	            query.distinct(true);//definicao sem valores duplicados
	            Root<ModuleModel> module = root;
	            Root<CourseModel> course = query.from(CourseModel.class);
	            Expression<Collection<ModuleModel>> courseModules = course.get("module");
	            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, courseModules));
	        };
	    }
	 //relacaso lesson module
	 public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {//pesquisando um lista de modulos determinado pelo cursoiD 
	        return (root, query, cb) -> {
	            query.distinct(true);//definicao sem valores duplicados
	            Root<LessonModel> lesson = root;
	            Root<ModuleModel> module = query.from(ModuleModel.class);
	            Expression<Collection<LessonModel>> moduleLesson = module.get("lesson");
	            return cb.and(cb.equal(module.get("courseId"), moduleId), cb.isMember(lesson, moduleLesson));
	        };
	    }
	
	 
	    //pesquisa para comunicacao sincrona de user para course um Join simple com cirteriabuilder
		//diferente do outro spec do course , aqui não retorna uma colecao
			public static Specification<CourseModel> CourseUserId(final UUID userId){
				return (root, query, cb) ->{
					query.distinct(true);
					Join<CourseModel, CourseUserModel> courseProd = root.join("courseUser");
					return cb.equal(courseProd.get("userId"), userId); //o courseId tem que igual ao parameytro do metodo 
					//select * all tb course_model join tb_course_user_model when userId equal "userId"; recebido no parametro
				
			};
			
		}
	 
	 
	 
	}

/*
  public static Specification<EntityA> moduleCourseId(final UUID id) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<EntityA> a = root;
            Root<EntityB> b = query.from(EntityB.class);
            Expression<Collection<EntityA>> colecttionBA = b.get("collection");
            return cb.and(cb.equal(b.get("id"), id), cb.isMember(a, colecttionBA));
        };
    }
 */
  

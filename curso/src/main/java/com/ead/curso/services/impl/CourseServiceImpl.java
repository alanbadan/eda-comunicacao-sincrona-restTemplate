package com.ead.curso.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.WebAsyncUtils;

import com.ead.curso.cliente.AuthUserClient;
import com.ead.curso.model.CourseModel;
import com.ead.curso.model.CourseUserModel;
import com.ead.curso.model.LessonModel;
import com.ead.curso.model.ModuleModel;
import com.ead.curso.repository.CourseRepository;
import com.ead.curso.repository.CourseUserRepository;
import com.ead.curso.repository.LessonRepository;
import com.ead.curso.repository.ModuleRepositroy;
import com.ead.curso.services.CourseService;
import com.ead.curso.specification.SpecificationTemplate;


@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	ModuleRepositroy moduleRepositroy;
	
	@Autowired
	LessonRepository lessonRepository;
	
	@Autowired
	CourseUserRepository courseUserRepository;
	
	@Autowired
	AuthUserClient authUserClient;
	
	@Transactional // anaotacao para se caso de errado ele volta ao estado natural que estava.
	@Override
	public void delete(CourseModel courseModel) {// metodo para delecao de modulos em um curso sem usar as anotaçoes cascade.all
		//variavel para verificacao se ha relacao de couseId com userId (caso não tenha não precisa enviar a requisicao para ms User e pose deletar o course
		boolean deleteCourseUserIntoUser = false;
		//recbeno o modulos // e deltando todos relacionamentos jpa
		List<ModuleModel> moduleModelList = moduleRepositroy.findAllModuleIntoCourse(courseModel.getCourseId());
		if(!moduleModelList.isEmpty()) {
			for(ModuleModel module : moduleModelList) { //percorrendo a lista para ver as lesson dos module
				List<LessonModel> lessonModelList = lessonRepository.findAllLessonIntoModule(module.getModuleId());
				 if(!lessonModelList.isEmpty()) {
					 lessonRepository.deleteAll(lessonModelList); //se não passar o parametro lel deleta tudo !!!!!!!!
				 }
			}
			moduleRepositroy.deleteAll(moduleModelList);
		}
		//delecao em cascata de um deteriminado curso 
		List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
		if(!courseUserModelList.isEmpty()) {
			courseUserRepository.deleteAll(courseUserModelList);
			deleteCourseUserIntoUser = true; //se exixtir alguma relacao na lista de userModel a variavel se trona true e se faz a requisicao para user
		}
		courseRepository.delete(courseModel); //deletando o curso
		//verificandi se vai fazer o unvuio ou não.
		if(deleteCourseUserIntoUser) {
			//acessando o Client e criando um novo metodo de delete
			authUserClient.deleteCourseIntoUser(courseModel.getCourseId()); //passando o coureId que foi deletado
			
		}
	}

	@Override
	public CourseModel save(CourseModel courseModel) {
		return courseRepository.save(courseModel);
	}

	@Override
	public Optional<CourseModel> findById(UUID courseId) {
		
		return courseRepository.findById(courseId);
	}

	@Override
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
		return courseRepository.findAll(spec, pageable); // retornando a listagem de cursos
	}

}

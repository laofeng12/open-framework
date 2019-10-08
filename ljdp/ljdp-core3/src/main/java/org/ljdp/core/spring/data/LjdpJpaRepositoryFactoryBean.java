package org.ljdp.core.spring.data;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class LjdpJpaRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I>  {
	
	
	
	public LjdpJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {

		return new LjdpJpaRepositoryFactory(entityManager);
	}

	private static class LjdpJpaRepositoryFactory<T, I extends Serializable> extends
			JpaRepositoryFactory {

//		private EntityManager entityManager;

		public LjdpJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);

//			this.entityManager = entityManager;
		}

		protected JpaRepositoryImplementation<?, ?>  getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
			//获取继承的接口
			Class[] ifs = information.getRepositoryInterface().getInterfaces();
			for (int i = 0; i < ifs.length; i++) {
				//如果存在自定义的接口，返回自定义的实现
				if(ifs[i].equals(MultiDynamicJpaRepository.class)) {
					return new MultiDynamicJpaRepositoryImpl<T, I>(
							(Class<T>) information.getDomainType(), entityManager);
				}
				if(ifs[i].equals(DynamicJpaRepository.class)) {
					return new DynamicJpaRepositoryImpl<T, I>(
							(Class<T>) information.getDomainType(), entityManager);
				}
			}
			return super.getTargetRepository(information, entityManager);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			// The RepositoryMetadata can be safely ignored, it is used by the
			// JpaRepositoryFactory
			// to check for QueryDslJpaRepository's which is out of scope.
			
			//获取继承的接口
			Class[] ifs = metadata.getRepositoryInterface().getInterfaces();
			for (int i = 0; i < ifs.length; i++) {
				//如果存在自定义的接口，返回自定义的接口
				if(ifs[i].equals(MultiDynamicJpaRepository.class)) {
					return MultiDynamicJpaRepository.class;
				}
				if(ifs[i].equals(DynamicJpaRepository.class)) {
					return DynamicJpaRepository.class;
				}
			}
			
			return super.getRepositoryBaseClass(metadata);
		}
	}
}

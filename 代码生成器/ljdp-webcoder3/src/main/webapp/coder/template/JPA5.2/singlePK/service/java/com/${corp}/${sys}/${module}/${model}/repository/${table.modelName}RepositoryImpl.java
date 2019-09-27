<#if model == "">
package com.${corp}.${sys}.${module}.repository;
<#else>
package com.${corp}.${sys}.${module}.${model}.repository;
</#if>

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ${table.modelName}RepositoryImpl implements ${table.modelName}RepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}

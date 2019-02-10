    // ${resName}
	{
        path: '/${module}',
        icon: 'table',
        name: '${module}',
        authority: ['请改为菜单的id'],
        routes: [
          {
            path: '/${module}/${table.modelName2}',
            name: '${table.modelName2}list',
            authority: ['请改为菜单的id'],
            routes: [
              {
                path: '/${module}/${table.modelName2}',
                redirect: '/${module}/${table.modelName2}/list',
              },
              {
                path: '/${module}/${table.modelName2}/list',
                component: './${module}/${table.modelName}List',
              },
              <#if baseFun.importFun == "on">
              {
                path: '/${module}/${table.modelName2}/batch',
                component: './${module}/${table.modelName}Batch',
              },
              </#if>
              {
                path: '/${module}/${table.modelName2}/:pid',
                component: './${module}/${table.modelName}Profile',
              },
            ]
          }
        ]
      },
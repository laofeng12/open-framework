    '/${module}/${table.modelName2}-list': {
      component: dynamicWrapper(app, ['${module}/${table.modelName2}', 'dictionary'], () => import('../routes/${module}/${table.modelName}List')),
      name: '${resName}',
      authority: [ 'clouduser'],
    },
    '/${module}/${table.modelName2}-profile/:pid': {
      component: dynamicWrapper(app, ['${module}/${table.modelName2}'], () => import('../routes/${module}/${table.modelName}Profile')),
      name: '${tableModelName}详情',
      authority: ['clouduser'],
    },
    <#if baseFun.add == "on">
    '/${module}/${table.modelName2}-form/:opertype/:pid': {
        component: dynamicWrapper(app, ['${module}/${table.modelName2}'], () => import('../routes/${module}/${table.modelName}Edit')),
        name: '${tableModelName}编辑',
        authority: ['clouduser'],
    },
    </#if>
    <#if baseFun.importFun == "on">
    '/${module}/${table.modelName2}-batch': {
      component: dynamicWrapper(app, ['${module}/${table.modelName2}'], () => import('../routes/${module}/${table.modelName}Batch')),
      name: '${tableModelName}批量导入',
      authority: ['clouduser'],
    },
    </#if>
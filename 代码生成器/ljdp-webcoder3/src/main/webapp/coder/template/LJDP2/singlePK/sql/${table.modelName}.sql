INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysResId},'${resName}',&输入父菜单ID,1,'${sys}/${module}/${model}/${table.modelName}/mainPage.action',10,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '查询',${sysResId},2,'queryBtn',1,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '新增',${sysResId},2,'addBtn',2,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '编辑',${sysResId},2,'editBtn',3,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '删除',${sysResId},2,'deleteBtn',4,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '导出',${sysResId},2,'exportBtn',5,1,${sysId});

INSERT INTO tsys_res(resid,resname,presid,restype,resurl,resprio,resenabled,sysid)
VALUES (${sysId}||seq_sys_res.nextval, '导入',${sysResId},2,'importBtn',6,1,${sysId});


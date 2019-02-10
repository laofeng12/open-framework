<#assign d = "$"/>
import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import moment from 'moment';
import {
  Row,
  Col,
  Card,
  Form,
  Input,
  Button,
  InputNumber,
  DatePicker,
  Modal,
  message,
  Divider,
} from 'antd';
import StandardTable from '@/components/StandardTable';
import DictSelect from '@/components/DictSelect';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
<#if baseFun.export == "on">
import { getAuthorityToken } from '@/utils/authority';
</#if>

import styles from '../List/TableList.less';

const FormItem = Form.Item;
const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');

<#if baseFun.add == "on">
const CreateForm = Form.create({
  mapPropsToFields(props) {
	const {
		<#list table.columnList as item>
    	<#if item.javaDataType == "Long" || item.javaDataType == "Integer" || item.javaDataType == "Short">
    	<#if item.userDict == true>
    	<#else>
    	${item.columnName},
    	</#if>
    	<#elseif item.javaDataType == "Date">
    	<#else>
    	${item.columnName},
    	</#if>
    	</#list>
	} = props.domain;
    let { 
    	<#list table.columnList as item>
    	<#if item.javaDataType == "Long" || item.javaDataType == "Integer" || item.javaDataType == "Short">
    	<#if item.userDict == true>
    	${item.columnName},
    	</#if>
    	<#elseif item.javaDataType == "Date">
    	${item.columnName},
    	</#if>
    	</#list>
    	} = props.domain;
	<#list table.columnList as item>
	<#if item.javaDataType == "Long" || item.javaDataType == "Integer" || item.javaDataType == "Short">
	<#if item.userDict == true>
	if (${item.columnName} !== undefined && ${item.columnName} !== null) {
		${item.columnName} = ${item.columnName}.toString();
	}
	</#if>
	<#elseif item.javaDataType == "Date">
	if(${item.columnName}){
		${item.columnName} = moment(props.domain.${item.columnName}, 'yyyy-MM-dd HH:mm:ss');
	}
	</#if>
	</#list>
    return {
    	<#list table.columnList as item>
    	<#if item.userDict == true && (item.javaDataType == "Long" || item.javaDataType == "Integer" || item.javaDataType == "Short")>
    	${item.columnName}: Form.createFormField({
    		value: ${item.columnName},
    	}),
    	<#elseif item.javaDataType == "Date">
    	${item.columnName}: Form.createFormField({
    		value: ${item.columnName},
    	}),
    	<#else>
    	${item.columnName}: Form.createFormField({
    		value: ${item.columnName},
    	}),
    	</#if>
		</#list>
    };
  },
})(props => {
  const { modalVisible, form, handleSave, handleModalVisible, dictionary, isNew } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleSave(fieldsValue);
    });
  };

  return (
    <Modal
      width={1024}
      title="${resName}"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
    <#list table.rowCloumnList as rows>
    <Row>
	<#list rows as item>
		<Col md={12} sm={24}>
		<#if item.userDict == true>
	    <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="${item.comment}">
	      {form.getFieldDecorator('${item.columnName}', {
	          rules: [{ required: true, message: '请输入${item.comment}' }],
	        })(
	          <DictSelect
	            placeholder="请选择"
	            style={{ width: '100%' }}
	            dictList={dictionary['${item.dictDefined}']}
	          />
	        )}
	    </FormItem>
        <#elseif item.iskey == true>
        
        <#elseif item.extJsFieldType=='datefield'>
	    <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="${item.comment}">
        	{form.getFieldDecorator('${item.columnName}',{
        		rules: [{ required: false }],
    	        initialValue: '',
        		})(
	                <DatePicker
	                  style={{ width: '100%' }}
	                  format="YYYY-MM-DD HH:mm:ss"
	                  showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
	                />
	              )}
	    </FormItem>
		<#else>
	    <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="${item.comment}">
	    	{form.getFieldDecorator('${item.columnName}', {
		        rules: [{ required: true, message: '请输入${item.comment}' }],
		        initialValue: '',
		      })(<Input placeholder="请输入" />)}
	    </FormItem>
		</#if>
		</Col>
	</#list>
    </Row>
	</#list>
    </Modal>
  );
});
</#if>

// ${resName}
@connect(({ ${table.modelName2}, dictionary, loading }) => ({
  ${table.modelName2},
  dictionary,
  loading: loading.models.${table.modelName2},
}))
@Form.create()
class ${table.modelName}List extends PureComponent {
  state = {
    modalVisible: false,
    selectedRows: [],
    formValues: {},
    isNew: true,
    editingKey: '',
  };

  componentDidMount() {
    const { dispatch, ${table.modelName2}: { data: { pagination: { pageSize } } } } = this.props;
    dispatch({
      type: '${table.modelName2}/fetch',
      payload: {
        size: pageSize
      }
    });
    
    <#list table.columnList as item>
    <#if item.userDict == true>    
    dispatch({
    	type: 'dictionary/loadDict',
    	codetype: '${item.dictDefined}',
    });
    </#if>
    </#list>
  }

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { dispatch } = this.props;
    const { formValues } = this.state;

    const filters = Object.keys(filtersArg).reduce((obj, key) => {
      const newObj = { ...obj };
      newObj[key] = getValue(filtersArg[key]);
      return newObj;
    }, {});

    const params = {
      ...formValues,
      ...filters,
      page: pagination.current - 1,
      size: pagination.pageSize,
    };
    if (sorter.field) {
      params.sorter = `${d}{sorter.field}_${d}{sorter.order}`;
    }

    dispatch({
      type: '${table.modelName2}/fetch',
      payload: params,
    });
  };

  handleFormReset = () => {
    const { form } = this.props;
    form.resetFields();
    this.doSearch();
  };

  <#if baseFun.delete == "on">
  handleRemove = (id) => {
    const { dispatch } = this.props;
    const { selectedRows } = this.state;

    // if (!selectedRows) return;
    const ids = id || selectedRows.map(row => row.${table.keyField}).join(',');
    if(!ids) return;
    const onOkf = () => {
      dispatch({
        type: '${table.modelName2}/remove',
        payload: {
          ids,
        },
        callback: () => {
          this.setState({
            selectedRows: [],
          });
          message.info('已成功删除');
          this.doSearch();
        },
      });
    };
    Modal.confirm({
      title: '删除',
      content: '确定永久删除选定的记录吗？',
      okText: '确定删除',
      okType: 'danger',
      cancelText: '取消',
      onOk: onOkf,
      onCancel() {
        // console.log('Cancel');
      },
    });
  };
  </#if>

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  doSearch = () => {
    const { dispatch, form, ${table.modelName2}: { data: { pagination: { pageSize } } } } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        size: pageSize,
        ...fieldsValue,
        <#list rowDbParamList as rows>
        <#list rows as item>
        <#if item.extJsFieldType=='datefield'>
        ${item.condition}_${item.columnName}: fieldsValue.${item.condition}_${item.columnName} && fieldsValue.${item.condition}_${item.columnName}.format('YYYY-MM-DD HH:mm:ss'),
        </#if>
        </#list>
        </#list>
      };

      this.setState({
        formValues: values,
      });

      dispatch({
        type: '${table.modelName2}/fetch',
        payload: values,
      });
    });
  };

  handleSearch = e => {
    e.preventDefault();

    this.doSearch();
  };
  
  <#if baseFun.export == "on">
  handleExport = e => {
    e.preventDefault();
    const { form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const fvalues = {
        ...fieldsValue,
        <#list rowDbParamList as rows>
        <#list rows as item>
        <#if item.extJsFieldType=='datefield'>
        ${item.condition}_${item.columnName}: fieldsValue.${item.condition}_${item.columnName} && fieldsValue.${item.condition}_${item.columnName}.format('YYYY-MM-DD HH:mm:ss'),
        </#if>
        </#list>
        </#list>
      };

      let params = '';
//      for (const v in fvalues) {
//        if (values[v] !== null && fvalues[v] !== undefined) {
//          params += `$d{v}=$d{fvalues[v]}&`;
//        }
//      }
      Object.entries(fvalues).forEach(item => {
        const key = item[0];
        const value = item[1];
        if (value !== null && value !== undefined) {
          params += `${d}{key}=${d}{value}&`;
        }
      });
      
      open(`/api/${sys}/${module}/${table.modelName2}/export?${d}{params}tokenid=${d}{getAuthorityToken()}`);
    });
  };
  </#if>
  
  <#if baseFun.importFun == "on">
  handleImport = () => {
    this.props.dispatch(routerRedux.push('/${module}/${table.modelName2}/batch'));
  };
  </#if>

  handleShow = (e, key) => {
    const { dispatch } = this.props;
    dispatch(routerRedux.push(`/${module}/${table.modelName2}/${d}{key}`));
  };

  <#if baseFun.add == "on">
  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  handleAdd2 = () => {
    this.props.dispatch({
      type: '${table.modelName2}/clearDomain',
    });
    this.handleModalVisible(true);
    this.setState({
      isNew: true,
      editingKey: '',
    });
  };

  handleEdit2 = (e, key) => {
    this.props.dispatch({
      type: '${table.modelName2}/loadDomain',
      payload: key,
      callback: () => {
        this.handleModalVisible(true);
        this.setState({
          isNew: false,
          editingKey: key,
        });
      },
    });
  };

  handleSave = fields => {
    const { isNew, editingKey } = this.state;
    
    let datefields = {};
    <#list table.columnList as item>
    <#if item.javaDataType == "Date">
	if(fields.${item.columnName} != undefined){
    	datefields.${item.columnName} = fields.createtime.format('YYYY-MM-DD HH:mm:ss');
	} else{
		datefields.${item.columnName} = '';
	}
    </#if>
    </#list>
    
    this.props.dispatch({
      type: '${table.modelName2}/save',
      payload: {
        ...fields,
        isNew,
        ${table.keyField}: editingKey,
        ...datefields,
      },
      callback: response => {
        if (response.code === 200) {
          this.doSearch();
          message.success('保存成功');
        } else {
          message.success('保存失败：[' + response.code + ']' + response.message);
        }
      },
    });
    this.setState({
      modalVisible: false,
    });
  };
  </#if>

  renderAdvancedForm() {
    const { getFieldDecorator } = this.props.form;
    const { dictionary } = this.props;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
      <#list rowDbParamList as rows>
      <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
      <#list rows as item>
      	<Col md={6} sm={24}>
      	<FormItem label="${item.name}${item.symbol}">
      	<#if item.extJsFieldType=='combo'>
	        {getFieldDecorator('${item.condition}_${item.columnName}')(
	          <Select placeholder="请选择" style={{ width: '100%' }}>
	            <Option value="">==请选择==</Option>
	            <Option value="true">空</Option>
	            <Option value="false">非空</Option>
	          </Select>
	        )}
      	<#elseif item.extJsFieldType=='dictCombo'>
      		{getFieldDecorator('${item.condition}_${item.columnName}')(
                <DictSelect
                  placeholder="请选择"
                  style={{ width: '100%' }}
                  dictList={dictionary['${item.dictDefined}']}
                />
              )}
      	<#elseif item.extJsFieldType=='datefield'>
      		{getFieldDecorator('${item.condition}_${item.columnName}')(
                <DatePicker
                  style={{ width: '100%' }}
                  format="YYYY-MM-DD HH:mm:ss"
                  showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
                  placeholder="选择日期"
                />
              )}
      	<#else>
      		{getFieldDecorator('${item.condition}_${item.columnName}')(<Input placeholder="请输入" />)}
      	</#if>
      	</FormItem>
      	</Col>
      </#list>
      </Row>
      </#list>
        <div style={{ overflow: 'hidden' }}>
          <span style={{ float: 'right', marginBottom: 24 }}>
            <Button icon="search" type="primary" htmlType="submit">
              查询
            </Button>
            <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
              重置
            </Button>
            <#if baseFun.export == "on">
            <Button icon="export" style={{ marginLeft: 8 }} onClick={this.handleExport}>
              导出
            </Button>
            </#if>
            <#if baseFun.importFun == "on">
            <Button icon="cloud-download-o" style={{ marginLeft: 8 }} onClick={this.handleImport}>
              导入
            </Button>
            </#if>
          </span>
        </div>
      </Form>
    );
  }

  renderForm() {
    return this.renderAdvancedForm();
  }

  render() {
    const { ${table.modelName2}: { data, domain }, dictionary, loading } = this.props;
    const { selectedRows, modalVisible, isNew } = this.state;

    const columns = [
	<#list table.columnList as item>
    <#if item.userDict == true>
      {
        title: '${item.comment}',
        dataIndex: '${item.columnName}Name',
      },
    <#else>
      {
        title: '${item.comment}',
        dataIndex: '${item.columnName}',
      },
    </#if>
    </#list>
      {
        title: '操作',
        render: (text, record) => (
          <Fragment>
          <#if baseFun.add == "on">
            <a onClick={e => this.handleEdit2(e, record.${table.keyField})}>编辑</a>
          </#if>
            <Divider type="vertical" />
            <a onClick={e => this.handleShow(e, record.${table.keyField})}>查看</a>
          </Fragment>
        ),
      },
    ];
    <#if baseFun.add == "on">
    const parentMethods = {
      handleSave: this.handleSave,
      handleModalVisible: this.handleModalVisible,
      dictionary,
      domain,
      isNew,
    };
    </#if>
    return (
      <PageHeaderWrapper>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>{this.renderForm()}</div>
            <div className={styles.tableListOperator}>
            <#if baseFun.add == "on">
              <Button icon="plus" type="primary" onClick={this.handleAdd2}>
                新建
              </Button>
            </#if>
              {selectedRows.length > 0 && (
                <span>
                <#if baseFun.delete == "on">
                  <Button icon="minus" type="dashed" onClick={this.handleRemove}>
                    删除
                  </Button>
                </#if>
                </span>
              )}
            </div>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={data}
              columns={columns}
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
              rowKey="${table.keyField}"
            />
          </div>
        </Card>
        <#if baseFun.add == "on">
        <CreateForm {...parentMethods} modalVisible={modalVisible} />
        </#if>
        </PageHeaderWrapper>
    );
  }
}
export default ${table.modelName}List;
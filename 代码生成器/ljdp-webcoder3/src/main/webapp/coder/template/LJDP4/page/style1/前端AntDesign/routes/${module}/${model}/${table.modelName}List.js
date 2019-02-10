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
import StandardTable from 'components/StandardTable';
import DictSelect from 'components/DictSelect';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
<#if baseFun.export == "on">
import { getAuthorityToken } from '../../utils/authority';
</#if>

import styles from '../List/TableList.less';

const FormItem = Form.Item;
const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');


@connect(({ ${table.modelName2}, dictionary, loading }) => ({
  ${table.modelName2},
  dictionary,
  loading: loading.models.${table.modelName2},
}))
@Form.create()
export default class ${table.modelName}List extends PureComponent {
  state = {
    selectedRows: [],
    formValues: {},
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: '${table.modelName2}/fetch',
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
      page: pagination.current - 1,
      size: pagination.pageSize,
      ...formValues,
      ...filters,
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
    const { form, dispatch } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    dispatch({
      type: '${table.modelName2}/fetch',
      payload: {},
    });
  };

  <#if baseFun.delete == "on">
  handleRemove = () => {
    const { dispatch } = this.props;
    const { selectedRows } = this.state;

    if (!selectedRows) return;
    const onOkf = () => {
      dispatch({
        type: '${table.modelName2}/remove',
        payload: {
          ids: selectedRows.map(row => row.${table.keyField}).join(','),
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
    const { dispatch, form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        <#list rowDbParamList as rows>
        <#list rows as item>
        <#if item.extJsFieldType=='datefield'>
        ${item.condition}_${item.columnName}: fieldsValue.${item.condition}_${item.columnName} && fieldsValue.${item.condition}_${item.columnName}.format('YYYY-MM-DD HH:mm:ss'),
        </#if>
        </#list>
        </#list>
      };

      // console.log(values)

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

      const values = {
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
      for (const v in values) {
        if (values[v] !== null && values[v] !== undefined) {
          params += `$d{v}=$d{values[v]}&`;
        }
      }
      
      open(`/api/${sys}/${module}/${table.modelName2}/export?${d}{params}tokenid=${d}{getAuthorityToken()}`);
    });
  };
  </#if>
  <#if baseFun.importFun == "on">
  handleImport = () => {
	const { dispatch } = this.props;
    dispatch(routerRedux.push('/${module}/${table.modelName2}-batch'));
  };
  </#if>

  handleShow = (e, key) => {
	const { dispatch } = this.props;
    dispatch(routerRedux.push(`/${module}/${table.modelName2}-profile/${d}{key}`));
  };

  <#if baseFun.add == "on">
  handleAdd = () => {
	const { dispatch } = this.props;
	dispatch(routerRedux.push(`/${module}/${table.modelName2}-form/add/0`));
  };
  
  handleEdit = (e, key) => {
	const { dispatch } = this.props;
	dispatch(routerRedux.push(`/${module}/${table.modelName2}-form/edit/${d}{key}`));
  };
  </#if>

  renderAdvancedForm() {
	const { dictionary, form } = this.props;
    const { getFieldDecorator } = form;
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
    const { selectedRows } = this.state;

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
            <a onClick={e => this.handleEdit(e, record.${table.keyField})}>编辑</a>
          </#if>
            <Divider type="vertical" />
            <a onClick={e => this.handleShow(e, record.${table.keyField})}>查看</a>
          </Fragment>
        ),
      },
    ];
    
    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>{this.renderForm()}</div>
            <div className={styles.tableListOperator}>
            <#if baseFun.add == "on">
              <Button icon="plus" type="primary" onClick={this.handleAdd}>
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
        </PageHeaderLayout>
    );
  }
}

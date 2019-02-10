import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import moment from 'moment';
import { Row, Col, Form, Input, Button, Card, message, DatePicker } from 'antd';
import DictSelect from 'components/DictSelect';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
<#if baseFun.add == "on">
@connect(({ ${table.modelName2}, dictionary, loading }) => ({
  ${table.modelName2},
  dictionary,
  submitting: loading.effects['${table.modelName2}/save'],
}))
@Form.create()
export default class ${table.modelName}Edit extends PureComponent {
  componentDidMount() {
    const {
      dispatch,
      match: { params },
    } = this.props;
    const { opertype, pid } = params;

    if (opertype === 'edit') {
      dispatch({
        type: '${table.modelName2}/loadDomain',
        payload: pid,
        callback: () => {},
      });
    }

    <#list table.columnList as item>
    <#if item.userDict == true>    
    dispatch({
    	type: 'dictionary/loadDict',
    	codetype: '${item.dictDefined}',
    });
    </#if>
    </#list>
  }
  
  handleSave = (fields, isNew, editingKey) => {
	const { dispatch } = this.props;
	let datefields = {};
    <#list table.columnList as item>
    <#if item.javaDataType == "Date">
	if(fields.${item.columnName} != undefined){
    	datefields.${item.columnName} = fields.${item.columnName}.format('YYYY-MM-DD HH:mm:ss');
	} else{
		datefields.${item.columnName} = '';
	}
    </#if>
    </#list>
	dispatch({
        type: '${table.modelName2}/save',
        payload: {
          ...fields,
          isNew,
          ${table.keyField}: editingKey,
          ...datefields,
        },
        callback: response => {
          if (response.code === 200) {
            message.success('保存成功');
            dispatch(routerRedux.push('/${module}/${table.modelName2}-list'));
          } else {
            message.success('保存失败：[' + response.code + ']' + response.message);
          }
        },
    });
  }

  handleSubmit = e => {
    e.preventDefault();
    const {
      form,
      match: { params },
    } = this.props;
    const { opertype, pid } = params;
    let isNew = true;
    if (opertype === 'edit') {
      isNew = false;
    }
    form.validateFieldsAndScroll((err, fieldsValue) => {
      if (!err) {
    	this.handleSave(fieldsValue, isNew, pid);
      }
    });
  };

  goback = () => {
    history.back();
  };

  render() {
    const {
      submitting,
      form,
      dictionary,
      match: { params },
      ${table.modelName2}: { domain },
    } = this.props;
    const { getFieldDecorator } = form;
    const { opertype } = params;
    let isNew = true;
    let {
		<#list table.columnList as item>
			${item.columnName},
		</#list>
		} = domain;
    if (opertype === 'edit') {
    	isNew = false;
	  	<#list table.columnList as item>
	  	<#if item.javaDataType == "Long" || item.javaDataType == "Integer" || item.javaDataType == "Short">
	  	<#if item.userDict == true>
	  	if (${item.columnName} != undefined && ${item.columnName} != null) {
	  		${item.columnName} = ${item.columnName}.toString();
	  	}
	  	</#if>
	  	<#elseif item.javaDataType == "Date">
	  	if(${item.columnName} != undefined && ${item.columnName} != null){
	  		${item.columnName} = moment(${item.columnName}, 'yyyy-MM-dd HH:mm:ss');
	  	}
	  	</#if>
	  	</#list>
    } else if (opertype === 'add') {
      	<#list table.columnList as item>
      		${item.columnName} = '';
		</#list>
    }

    const formItemLayout = {
      labelCol: {
        xs: { span: 5 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 15 },
        sm: { span: 15 },
        md: { span: 15 },
        lg: { span: 15 },
      },
    };

    const submitFormLayout = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 10, offset: 7 },
      },
    };

    const action = (
      <Fragment>
        <ButtonGroup>
          <Button icon="rollback" onClick={this.goback}>
            返回
          </Button>
        </ButtonGroup>
      </Fragment>
    );

    return (
      <PageHeaderLayout title="${resName}" content="" action={action}>
        <Card bordered={false}>
          <Form onSubmit={this.handleSubmit} hideRequiredMark style={{ marginTop: 8 }}>
          <#list table.rowCloumnList as rows>
          <Row>
      		<#list rows as item>
      		<Col md={12} sm={24}>
      		<#if item.userDict == true>
      	    <FormItem {...formItemLayout} label="${item.comment}">
      	      {form.getFieldDecorator('${item.columnName}', {
      	          rules: [{ required: true, message: '请输入${item.comment}' }],
      	          initialValue: ${item.columnName},
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
      	    <FormItem {...formItemLayout} label="${item.comment}">
              	{form.getFieldDecorator('${item.columnName}',{
              		rules: [{ required: false }],
              		initialValue: ${item.columnName},
              		})(
      	                <DatePicker
      	                  style={{ width: '100%' }}
      	                  format="YYYY-MM-DD HH:mm:ss"
      	                  showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
      	                />
      	              )}
      	    </FormItem>
      		<#else>
      	    <FormItem {...formItemLayout} label="${item.comment}">
      	    	{form.getFieldDecorator('${item.columnName}', {
      		        rules: [{ required: true, message: '请输入${item.comment}' }],
      		        initialValue: ${item.columnName},
      		      })(<Input placeholder="请输入" />)}
      	    </FormItem>
      		</#if>
      		</Col>
      		</#list>
          </Row>
      	  </#list>
      	  <Row>
	        <Col md={12} sm={24} />
	        <Col md={12} sm={24}>
	          <FormItem {...submitFormLayout} style={{ marginTop: 32 }}>
	            <Button type="primary" htmlType="submit" loading={submitting}>
	              提交
	            </Button>
	          </FormItem>
	        </Col>
	      </Row>
          </Form>
        </Card>
      </PageHeaderLayout>
    );
  }
}
<#else>
没有选新增，此文件可删除
</#if>
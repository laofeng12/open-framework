import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { Button, Card, Divider } from 'antd';
import DescriptionList from 'components/DescriptionList';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

const { Description } = DescriptionList;
const ButtonGroup = Button.Group;

@connect(({ ${table.modelName2}, loading }) => ({
	${table.modelName2},
  loading: loading.effects['${table.modelName2}/loadDomain'],
}))
export default class ${table.modelName}Profile extends Component {
  componentDidMount() {
    const { dispatch, match: { params } } = this.props;
    dispatch({
      type: '${table.modelName2}/loadDomain',
      payload: params.pid,
    });
  }

  goback = () => {
    history.back();
  };

  render() {
    const { ${table.modelName2}: { domain } } = this.props;
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
      <PageHeaderLayout title="${resName}" action={action}>
        <Card bordered={false}>
          <DescriptionList size="large" title="基础信息" style={{ marginBottom: 32 }}>
          <#list table.columnList as item>
          <#if item.userDict == true>
          	<Description term="${item.comment}">{domain.${item.columnName}Name}</Description>
          <#else>
          	<Description term="${item.comment}">{domain.${item.columnName}}</Description>
          </#if>
          </#list>
          </DescriptionList>
          <Divider style={{ marginBottom: 32 }} />
        </Card>
      </PageHeaderLayout>
    );
  }
}

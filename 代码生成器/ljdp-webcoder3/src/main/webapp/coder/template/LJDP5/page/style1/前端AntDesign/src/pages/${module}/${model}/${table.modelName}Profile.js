import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { Button, Card, Divider } from 'antd';
import router from 'umi/router';
import DescriptionList from '@/components/DescriptionList';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';

const { Description } = DescriptionList;
const ButtonGroup = Button.Group;


@connect(({ ${table.modelName2}, loading }) => ({
	${table.modelName2},
  loading: loading.effects['${table.modelName2}/loadDomain'],
}))
class ${table.modelName}Profile extends Component {
  componentDidMount() {
    const { dispatch, match: { params } } = this.props;
    dispatch({
      type: '${table.modelName2}/loadDomain',
      payload: params.pid,
    });
  }

  goback = () => {
    // history.back();
    router.goBack();
  };

  render() {
    const { ${table.modelName2}: { domain } } = this.props;
    const action = (
      <Fragment>
        <ButtonGroup>
          <Button type="primary" icon="rollback" onClick={this.goback}>
            返回
          </Button>
        </ButtonGroup>
      </Fragment>
    );

    return (
      <PageHeaderWrapper title="${resName}" action={action}>
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
      </PageHeaderWrapper>
    );
  }
}
  export default ${table.modelName}Profile;
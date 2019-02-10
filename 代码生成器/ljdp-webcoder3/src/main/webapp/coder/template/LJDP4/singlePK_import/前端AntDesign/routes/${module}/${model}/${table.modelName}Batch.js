import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { Button, Card, Row, Col, Upload, Icon, message, Modal } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

const ButtonGroup = Button.Group;

@connect(({ ${table.modelName2}, loading }) => ({
  ${table.modelName2},
  loading: loading.effects['${table.modelName2}/submitBatchTask'],
}))
export default class ${table.modelName}Batch extends Component {
  state = {
    fileList: [],
  };

  componentDidMount() {}

  goback = () => {
    history.back();
  };

  handleChange = info => {
    let { fileList } = info;

    // 1. Limit the number of uploaded files
    //    Only to show two recent uploaded files, and old ones will be replaced by the new
    fileList = fileList.slice(-1);

    // 2. read from response and show file link
    // fileList = fileList.map((file) => {
    //   if (file.response) {
    //     // Component will show file.url as link
    //     file.url = file.response.url;
    //   }
    //   return file;
    // });

    // 3. filter successfully uploaded files according to response from server
    fileList = fileList.filter(file => {
      if (file.response) {
        if (file.response.success) {
          file.fid = file.response.data;
          file.url = '/api/ljdp/attach/memory/view/' + file.response.data;
          return true;
        } else {
          message.info(file.response.msg);
        }
      }
      return true;
    });

    // console.log(fileList);

    this.setState({ fileList });
  };

  handleSubmitTask = () => {
    const { fileList } = this.state;
    if (fileList.length === 0) {
      message.info('请选择文件');
      return;
    }
    if (fileList.length > 1) {
      message.info('暂时只支持每次提交一个文件');
      return;
    }

    const hide = message.loading('正在提交任务...', 0);
    const { dispatch } = this.props;
    dispatch({
      type: '${table.modelName2}/submitBatchTask',
      payload: {
        fileId: fileList[0].fid,
        component: '${table.modelName2}BatchBO',
        batchType: '${resName}批量',
        submitParams: '', // 填写自定义参数,格式：param1=test1&params2=test2
      },
      callback: response => {
        hide();
        // console.log(response);
        if (response.success) {
          Modal.success({
            title: '提交成功',
            content: '文件已经成功提交至后台处理，点击确定去查看处理进度',
            okText: '查看进度',
            onOk: () => {
              open('http://网关:端口/page/filebatch/list.jspx');
            },
          });
        } else {
          Modal.error({
            title: '提交失败',
            content: '失败原因：' + response.msg,
          });
        }
      },
    });
  };

  render() {
    const { loading } = this.props;
    const { fileList } = this.state;
    const action = (
      <Fragment>
        <ButtonGroup>
          <Button icon="rollback" onClick={this.goback}>
            返回
          </Button>
        </ButtonGroup>
      </Fragment>
    );
    const uploadprops = {
      action: '/api/ljdp/attach/memory/upload.act?busiPath=${table.modelName2}',
      onChange: this.handleChange,
      multiple: true,
    };

    return (
      <PageHeaderLayout title="${resName}-批量导入" action={action}>
        <Card bordered={false} style={{ lineHeight: 2.5 }}>
          <Row gutter={0}>
            <Col span={3}>上传：</Col>
            <Col span={21}>
              <Upload {...uploadprops} fileList={fileList}>
                <Button>
                  <Icon type="upload" /> 选择文件
                </Button>
              </Upload>
            </Col>
          </Row>
          <Row gutter={0}>
            <Col span={3}>文件格式：</Col>
            <Col span={21}>
              .xls Excle文件，参考模板【<a href="/example/import/test.xlsx">批量导入模板.xlsx</a>】
            </Col>
          </Row>
          <Row gutter={0}>
            <Col span={3}>数据格式：</Col>
            <Col span={21}>
            <#list table.columnList as item>${item.comment}|</#list>
            </Col>
          </Row>
          <Row gutter={0}>
            <Col span={3}>注意事项：</Col>
            <Col span={21}>
              1、文件第一行为标题，系统从第二行数据开始处理<br />
              2、注意去除表格中公式等设置<br />
            </Col>
          </Row>
          <Row gutter={0}>
            <Col span={21} />
            <Col span={3}>
              <Button type="primary" onClick={this.handleSubmitTask} loading={loading}>
                <Icon type="cloud-upload" /> 提交任务
              </Button>
            </Col>
          </Row>
        </Card>
      </PageHeaderLayout>
    );
  }
}

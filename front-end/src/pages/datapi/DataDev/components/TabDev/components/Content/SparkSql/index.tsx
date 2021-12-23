import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import SplitPane from 'react-split-pane';
import { Form, Input, Modal, Select, Table, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import type { IDisposable } from 'monaco-editor';
import styles from './index.less';
<<<<<<< HEAD
import { getUDFList } from '@/services/datadev';
import { UDF } from '@/types/datadev';

=======
import SqlEditor from '@/components/SqlEditor';
>>>>>>> feat:新增自动联动逻辑
interface SparkSqlProps {
  monaco: any;
  data: {
    content: any;
    log: any;
    res: any[];
  };
  removeResult: (i: number) => void;
  visible: boolean;
  onCancel: () => void;
}

const { TabPane } = Tabs;
const { Item } = Form;
const width = 200;

const SparkSql: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { monaco, data: { content, log, res }, removeResult, visible, onCancel },
  ref,
) => {
  const [monacoValue, setMonacoValue] = useState('');
  const [monacoHeight, setMonacoHeight] = useState(500);
  const [UDFList, setUDFList] = useState<UDF[]>([]);
  const [form] = Form.useForm();
  const editorRef = useRef<any>();
  const monacoInnerRef = useRef<IDisposable>();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setMonacoHeight(height);

    getUDFListWrapped();

    return () => {
      editorRef.current?.dispose();
      monacoInnerRef.current?.dispose();
    };
  }, []);

  useEffect(() => {
    if (content) {
      setMonacoValue(content.sourceSql);
      const udfIds = content.udfIds || '';
      form.setFieldsValue({
        externalTables: content.externalTables,
        udfIds: udfIds.split(','),
      });
    }
  }, [content]);

  const getUDFListWrapped = () =>
    getUDFList()
      .then((res) => setUDFList(res.data))
      .catch((err) => {});

  return (
    <>
<<<<<<< HEAD
      <div style={{ position: 'relative', height: monacoHeight }}>
        <SplitPane split="horizontal" defaultSize="60%">
          <MonacoEditor
            ref={monaco}
            height="100%"
            width="100%"
            language="plaintext"
            theme="vs-dark"
            value={monacoValue}
            onChange={(v) => setMonacoValue(v)}
            options={{ automaticLayout: true, quickSuggestions: false }}
          />
          <Tabs
            className={styles.tabs}
            type="editable-card"
            hideAdd
            onEdit={(key, action) => {
              if (action === 'remove') {
                const index = Number(key);
                if (Number.isInteger(index)) {
                  removeResult(index);
                }
              }
            }}
            style={{ height: '100%' }}
          >
            <TabPane tab="运行日志" key="log" style={{ height: '100%' }} closable={false}>
              <MonacoEditor
                height="100%"
                language="json"
                theme="vs-dark"
                value={log.join('\n')}
                options={{ readOnly: true, automaticLayout: true }}
              />
            </TabPane>
            {res.map((_, i) => (
              <TabPane
                tab={`结果${i + 1}`}
                key={i}
                style={{ height: '100%', backgroundColor: '#2d3956', padding: 16 }}
              >
                <Table
                  className={styles.table}
                  columns={_.columns}
                  dataSource={_.dataSource}
                  size="small"
                  pagination={{
                    hideOnSinglePage: true,
                    pageSize: 9999,
                  }}
                  scroll={{
                    x: 'max-content',
                  }}
                />
              </TabPane>
            ))}
          </Tabs>
        </SplitPane>
      </div>
      <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={null} forceRender>
        <Form form={form} colon={false}>
          <Item name="externalTables" label="外部表">
            <Input placeholder="请输入" style={{ width }} />
          </Item>
          <Item name="udfIds" label="自定义函数">
            <Select
              mode="multiple"
              placeholder="请选择"
              options={UDFList.map((_) => ({
                label: _.udfName,
                value: `${_.id}`,
              }))}
              style={{ width }}
=======
      <Tabs>
        <TabPane tab="编辑器" key="editor" style={{ padding: '16px 0', height: 440 }} forceRender >
          <SqlEditor />
          </TabPane>
        <TabPane tab="作业配置" key="config" style={{ padding: 16 }} forceRender>
          <Form form={form} colon={false}>
            <Item name="externalTables" label="外部表">
              <Input placeholder="请输入" style={{ width }} />
            </Item>
            <Item name="udfIds" label="自定义函数">
              <Select placeholder="请选择" options={[]} style={{ width }} />
            </Item>
          </Form>
        </TabPane>
      </Tabs>
      <Tabs
        className={styles.tabs}
        type="editable-card"
        hideAdd
        onEdit={(key, action) => {
          if (action === 'remove') {
            const index = Number(key);
            if (Number.isInteger(index)) {
              removeResult(index);
            }
          }
        }}
      >
        <TabPane tab="日志" key="log" style={{ height: 440, padding: '16px 0' }} closable={false}>
          <MonacoEditor
            height="400"
            language="json"
            theme="vs-dark"
            value={log.join('\n')}
            options={{ readOnly: true, automaticLayout: true }}
          />
        </TabPane>
        {res.map((_, i) => (
          <TabPane tab={`结果${i + 1}`} key={i} style={{ height: 440, padding: '16px 0' }}>
            <Table
              columns={_.columns}
              dataSource={_.dataSource}
              size="small"
              pagination={{
                hideOnSinglePage: true,
                pageSize: 9999,
              }}
>>>>>>> feat:新增自动联动逻辑
            />
          </Item>
        </Form>
      </Modal>
    </>
  );
};

export default forwardRef(SparkSql);

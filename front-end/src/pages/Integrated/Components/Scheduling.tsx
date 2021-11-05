import React, { useState, useRef } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { Button } from 'antd';
type DataSourceType = {
  id: React.Key;
  configValue?: string;
  configValueKey?: string;
  configValueRemarks?: string;
};

const defaultData: DataSourceType[] = new Array(3).fill(1).map((_, index) => {
  return {
    id: (Date.now() + index).toString(),
    configValueKey: `活动名称${index}`,
    configValue: '这个活动真好玩',
    configValueRemarks: 'open',
  };
});

export default () => {
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>(() =>
    defaultData.map((item) => item.id),
  );
  const actionRef = useRef<ActionType>();
  const [dataSource, setDataSource] = useState<DataSourceType[]>(() => defaultData);

  const columns: ProColumns<DataSourceType>[] = [
    {
      title: '参数名称',
      dataIndex: 'configValueKey',
      width: '100px',
      editable: false
    },
    {
      title: '值',
      dataIndex: 'configValue',
      width: '280px',
    },
    {
      title: '备注',
      dataIndex: 'configValueRemarks',
    },
  ];

  return (
    <>
        <EditableProTable<DataSourceType>
          columns={columns}
          rowKey="id"
          value={dataSource}
          actionRef={actionRef}
          onChange={setDataSource}
          recordCreatorProps={false}
          editable={{
            type: 'multiple',
            editableKeys,
            onValuesChange: (record, recordList) => {
              setDataSource(recordList);
            },
            onChange: setEditableRowKeys,
          }}
        />
        <div style={{textAlign: 'right'}}>
          <Button type="primary"> 保存</Button>
        </div>
    </>
  );
};

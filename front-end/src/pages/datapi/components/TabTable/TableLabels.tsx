import React, { Fragment, useEffect, useState } from 'react';
import ProForm, {
  ProFormText,
  ProFormRadio,
  ProFormSelect,
  ProFormCheckbox,
} from '@ant-design/pro-form';
import { Checkbox, Popover, Tooltip, Typography } from 'antd';
import type { FormInstance } from 'antd';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';
import Title from '../Title';
import { InitialLabel } from './constants';
import { getTableLabels, getDWOwner } from '@/services/tablemanage';

export type EnumValueType = { enumValue: string; valueCode: string };
export interface LabelProps {
  labelName: string;
  labelCode: string;
  labelParamType: string;
  labelRequired: 0 | 1;
  enumValues: EnumValueType[];
  [key: string]: any;
}
export interface TableLabelsProps {
  form: FormInstance;
  initial: any;
}

const CheckboxGroup = Checkbox.Group;
const { Text } = Typography;
const rules = [{ required: true, message: '必填' }];

const FormLabel: FC = ({ children }) => {
  return (
    <Tooltip title={children}>
      <Text ellipsis>{children}</Text>
    </Tooltip>
  );
};

const TableLabels: FC<TableLabelsProps> = ({ form, initial }) => {
  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [allChecked, setAllChecked] = useState(true); // 是否全选
  const [indeterminate, setIndeterminate] = useState(false); // 一个样式
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');

  const [labels, setLabels] = useState<any[]>([]);
  const [labelsMap, setLabelsMap] = useState<Map<string, any>>(new Map());

  useEffect(() => {
    getTableLabels({ subjectType: 'TABLE' }).then((res) => {
      getDWOwner()
        .then((owners) => {
          const map = new Map(); // 快速检索用的map, [labelCode, _]
          const list: string[] = []; // 选中的checkedlist, labelCode[]
          // ops, 用以渲染checklist.group
          const ops = [InitialLabel, ...res.data].map((_: LabelProps) => {
            const tmp: any = {
              ..._,
              label: _.labelName,
              value: _.labelCode,
              disabled: _.labelRequired,
            };
            // 只有当 labelTag === "ATTRIBUTE_LABEL" 时不存在 labelParamType
            if (_.labelTag !== 'ATTRIBUTE_LABEL') {
              if (_.labelCode === 'dwOwnerId') {
                tmp.enums = owners.data.content.map((_: any) => ({
                  label: _.nickname,
                  value: _.id,
                }));
              }
              if (_.labelParamType?.endsWith('ENUM')) {
                tmp.enums = _.enumValues.map((item: any) => ({
                  label: item.enumValue,
                  value: item.valueCode,
                }));
              }
            }
            map.set(_.labelCode, tmp);
            list.push(_.labelCode);
            return tmp;
          });

          if (initial) {
            const tableLabels = initial.tableLabels;
            const initialValue = { tableName: initial.tableName };
            tableLabels.forEach((_: any) => (initialValue[_.labelCode] = _.labelParamValue));
            form.setFieldsValue(initialValue);
          }

          setLabelsMap(map);
          setCheckedList(list);
          setLabels(ops);
        })
        .catch((err) => []);
    });
  }, []);

  // 单选
  const onCheck = (list: any[]) => {
    setCheckedList(list);
    setAllChecked(list.length === labels.length);
    setIndeterminate(!!list.length && list.length < labels.length);
  };
  // 全选
  const onAllCheck = (checked: boolean) => {
    const list = checked
      ? labels.map((_) => _.value)
      : labels.filter((_) => _.labelRequired).map((_) => _.value);
    setCheckedList(list);
    setAllChecked(checked);
    setIndeterminate(!checked);
  };
  // 渲染checklist的每一项
  const renderFormList = () => {
    return checkedList.map((labelCode) => {
      const _ = labelsMap.get(labelCode);
      switch (_.labelTag) {
        case 'STRING_LABEL':
          return (
            <ProFormText
              key={_.value}
              name={_.labelCode}
              label={<FormLabel>{_.labelName}</FormLabel>}
              width="sm"
              rules={!!_.labelRequired ? rules : []}
              placeholder="请输入"
            />
          );
        case 'BOOLEAN_LABEL':
          return (
            <ProFormRadio.Group
              key={_.value}
              name={_.labelCode}
              label={<FormLabel>{_.labelName}</FormLabel>}
              width="sm"
              rules={!!_.labelRequired ? rules : []}
              options={[
                { label: '是', value: 'true' },
                { label: '否', value: 'false' },
              ]}
            />
          );
        case 'ENUM_VALUE_LABEL':
        case 'USER_LABEL':
          return (
            <ProFormSelect
              key={_.value}
              name={_.labelCode}
              label={<FormLabel>{_.labelName}</FormLabel>}
              width="sm"
              rules={!!_.labelRequired ? rules : []}
              options={_.enums}
            />
          );
        case 'ATTRIBUTE_LABEL':
        default:
          return (
            <ProFormCheckbox.Group
              key={_.value}
              name={_.labelCode}
              label={<FormLabel>{_.labelName}</FormLabel>}
              width="sm"
              options={[{ label: null, value: _.labelCode }]}
            />
          );
      }
    });
  };

  return (
    <Fragment>
      <Title>
        <span>基本信息</span>
        <Popover
          trigger="click"
          placement="bottomRight"
          onVisibleChange={(v) => setIconType(v ? 'icon-shezhijihuo' : 'icon-shezhi')}
          title={
            <Checkbox
              indeterminate={indeterminate}
              onChange={({ target: { checked } }) => onAllCheck(checked)}
              checked={allChecked}
            >
              表信息展示
            </Checkbox>
          }
          content={
            <CheckboxGroup
              options={labels}
              value={checkedList}
              onChange={(checked) => onCheck(checked)}
              style={{ display: 'flex', flexDirection: 'column' }}
            />
          }
        >
          <IconFont type={iconType} style={{ cursor: 'pointer', marginLeft: 8 }} />
        </Popover>
      </Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="inline"
        colon={false}
        form={form}
        submitter={false}
      >
        {renderFormList()}
      </ProForm>
    </Fragment>
  );
};

export default TableLabels;

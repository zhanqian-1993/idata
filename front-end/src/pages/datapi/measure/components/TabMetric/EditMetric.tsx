import React, {
  forwardRef,
  Fragment,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';
import EditAtomic from './components/EditAtomic';
import EditDerive from './components/EditDerive';
import EditComplex from './components/EditComplex';
import { getFolders } from '@/services/measure';
import { rules } from '@/constants/datapi';
import { EnumValue, Metric } from '@/types/datapi';
import { getEnumValues } from '@/services/tablemanage';

export interface ViewModifierProps {
  initial?: Metric;
}
interface AtomicExportProps {
  data: [];
}
interface DeriveExportProps {
  atomicMetricCode: string;
  modifiers: [];
}
interface ComplexExportProps {
  data: string;
}

const { require } = rules;
const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
  { label: '复合指标', value: 'COMPLEX_METRIC_LABEL' },
];

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ initial }, ref) => {
  const [folderOps, setFolderOps] = useState([]);
  const [form] = Form.useForm();
  const [bizTypeEnum, setBizTypeEnum] = useState([]);
  const [metricType, setMetricType] = useState('');
  const refAtomic = useRef<AtomicExportProps>();
  const refDerive = useRef<DeriveExportProps>();
  const refComplex = useRef<ComplexExportProps>();
  const EditRefMap = {
    ATOMIC_METRIC_LABEL: <EditAtomic ref={refAtomic} initial={initial} />,
    DERIVE_METRIC_LABEL: <EditDerive ref={refDerive} initial={initial} />,
    COMPLEX_METRIC_LABEL: <EditComplex ref={refComplex} initial={initial} />,
  };

  useImperativeHandle(ref, () => ({
    form: form,
    data: () => {
      switch (metricType) {
        case 'ATOMIC_METRIC_LABEL':
          return {
            type: 'ATOMIC',
            data: refAtomic.current?.data,
          };
        case 'DERIVE_METRIC_LABEL':
          return {
            type: 'DERIVE',
            data: {
              atomicMetricCode: refDerive.current?.atomicMetricCode,
              modifiers: refDerive.current?.modifiers,
            },
          };
        case 'COMPLEX_METRIC_LABEL':
        default:
          return {
            type: 'COMPLEX',
            data: refComplex.current?.data,
          };
      }
    },
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({
          label: _.folderName,
          value: _.id,
        }));
        setFolderOps(fd);
      })
      .catch((err) => {});
    getEnumValues({ enumCode: 'bizTypeEnum:ENUM' })
      .then((res) => {
        const options = res.data.map((enumValue: EnumValue) => ({
          label: enumValue.enumValue,
          value: enumValue.valueCode,
        }));
        setBizTypeEnum(options);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      // form initial
      const values = {
        labelName: initial.labelName,
        folderId: initial.folderId,
        labelTag: initial.labelTag,
      };
      initial.labelAttributes.forEach((labelAttribute) => {
        values[labelAttribute.attributeKey] = labelAttribute.attributeValue;
      });
      form.setFieldsValue(values);
      setMetricType(initial.labelTag);
    }
  }, [initial]);

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        form={form}
        submitter={false}
      >
        <ProFormGroup>
          <ProFormSelect
            name="labelTag"
            label="指标类型"
            width="sm"
            placeholder="请选择"
            rules={require}
            options={MetricTypeOps}
            fieldProps={{ onChange: setMetricType }}
          />
          <ProFormText
            name="labelName"
            label="指标名称"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormText
            name="metricId"
            label="指标ID"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText
            name="enName"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormSelect
            name="bizProcessCode"
            label="业务过程"
            width="sm"
            placeholder="请选择"
            rules={require}
            options={bizTypeEnum}
          />
          <ProFormSelect
            name="folderId"
            label="位置"
            width="sm"
            placeholder="根目录"
            options={folderOps}
          />
        </ProFormGroup>
        <ProFormText
          name="metricDefine"
          label="定义"
          width="md"
          placeholder="请输入"
          rules={require}
        />
        <ProFormTextArea name="comment" label="备注" width="md" placeholder="请输入" />
      </ProForm>
      {EditRefMap[metricType]}
    </Fragment>
  );
};

export default forwardRef(ViewModifier);

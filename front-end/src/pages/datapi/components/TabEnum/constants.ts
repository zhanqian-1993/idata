export const ViewInitialColumns = [
  { title: '枚举值', dataIndex: ['enumValue', 'value'], key: 'enumValue', type: 'STRING' },
  {
    title: '父级枚举值',
    dataIndex: 'parentValue',
    key: 'parentValue',
    type: 'STRING',
    render: (_: any) => _ || '-',
  },
];

export const rules = [{ required: true, message: '必填' }];

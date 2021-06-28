import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal } from 'antd';
import { useModel } from 'umi';
import type { FC, ChangeEvent } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import CreateFolder from './components/CreateFolder';

import { deleteFolder } from '@/services/objectlabel';

export type Key = string | number;
export interface FolderTreeProps {}
export interface TreeNode {
  name: string;
  type: string;
  folderId: string;
  parentId: string;
  children?: TreeNode[];
  [key: string]: any;
}
export interface FlatTreeNode {
  key: string;
  name: string;
  parentId?: string;
}

const { TreeNode } = Tree;
const { confirm } = Modal;
const NodeTypeIcon = {
  FOLDER: <IconFont type="icon-wenjianjia" key="folder" />,
  FOLDEROPEN: <IconFont type="icon-wenjianjiazhankai" key="folderopen" />,
  LABEL: <IconFont type="icon-biaoqian1" key="label" />,
  ENUM: <IconFont type="icon-meiju" key="enum" />,
  TABLE: <IconFont type="icon-biao" key="table" />,
};

const FolderTree: FC<FolderTreeProps> = ({}) => {
  const [searchValue, setSearchValue] = useState('');
  const [expandedKeys, setExpandedKeys] = useState<any[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const flatTree = useRef<FlatTreeNode[]>([]);

  const [visible, setVisible] = useState(false);

  const { tree, getTree, curNode, setCurNode, setFolderMode, viewTab, createTab } = useModel(
    'objectlabel',
    (_) => ({
      tree: _.tree,
      getTree: _.getTree,
      curNode: _.curNode,
      setCurNode: _.setCurNode,
      setFolderMode: _.setFolderMode,
      viewTab: _.viewTab,
      createTab: _.createTab,
    }),
  );

  useEffect(() => {
    getTree();
  }, []);

  useEffect(() => {
    flat(tree);
  }, [tree]);

  const menu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="objectLabel">新建数据标签</Menu.Item>
    </Menu>
  );

  const treeMenu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Item key="objectLabel">新建数据标签</Menu.Item>
      <Menu.Divider />
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      {curNode?.type === 'FOLDER' && (
        <Fragment>
          <Menu.Item key="edit">编辑文件夹</Menu.Item>
          <Menu.Item key="delete">删除文件夹</Menu.Item>
        </Fragment>
      )}
    </Menu>
  );

  // 目录的操作
  const onMenuActions = (key: Key) => {
    switch (key) {
      case 'folder':
        setFolderMode('create');
        setVisible(true);
        break;
      case 'edit':
        setFolderMode('edit');
        setVisible(true);
        break;
      case 'delete':
        onDeleteFolder();
        break;
      case 'objectLabel':
        createTab();
        break;
      default:
        break;
    }
  };

  const onDeleteFolder = () =>
    confirm({
      title: '您确定要删除该文件夹吗？',
      onOk: () =>
        deleteFolder({ id: curNode?.folderId })
          .then((res) => {
            if (res.success) {
              message.success('删除文件夹成功');
              getTree();
            }
          })
          .catch((err) => {}),
    });

  // 组装数据，并高亮检索结果
  const loop = (data: any[], parentId?: string): any => {
    const n = data.length;
    return data.map((_, i) => {
      const { name, type, cid } = _;
      const _i = name.indexOf(searchValue);
      const node = { ..._, key: cid };
      let _type = type;
      let title = (
        <span key="title" className={!parentId && type === 'FOLDER' && styles['folder-root']}>
          {name}
        </span>
      );
      // 判断文件夹类型的节点是否展开以赋值icon类型
      if (type === 'FOLDER' && expandedKeys.indexOf(cid) > -1) {
        _type = 'FOLDEROPEN';
      }
      // 给检索命中的title加高亮
      if (_i > -1) {
        const beforeStr = name.substring(0, _i);
        const afterStr = name.substring(_i + searchValue?.length);
        const className = (!parentId && type === 'FOLDER' && styles['folder-root']) || '';
        title = (
          <span key="title" className={className}>
            {beforeStr}
            <span className={styles['search-match']}>{searchValue}</span>
            {afterStr}
          </span>
        );
      }
      (type === 'FOLDER' || i === n - 1) && (node.className = styles['folder-margin']);
      node.title = [NodeTypeIcon[_type], title];
      parentId && (node.parentId = parentId);

      return _.children ? (
        <TreeNode {...node}>{loop(_.children, cid)}</TreeNode>
      ) : (
        <TreeNode {...node} />
      );
    });
  };

  // 将树平铺用以检索
  const flat = (data: any[], parentId?: string) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i];
      const { name, cid } = node;
      flatTree.current.push({ name, key: cid, parentId: parentId });
      if (node.children) {
        flat(node.children, node.cid);
      }
    }
  };

  // 检索树
  const onFilterTree = ({ target: { value } }: ChangeEvent<HTMLInputElement>) => {
    if (!value) {
      setExpandedKeys([]);
      setSearchValue('');
      return;
    }
    const keys = flatTree.current
      .map((node) => (node.name.indexOf(value) > -1 ? node.parentId : ''))
      .filter((_) => !!_);
    setExpandedKeys(keys);
    setSearchValue(value);
    setAutoExpandParent(true);
  };

  const onExpand = (keys: Key[] = []) => {
    setExpandedKeys(keys);
    setAutoExpandParent(false);
  };

  return (
    <Fragment>
      <div className={styles.search}>
        <Input
          className={styles['search-input']}
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onChange={onFilterTree}
        />
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <IconFont
            type="icon-xinjian"
            className={styles['icon-plus']}
            onClick={() => setCurNode(null)}
          />
        </Dropdown>
      </div>
      <Dropdown overlay={treeMenu} placement="bottomLeft" trigger={['contextMenu']}>
        <Tree
          blockNode
          onExpand={(keys) => onExpand(keys)}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          onRightClick={({ node }: any) => {
            const parentId = node.parentId?.split('_')[1] || null;
            const folderId = `${node.folderId}`;
            setCurNode({ ...node, folderId, parentId });
          }}
          onSelect={(selectedKeys, { node }: any) => node.type !== 'FOLDER' && viewTab(node)}
        >
          {loop(tree)}
        </Tree>
      </Dropdown>
      <CreateFolder visible={visible} onCancel={() => setVisible(false)} />
    </Fragment>
  );
};

export default FolderTree;
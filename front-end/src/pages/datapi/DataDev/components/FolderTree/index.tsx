import React, { useCallback, useEffect, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal, Popover, Empty } from 'antd';
import { useModel } from 'umi';
import type { FC, Key } from 'react';
import styles from './index.less';

import IconFont from '@/components/IconFont';
import { deleteFolder, getFunctionTree } from '@/services/datadev';
import { TreeNode as Treenode } from '@/types/datadev';
import { FolderBelong, FolderTypes } from '@/constants/datadev';

import CreateFolder from './components/CreateFolder';
import TreeNodeTitle from './components/TreeNodeTitle';

const { TreeNode } = Tree;
const { confirm } = Modal;

const FolderTree: FC = () => {
  const [functionTree, setFunctionTree] = useState<Treenode[]>([]);
  const [visible, setVisible] = useState(false);

  const {
    tree,
    getTreeWrapped,
    setFolderMode,
    curNode,
    setCurNode,
    belongFunctions,
    setBelongFunctions,
    setKeyWord,
    onCreateEnum,
    onCreateTable,
    onViewTree,
    showLabel,
    onCreateDAG,
    setVisibleTask,
    setCurLabel,
  } = useModel('datadev', (_) => ({
    tree: _.tree,
    getTreeWrapped: _.getTreeWrapped,
    setFolderMode: _.setFolderMode,
    curNode: _.curNode,
    setCurNode: _.setCurNode,
    belongFunctions: _.belongFunctions,
    setBelongFunctions: _.setBelongFunctions,
    setKeyWord: _.setKeyWord,
    onCreateEnum: _.onCreateEnum,
    onCreateTable: _.onCreateTable,
    onViewTree: _.onViewTree,
    showLabel: _.showLabel,
    onCreateDAG: _.onCreateDAG,
    setVisibleTask: _.setVisibleTask,
    setCurLabel: _.setCurLabel,
  }));

  useEffect(() => {
    getTreeWrapped();
    getFunctionTree()
      .then((res) => setFunctionTree(res.data))
      .catch((err) => {});
  }, []);

  const menu = (
    <Menu onClick={({ key }) => onAction(key)}>
      <Menu.Item key="CreateTable">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianwenjianjia1" />
        新建表
      </Menu.Item>
      <Menu.Item key="CreateLabel">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianbiaoqian1" />
        新建标签
      </Menu.Item>
      <Menu.Item key="CreateEnum">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianmeiju" />
        新建枚举
      </Menu.Item>
      <Menu.Item key="CreateDAG">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianDAG" />
        新建DAG
      </Menu.Item>
      <Menu.Item key="CreateJob">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianzuoye" />
        新建作业
      </Menu.Item>
    </Menu>
  );

  // 新建文件夹/标签/枚举/表
  const onAction = (key: Key, node?: Treenode) => {
    switch (key) {
      case 'CreateFolder':
        setCurNode(node);
        setFolderMode('create');
        setVisible(true);
        break;
      case 'EditFolder':
        setCurNode(node);
        setFolderMode('edit');
        setVisible(true);
        break;
      case 'DeleteFolder':
        setCurNode(node);
        onDeleteFolder();
        break;
      case 'CreateTable':
        setCurNode(node);
        onCreateTable();
        break;
      case 'CreateLabel':
        setCurNode(node);
        setCurLabel(-1);
        showLabel();
        break;
      case 'CreateEnum':
        setCurNode(node);
        onCreateEnum();
        break;
      case 'CreateDAG':
        setCurNode(node);
        onCreateDAG();
        break;
      case 'CreateJob':
        setCurNode(node);
        setVisibleTask(true);
        break;

      default:
        break;
    }
  };

  const onDeleteFolder = () =>
    confirm({
      title: '您确定要删除该文件夹吗？',
      autoFocusButton: null,
      onOk: () => {
        console.log(curNode);

        if (curNode) {
          deleteFolder({ id: curNode.id }).then((res) => {
            if (res.success) {
              message.success('删除文件夹成功');
              getTreeWrapped();
            }
          });
        }
      },
    });

  const isRootFolder = useCallback((belong: FolderBelong) => {
    if (
      belong === FolderBelong.DESIGN ||
      belong === FolderBelong.DAG ||
      belong === FolderBelong.DI ||
      belong === FolderBelong.DEV
    ) {
      return true;
    } else {
      return false;
    }
  }, []);

  // 为了加上样式所以使用这种方式
  const loop = (data: Treenode[]): any => {
    const n = data.length;
    return data.map((_, i) => {
      // 按组件的属性赋值key
      _.key = _.cid;
      // 给node加上样式
      if (isRootFolder(_.belong) || i === n - 1) {
        _.className = 'folder-margin';
      }
      // 给title加上样式
      let title = <span className={_.parentId ? '' : 'folder-root'}>{_.name}</span>;
      // 给type不为FolderTypes.RECORD的节点加上icon
      if (_.type === FolderTypes.RECORD) {
        _.title = title;
      } else {
        _.title = <TreeNodeTitle node={_} title={title} onAction={onAction} />;
      }

      return _.children ? <TreeNode {..._}>{loop(_.children)}</TreeNode> : <TreeNode {..._} />;
    });
  };

  // 渲染筛选树
  const loopBelongTree = (data: Treenode[]) =>
    data.map((_) => {
      _.title = _.name;
      _.key = _.belong;
      return _.children ? (
        <TreeNode {..._}>{loopBelongTree(_.children)}</TreeNode>
      ) : (
        <TreeNode {..._} />
      );
    });

  return (
    <div className="folder-tree">
      <div className="search">
        <Input
          className="search-input"
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onKeyDown={(e) => e.key === 'Enter' && getTreeWrapped()}
          onChange={({ target: { value } }) => setKeyWord(value)}
        />
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <IconFont type="icon-xinjian1" className="icon-plus" onClick={() => setCurNode(null)} />
        </Dropdown>
        <Popover
          content={
            functionTree.length ? (
              <Tree
                blockNode
                checkable
                defaultExpandAll
                defaultCheckedKeys={belongFunctions}
                onCheck={(checked, { halfCheckedKeys }) => {
                  let checkedKeys = checked as string[];
                  if (Array.isArray(halfCheckedKeys)) {
                    checkedKeys = checkedKeys.concat(halfCheckedKeys as string[]);
                  }
                  setBelongFunctions(checkedKeys);
                }}
              >
                {loopBelongTree(functionTree)}
              </Tree>
            ) : (
              <Empty />
            )
          }
          placement="bottomLeft"
          trigger="click"
        >
          <IconFont type="icon-shaixuan" className="icon-plus" />
        </Popover>
      </div>
      {tree.length ? (
        <div className={styles.tree} style={{ marginTop: 16, height: '100%' }}>
          <Tree
            blockNode
            onSelect={(selectedKeys, { node, ...props }) => {
              // 节点的浮窗菜单点击时会触发onSelect，不知道为什么。
              // 但是这个时候 selectedKeys.length === 0
              // 可以通过这个来判断是选中节点还是浮窗点击。
              if (selectedKeys.length) {
                const nodeForTSLint: any = node;
                // setCurNode(null);
                if (nodeForTSLint.type === FolderTypes.RECORD) {
                  onViewTree(nodeForTSLint);
                }
              }
            }}
          >
            {loop(tree)}
          </Tree>
        </div>
      ) : (
        <Empty style={{ marginTop: 56 }} />
      )}
      {visible && <CreateFolder visible={visible} onCancel={() => setVisible(false)} />}
    </div>
  );
};

export default FolderTree;
package cn.zhengcaiyun.idata.label.service.impl;

import cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDao;
import cn.zhengcaiyun.idata.label.dal.model.LabObjectLabel;
import cn.zhengcaiyun.idata.label.dto.LabObjectLabelDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleLayerDto;
import cn.zhengcaiyun.idata.label.service.LabObjectLabelService;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderManager;
import cn.zhengcaiyun.idata.label.service.label.LabObjectLabelManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDynamicSqlSupport.labObjectLabel;
import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-23 10:31
 **/
@Service
public class LabObjectLabelServiceImpl implements LabObjectLabelService {

    private final LabObjectLabelDao objectLabelDao;
    private final LabFolderManager folderManager;
    private final LabObjectLabelManager objectLabelManager;

    @Autowired
    public LabObjectLabelServiceImpl(LabObjectLabelDao objectLabelDao,
                                     LabFolderManager folderManager,
                                     LabObjectLabelManager objectLabelManager) {
        checkNotNull(objectLabelDao, "objectLabelDao must not be null.");
        checkNotNull(folderManager, "folderManager must not be null.");
        checkNotNull(objectLabelManager, "objectLabelManager must not be null.");
        this.objectLabelDao = objectLabelDao;
        this.folderManager = folderManager;
        this.objectLabelManager = objectLabelManager;
    }

    @Override
    public Long createLabel(LabObjectLabelDto labelDto, String operator) {
        LabObjectLabel existRecord = objectLabelManager.getObjectLabel(labelDto.getName());
        checkState(Objects.isNull(existRecord), "标签名称已存在");
        Optional.ofNullable(labelDto.getFolderId())
                .ifPresent(folderId -> folderManager.getFolder(folderId, "文件夹不存在"));
        checkArgument(isNotEmpty(labelDto.getObjectType()), "标签主体不能为空");


        LabObjectLabel label = newCreatedObjectLabel(labelDto, operator);
        objectLabelDao.insertSelective(label);
        return label.getId();
    }

    @Override
    public Long editLabel(LabObjectLabelDto labelDto, String operator) {
        LabObjectLabel label = objectLabelManager.getObjectLabel(labelDto.getId(), "标签不存在");
        LabObjectLabel checkNameLabel = objectLabelManager.getObjectLabel(label.getName());
        if (!Objects.isNull(checkNameLabel)) {
            //编辑后，名称不能和其他标签相同
            checkState(Objects.equals(label.getId(), checkNameLabel.getId()), "标签名称已存在");
        }
        Optional.ofNullable(labelDto.getFolderId())
                .ifPresent(folderId -> folderManager.getFolder(folderId, "文件夹不存在"));

        //标签编辑逻辑：将旧数据状态置为删除，新增一条标签记录，版本号+1
        LabObjectLabel newLabel = newCreatedObjectLabel(labelDto, operator);
        newLabel.setVersion(label.getVersion() + 1);
        //标签主体限制修改
        newLabel.setObjectType(label.getObjectType());
        objectLabelManager.renewLabel(newLabel, label.getId());
        return newLabel.getId();
    }

    @Override
    public LabObjectLabelDto getLabel(Long id) {
        LabObjectLabel label = objectLabelManager.getObjectLabel(id, "标签不存在");
        LabObjectLabelDto dto = new LabObjectLabelDto();
        BeanUtils.copyProperties(label, dto);
        dto.setRuleLayers(ruleLayerFromJson(label.getRules()));
        return dto;
    }

    @Override
    public Boolean deleteLabel(Long id, String operator) {
        LabObjectLabel label = objectLabelManager.getObjectLabel(id, "标签不存在");
        //已删除
        if (label.getDel().equals(DEL_YES.val)) return true;
        // 软删除，修改del状态
        objectLabelDao.update(dsl -> dsl.set(labObjectLabel.del).equalTo(DEL_YES.val)
                .set(labObjectLabel.editor).equalTo(operator).where(labObjectLabel.id, isEqualTo(id)));
        return null;
    }

    private LabObjectLabel newCreatedObjectLabel(LabObjectLabelDto labelDto, String operator) {
        LabObjectLabel label = new LabObjectLabel();
        label.setName(labelDto.getName());
        label.setNameEn(labelDto.getNameEn());
        label.setObjectType(labelDto.getObjectType());
        label.setRemark(labelDto.getRemark());
        label.setFolderId(MoreObjects.firstNonNull(labelDto.getFolderId(), 0L));
        label.setVersion(1);
        label.setRules(ruleLayerToJson(labelDto.getRuleLayers()));
        label.setDel(DEL_NO.val);
        label.setCreator(operator);
        label.setEditor(operator);
        return label;
    }

    private String ruleLayerToJson(List<LabelRuleLayerDto> ruleLayers) {
        if (CollectionUtils.isEmpty(ruleLayers)) return "";
        return JSON.toJSONString(ruleLayers);
    }

    private List<LabelRuleLayerDto> ruleLayerFromJson(String ruleLayerJson) {
        if (isEmpty(ruleLayerJson)) return Lists.newArrayList();
        return JSON.parseObject(ruleLayerJson, new TypeReference<List<LabelRuleLayerDto>>() {
        });
    }
}
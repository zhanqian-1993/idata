/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.commons.exception.ExternalIntegrationException;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.commons.rpc.HttpUtil;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import cn.zhengcaiyun.idata.develop.dal.model.integration.DSEntityMapping;
import cn.zhengcaiyun.idata.develop.dal.repo.integration.DSEntityMappingRepo;
import cn.zhengcaiyun.idata.develop.integration.schedule.IDagIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.ResultDto;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:40
 **/
@Component
public class WorkflowIntegrator implements IDagIntegrator {

    public static final String ENTITY_TYPE = "workflow";
    public static final String DAG_NAME_DELIMITER = "__";


    public static final String PARAM_DEFAULT_WARNING_TYPE = "NONE";
    public static final Integer PARAM_DEFAULT_NOTIFY_GROUP_ID = 1;
    public static final String PARAM_DEFAULT_FAILURE_POLICY = "CONTINUE";
    public static final String PARAM_DEFAULT_PROCESS_INSTANCE_PRIORITY = "MEDIUM";


    public static final int RESULT_PROCESS_DEFINITION_NAME_EXIST = 10168;


    private final DSEntityMappingRepo dsEntityMappingRepo;

    @Autowired
    public WorkflowIntegrator(DSEntityMappingRepo dsEntityMappingRepo) {
        this.dsEntityMappingRepo = dsEntityMappingRepo;
    }

    private String getDSToken(String environment) {
        return "33333";
    }

    private String getDSProjectCode(String environment) {
        return "123";
    }

    private String getDSBaseUrl(String environment) {
        return "123";
    }

    private String getDSTenantCode(String environment) {
        return "default";
    }

    private Integer getDagTimeout(String environment) {
        return 120;
    }

    private String buildWorkflowName(DAGInfo dagInfo) {
        return dagInfo.getName() + DAG_NAME_DELIMITER + Strings.padStart(dagInfo.getId().toString(), 6, '0');
    }

    @Override
    public void create(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/s%/process-definition/empty", projectCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());
        // 创建空工作流和定时器，状态为下线
        Map<String, String> req_param = buildDagReqParam(dagInfo, dagSchedule);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            if (resultDto.isStatus(RESULT_PROCESS_DEFINITION_NAME_EXIST)) {
                // 存在同名工作流，两种方案：1、手动删除DS同名工作流；2、将同名工作流和DAG关联，可能会将无关工作流关联，本期暂用第一种方案实现
                throw new ExternalIntegrationException(String.format("DS存在同名工作流：s%", req_param.get("name")));
            } else {
                throw new ExternalIntegrationException(String.format("创建DS工作流失败：s%", resultDto.getMsg()));
            }
        }

        JSONObject workflow = resultDto.getData();
        Long workflowCode = workflow.getLong("code");
        //保存DAG和workflow关系
        DSEntityMapping mapping = new DSEntityMapping();
        mapping.setEntityId(dagInfo.getId());
        mapping.setEnvironment(dagInfo.getEnvironment());
        mapping.setDsEntityType(ENTITY_TYPE);
        mapping.setDsEntityCode(workflowCode);
        dsEntityMappingRepo.create(new DSEntityMapping());
    }

    private ResultDto<JSONObject> sendReq(HttpInput req_input) {
        ResultDto<JSONObject> resultDto = HttpUtil.executeHttpRequest(req_input, new TypeReference<ResultDto<JSONObject>>() {
        });
        return resultDto;
    }

    private Map<String, String> buildDagReqParam(DAGInfo dagInfo, DAGSchedule dagSchedule) {
        String name = buildWorkflowName(dagInfo);
        Integer timeout = getDagTimeout(dagInfo.getEnvironment());
        String tenantCode = getDSTenantCode(dagInfo.getEnvironment());
        List<String> globalParams = Lists.newArrayList();
        String description = "";
        String scheduleJson = buildScheduleJson(dagSchedule);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("name", name);
        paramMap.put("timeout", timeout.toString());
        paramMap.put("tenantCode", tenantCode);
        paramMap.put("globalParams", new Gson().toJson(globalParams));
        paramMap.put("description", description);
        paramMap.put("scheduleJson", scheduleJson);
        return paramMap;
    }

    private HttpInput buildHttpReq(Map<String, String> req_param, String req_url, String req_method, String token) {
        HttpInput input = new HttpInput();
        input.setServerName("[Dolphin Scheduler]");
        input.setUri(req_url);
        input.setMethod(req_method);
        input.setQueryParamMap(req_param);
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("token", token);
        return input;
    }

    private String buildScheduleJson(DAGSchedule dagSchedule) {
        //cron json: {"startTime":"2021-11-22 00:00:00","endTime":"2021-12-31 00:00:00","crontab":"0 0 3/5 ? * 2/2 *","timezoneId":"Asia/Shanghai"}
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JsonObject cronJson = new JsonObject();
        cronJson.addProperty("startTime", dateFormat.format(dagSchedule.getBeginTime()));
        cronJson.addProperty("endTime", dateFormat.format(dagSchedule.getEndTime()));
        cronJson.addProperty("crontab", dateFormat.format(dagSchedule.getCronExpression()));
        cronJson.addProperty("timezoneId", "Asia/Shanghai");

        JsonObject scheduleJson = new JsonObject();
        scheduleJson.addProperty("warningType", PARAM_DEFAULT_WARNING_TYPE);
        scheduleJson.addProperty("warningGroupId", PARAM_DEFAULT_NOTIFY_GROUP_ID);
        scheduleJson.addProperty("failureStrategy", PARAM_DEFAULT_FAILURE_POLICY);
        scheduleJson.addProperty("workerGroup", "default");
        scheduleJson.addProperty("environmentCode", -1L);
        scheduleJson.addProperty("processInstancePriority", PARAM_DEFAULT_PROCESS_INSTANCE_PRIORITY);
        scheduleJson.add("cronJson", cronJson);
        return scheduleJson.toString();
    }

    private Long getWorkflowCode(Long dagId, String environment) throws ExternalIntegrationException {
        Optional<Long> optional = dsEntityMappingRepo.queryDsEntityCode(dagId, ENTITY_TYPE, environment);
        if (optional.isEmpty())
            throw new ExternalIntegrationException("工作流code不存在");
        return optional.get();
    }

    private Map<String, String> buildDagStatusChangedParam(Integer status) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("releaseState", UsingStatusEnum.ONLINE.val == status ? "ONLINE" : "OFFLINE");
        return paramMap;
    }

    @Override
    public void update(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/s%/process-definition/s%/basic-info", projectCode, workflowCode);
        String req_method = "PUT";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagReqParam(dagInfo, dagSchedule);
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("更新DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void delete(Long dagId, String environment) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(environment);
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagId, environment);
        String req_url = getDSBaseUrl(environment) + String.format("/projects/s%/process-definition/s%/delete-workflow", projectCode, workflowCode);
        String req_method = "DELETE";
        String token = getDSToken(environment);

        HttpInput req_input = buildHttpReq(null, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("删除DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void online(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/s%/process-definition/s%/release-workflow", projectCode, workflowCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagStatusChangedParam(dagInfo.getStatus());
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("上线DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void offline(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 根据环境获取ds project code
        String projectCode = getDSProjectCode(dagInfo.getEnvironment());
        // 获取工作流code
        Long workflowCode = getWorkflowCode(dagInfo.getId(), dagInfo.getEnvironment());
        String req_url = getDSBaseUrl(dagInfo.getEnvironment()) + String.format("/projects/s%/process-definition/s%/release-workflow", projectCode, workflowCode);
        String req_method = "POST";
        String token = getDSToken(dagInfo.getEnvironment());

        Map<String, String> req_param = buildDagStatusChangedParam(dagInfo.getStatus());
        HttpInput req_input = buildHttpReq(req_param, req_url, req_method, token);
        ResultDto<JSONObject> resultDto = sendReq(req_input);
        if (!resultDto.isSuccess()) {
            throw new ExternalIntegrationException(String.format("下线DS工作流失败：s%", resultDto.getMsg()));
        }
    }

    @Override
    public void updateSchedule(DAGInfo dagInfo, DAGSchedule dagSchedule) throws ExternalIntegrationException {
        // 暂不单独实现schedule的更新，合并到update方法中
    }

    @Override
    public void run(DAGInfo dagInfo) throws ExternalIntegrationException {
        // 暂不实现
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void addDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, List<Long> dependenceDagIds) throws ExternalIntegrationException {
    }

    @Override
    @Deprecated
    public void removeDependence(DAGInfo currentDag, List<Long> jobInCurrentDag, Long dependenceDagId) throws ExternalIntegrationException {
    }

}
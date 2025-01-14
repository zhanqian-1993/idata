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

package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.service.job.JobExecuteConfigService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * job-basic-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 10:24
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs")
public class JobInfoController {

    private final JobInfoService jobInfoService;
    private final JobExecuteConfigService jobExecuteConfigService;
    private final UserAccessService userAccessService;

    @Autowired
    public JobInfoController(JobInfoService jobInfoService,
                             JobExecuteConfigService jobExecuteConfigService,
                             UserAccessService userAccessService) {
        this.jobInfoService = jobInfoService;
        this.jobExecuteConfigService = jobExecuteConfigService;
        this.userAccessService = userAccessService;
    }

    private final String JOB_MONITORING_ACCESS_CODE = "F_MENU_JOB_MONITORING";
    private final String DATA_DEVELOP_ACCESS_CODE = "F_MENU_DATA_DEVELOP";

    @ApiOperation(value = "查询job")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchName", value = "searchName", required = false, dataType = "String")
    })
    @GetMapping("")
    public RestResult<List<JobInfo>> getJobInfo(@RequestParam(value = "searchName", required = false) String searchName) {
        List<JobInfo> list = jobInfoService.getJobListByName(searchName);
        return RestResult.success(list);
    }

    /**
     * 获取作业类型
     *
     * @param catalog 作业分类，DI：数据集成作业（获取数据集成分类下的作业类型：离线作业、实时作业），为空时获取所有作业类型
     * @return
     */
    @GetMapping("/types")
    public RestResult<List<JobTypeDto>> getJobType(String catalog) {
        List<JobTypeEnum> typeEnumList;
        if (StringUtils.isEmpty(catalog)) {
            typeEnumList = Lists.newArrayList(JobTypeEnum.values());
        } else {
            typeEnumList = JobTypeEnum.getCatalogEnum(catalog).orElse(null);
        }

        List<JobTypeDto> dtoList = null;
        if (typeEnumList != null) {
            dtoList = typeEnumList.stream()
                    .map(JobTypeDto::from)
                    .collect(Collectors.toList());
        }
        return RestResult.success(dtoList);
    }

    @GetMapping("/catalogs")
    public RestResult<Set<String>> getJobCatalogs() {
        return RestResult.success(Arrays.stream(JobTypeEnum.values())
                .filter(jobType -> !"DI".equals(jobType.getCatalog())).collect(Collectors.toList())
                .stream().map(JobTypeEnum::getCatalog).collect(Collectors.toSet()));
    }

    /**
     * 新增作业
     *
     * @param jobInfoDto 作业基础信息
     * @return
     */
    @PostMapping
    public RestResult<JobInfoDto> addJobInfo(@RequestBody JobInfoDto jobInfoDto) throws IllegalAccessException {
        Long id = jobInfoService.addJob(jobInfoDto, OperatorContext.getCurrentOperator());
        if (Objects.isNull(id)) return RestResult.error("新增作业失败", "");

        return getJobInfo(id);
    }

    /**
     * 编辑作业信息
     *
     * @param jobInfoDto 作业基础信息
     * @return
     */
    @PutMapping
    public RestResult<JobInfoDto> editJobInfo(@RequestBody JobInfoDto jobInfoDto) throws IllegalAccessException {
        Boolean ret = jobInfoService.editJobInfo(jobInfoDto, OperatorContext.getCurrentOperator());
        if (BooleanUtils.isFalse(ret)) return RestResult.error("编辑作业失败", "");

        return getJobInfo(jobInfoDto.getId());
    }

    /**
     * 获取作业信息
     *
     * @param id 作业id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<JobInfoDto> getJobInfo(@PathVariable Long id) {
        return RestResult.success(jobInfoService.getJobInfo(id));
    }

    /**
     * 删除作业
     *
     * @param id 作业id
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> removeJob(@PathVariable Long id) throws IllegalAccessException {
        return RestResult.success(jobInfoService.removeJob(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 获取已配置的作业列表
     *
     * @param environment
     * @return
     */
    @GetMapping("/environments/{environment}/jobs")
    public RestResult<List<JobAndDagDto>> getConfiguredJobList(@PathVariable("environment") String environment) {
        return RestResult.success(jobExecuteConfigService.getConfiguredJobList(environment));
    }

    /**
     * 恢复作业
     *
     * @param id 作业id
     * @return
     */
    @PutMapping("/{id}/environments/{environment}/resume")
    public RestResult<Boolean> resumeJob(@PathVariable Long id,
                                         @PathVariable("environment") String environment) {
        return RestResult.success(jobInfoService.resumeJob(id, environment, OperatorContext.getCurrentOperator()));
    }

    /**
     * 暂停作业
     *
     * @param id 作业id
     * @return
     */
    @PutMapping("/{id}/environments/{environment}/pause")
    public RestResult<Boolean> pauseJob(@PathVariable Long id,
                                        @PathVariable("environment") String environment) {
        return RestResult.success(jobInfoService.pauseJob(id, environment, OperatorContext.getCurrentOperator()));
    }

    /**
     * 运行作业
     *
     * @param id 作业id
     * @return
     */
    @PostMapping("/{id}/environments/{environment}/run")
    public RestResult<Boolean> runJob(@PathVariable Long id,
                                      @PathVariable("environment") String environment) {
        return RestResult.success(jobInfoService.runJob(id, environment, OperatorContext.getCurrentOperator()));
    }

    /**
     * 测试作业
     *
     * @param jobId   作业id
     * @param version 作业版本
     * @return
     */

    @PostMapping("/{jobId}/dryRun/{version}")
    public RestResult<JobDryRunDto> dryRun(@PathVariable Long jobId,
                                           @PathVariable Integer version) {
        return RestResult.success(jobInfoService.dryRunJob(jobId, version));
    }

    /**
     * 查询悬垂作业
     *
     * @param condition
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping("/overhangPage")
    public RestResult<OverhangJobWrapperDto> pagingOverhangJob(JobInfoCondition condition,
                                                               @RequestParam(value = "limit") Long limit,
                                                               @RequestParam(value = "offset") Long offset) throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), JOB_MONITORING_ACCESS_CODE)) {
            throw new IllegalAccessException("没有任务监控权限");
        }
        return RestResult.success(jobInfoService.pagingOverhangJob(condition, PageParam.of(limit, offset)));
    }

}

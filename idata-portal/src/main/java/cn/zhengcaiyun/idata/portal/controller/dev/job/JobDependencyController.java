package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.job.JobDependencyService;
import cn.zhengcaiyun.idata.portal.model.response.dag.JobTreeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("任务依赖")
@RestController
@RequestMapping("/p1/dev/jobs/dependency")
public class JobDependencyController {

    @Autowired
    private JobDependencyService jobDependencyService;

    @Autowired
    private JobScheduleManager jobScheduleManager;


    @ApiOperation("加载树")
    @GetMapping("/{id}/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "preLevel", value = "上游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "nextLevel", value = "下游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "name", value = "搜索任务名", dataType = "String", required = true)
    })
    public RestResult<JobTreeResponse> tree(@PathVariable("id") Long jobId,
                                            @RequestParam("env") String env,
                                            @RequestParam("preLevel") Integer preLevel,
                                            @RequestParam("nextLevel") Integer nextLevel,
                                            @RequestParam("searchJobId") Long searchJobId) {
        Tuple2<JobTreeNodeDto, JobTreeNodeDto> tuple2 = jobDependencyService.loadTree(jobId, env, preLevel, nextLevel, searchJobId);
        JobTreeResponse response = new JobTreeResponse();
        JobTreeNodeDto prev = tuple2.e1;
        JobTreeNodeDto next = tuple2.e2;

        JobTreeResponse.JobNode rPrev = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(prev, rPrev);
        JobTreeResponse.JobNode rNext = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(next, rNext);

        //设置层级
        response.setPrevLevel(prev.getLevel());
        response.setNextLevel(next.getLevel());

        response.setParents(rPrev.getNextList());
        response.setChildren(rNext.getNextList());
        response.setJobId(rPrev.getJobId());
        response.setJobName(rPrev.getJobName());

        return RestResult.success(response);
    }

    @ApiOperation("查看作业日志")
    @GetMapping("/{id}/running/log")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "lineNum", value = "查看行数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "skipLineNum", value = "跳过行数", dataType = "String", required = true)
    })
    public RestResult<String> getRunningLog(@PathVariable(value = "id", required = true) Long jobId,
                                            @RequestParam(value = "env", required = true) String env,
                                            @RequestParam(value = "taskId", required = true) Integer taskId,
                                            @RequestParam(value = "lineNum", required = false, defaultValue = "100") Integer lineNum,
                                            @RequestParam(value = "skipLineNum", required = false, defaultValue = "0")Integer skipLineNum) {
        String log = jobScheduleManager.queryJobRunningLog(jobId, env, taskId, lineNum, skipLineNum);
        return RestResult.success(log);
    }

    @ApiOperation("重跑作业")
    @GetMapping("/{id}/rerun")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "runPost", value = "是否重跑下游", dataType = "Boolean", required = true)
    })
    public RestResult<Boolean> rerun(@PathVariable(value = "id", required = true) Long jobId,
                                            @RequestParam(value = "env", required = true) String env,
                                            @RequestParam(value = "runPost", required = false, defaultValue = "false") boolean runPost) {
        jobScheduleManager.runJob(jobId, env, runPost);
        return RestResult.success(true);
    }

}
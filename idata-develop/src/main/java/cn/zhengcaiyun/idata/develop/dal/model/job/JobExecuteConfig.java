package cn.zhengcaiyun.idata.develop.dal.model.job;

import java.util.Date;
import javax.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dev_job_execute_config
 */
public class JobExecuteConfig {
    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.id")
    private Long id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.del")
    private Integer del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.creator")
    private String creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.create_time")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.editor")
    private String editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.edit_time")
    private Date editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.job_id")
    private Long jobId;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.environment")
    private String environment;

    /**
     * Database Column Remarks:
     *   调度配置-dag编号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dag_id")
    private Long schDagId;

    /**
     * Database Column Remarks:
     *   调度配置-重跑配置，always：皆可重跑，failed：失败后可重跑，never：皆不可重跑
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_rerun_mode")
    private String schRerunMode;

    /**
     * Database Column Remarks:
     *   调度配置-超时时间，单位：秒
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out")
    private Integer schTimeOut;

    /**
     * Database Column Remarks:
     *   调度配置-是否空跑，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dry_run")
    private Integer schDryRun;

    /**
     * Database Column Remarks:
     *   运行配置-队列
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_queue")
    private String execQueue;

    /**
     * Database Column Remarks:
     *   运行配置-作业最大并发数，配置为0时表示使用默认并发数
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_max_parallelism")
    private Integer execMaxParallelism;

    /**
     * Database Column Remarks:
     *   运行配置-告警等级
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_warn_level")
    private String execWarnLevel;

    /**
     * Database Column Remarks:
     *   调度配置-超时策略，alarm：超时告警，fail：超时失败，都有时用,号分隔
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out_strategy")
    private String schTimeOutStrategy;

    /**
     * Database Column Remarks:
     *   调度配置-优先级，1：低，2：中，3：高
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_priority")
    private Integer schPriority;

    /**
     * Database Column Remarks:
     *   运行配置-驱动器内存
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_driver_mem")
    private Integer execDriverMem;

    /**
     * Database Column Remarks:
     *   运行配置-执行器内存
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_worker_mem")
    private Integer execWorkerMem;

    /**
     * Database Column Remarks:
     *   作业运行状态（环境级），0：暂停运行；1：恢复运行
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.running_state")
    private Integer runningState;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.del")
    public Integer getDel() {
        return del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.del")
    public void setDel(Integer del) {
        this.del = del;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.creator")
    public String getCreator() {
        return creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.editor")
    public String getEditor() {
        return editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.edit_time")
    public Date getEditTime() {
        return editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.edit_time")
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.job_id")
    public Long getJobId() {
        return jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.environment")
    public String getEnvironment() {
        return environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.environment")
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dag_id")
    public Long getSchDagId() {
        return schDagId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dag_id")
    public void setSchDagId(Long schDagId) {
        this.schDagId = schDagId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_rerun_mode")
    public String getSchRerunMode() {
        return schRerunMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_rerun_mode")
    public void setSchRerunMode(String schRerunMode) {
        this.schRerunMode = schRerunMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out")
    public Integer getSchTimeOut() {
        return schTimeOut;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out")
    public void setSchTimeOut(Integer schTimeOut) {
        this.schTimeOut = schTimeOut;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dry_run")
    public Integer getSchDryRun() {
        return schDryRun;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_dry_run")
    public void setSchDryRun(Integer schDryRun) {
        this.schDryRun = schDryRun;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_queue")
    public String getExecQueue() {
        return execQueue;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_queue")
    public void setExecQueue(String execQueue) {
        this.execQueue = execQueue;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_max_parallelism")
    public Integer getExecMaxParallelism() {
        return execMaxParallelism;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_max_parallelism")
    public void setExecMaxParallelism(Integer execMaxParallelism) {
        this.execMaxParallelism = execMaxParallelism;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_warn_level")
    public String getExecWarnLevel() {
        return execWarnLevel;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_warn_level")
    public void setExecWarnLevel(String execWarnLevel) {
        this.execWarnLevel = execWarnLevel;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out_strategy")
    public String getSchTimeOutStrategy() {
        return schTimeOutStrategy;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_time_out_strategy")
    public void setSchTimeOutStrategy(String schTimeOutStrategy) {
        this.schTimeOutStrategy = schTimeOutStrategy;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_priority")
    public Integer getSchPriority() {
        return schPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.sch_priority")
    public void setSchPriority(Integer schPriority) {
        this.schPriority = schPriority;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_driver_mem")
    public Integer getExecDriverMem() {
        return execDriverMem;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_driver_mem")
    public void setExecDriverMem(Integer execDriverMem) {
        this.execDriverMem = execDriverMem;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_worker_mem")
    public Integer getExecWorkerMem() {
        return execWorkerMem;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.exec_worker_mem")
    public void setExecWorkerMem(Integer execWorkerMem) {
        this.execWorkerMem = execWorkerMem;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.running_state")
    public Integer getRunningState() {
        return runningState;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_execute_config.running_state")
    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }
}
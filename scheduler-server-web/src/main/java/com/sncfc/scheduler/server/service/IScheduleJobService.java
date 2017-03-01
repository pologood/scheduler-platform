package com.sncfc.scheduler.server.service;


import com.sncfc.scheduler.server.pojo.Pager;
import com.sncfc.scheduler.server.pojo.Project;
import com.sncfc.scheduler.server.pojo.ScheduleJob;
import com.sncfc.scheduler.server.pojo.ScheduleLog;

import java.util.List;
import java.util.Map;

public interface IScheduleJobService {


    /**
     * 新增
     * 
     * @param ScheduleJob
     * @return
     */
    Long insert(ScheduleJob ScheduleJob);

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     * 
     * @param ScheduleJob
     */
    int update(ScheduleJob ScheduleJob);

    /**
     * 删除
     * 
     * @param scheduleJobId
     */
    void delete(Long scheduleJobId);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void runOnce(Long scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void pauseJob(Long scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void resumeJob(Long scheduleJobId);

    /**
     * 获取任务对象
     * 
     * @param scheduleJobId
     * @return
     */
    ScheduleJob get(Long scheduleJobId);

//    /**
//     * 查询任务列表
//     *
//     * @param ScheduleJob
//     * @return
//     */
//    List<ScheduleJob> queryList(ScheduleJob ScheduleJob);
//
//    /**
//     * 获取运行中的任务列表
//     *
//     * @return
//     */
//    List<ScheduleJob> queryExecutingJobList();

    /**
     * 启动任务
     * @param scheduleJobId
     */
    void startJob(Long scheduleJobId);



    /**
     * 更新日志
     * @param fireInstanceId
     * @param responseMap
     * @return
     */
    int updateScheduleLog(String fireInstanceId, Map responseMap);
    /**
     * 执行日志增加
     * @param scheduleLog
     * @return
     */
    int addScheduleLog(ScheduleLog scheduleLog);


    /**
     * 是否存在该日志
     * @param nodeName
     * @param fireInstanceId
     * @return
     */
    boolean existedScheduleLog(String nodeName, String fireInstanceId);

    /**
     * 更新
     * @param requestList
     * @return
     */
    List<String> updateScheduleLog(List requestList);

    /**
     * 新增JOB
     * @param scheduleJob
     * @return
     */
    Long addScheduleJob(ScheduleJob scheduleJob);

    /**
     * 查询任务列表 分页
     * @param paramsMap
     * @return
     */
    Pager queryJobList(Map paramsMap);

    /**
     * 查询任务主明细和参数明细
     * @param id
     * @return
     */
    ScheduleJob getScheduleJobAndParams(long id);

    /**
     * 获取项目列表
     * @return
     */
    List<Project> getProjectList();

    /**
     * 更新任务
     * @param scheduleJob
     * @return
     */
    void modifyJobDetail(ScheduleJob scheduleJob);

    /**
     * 查询 触发列表按天
     * @param days
     * @return
     */
    List countTriggersList(int days);
    /**
     * 查询触发数 按天
     * @param days
     * @return
     */
    Map countTriggers(int days);

    /**
     * 查询总触发数
     * @return
     */
    Map countAllTriggers();
}

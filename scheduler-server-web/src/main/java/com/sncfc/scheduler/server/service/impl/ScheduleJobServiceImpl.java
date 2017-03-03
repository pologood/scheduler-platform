package com.sncfc.scheduler.server.service.impl;

import com.sncfc.scheduler.server.Exception.ResultException;
import com.sncfc.scheduler.server.dao.IScheduleJobDao;
import com.sncfc.scheduler.server.pojo.*;
import com.sncfc.scheduler.server.service.IScheduleJobService;
import com.sncfc.scheduler.server.util.ScheduleUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service("scheduleJobService")
@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
public class ScheduleJobServiceImpl implements IScheduleJobService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);
    /**
     * 调度工厂Bean
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private IScheduleJobDao schedulerJobDao;


    @Override
    public Long addScheduleJob(ScheduleJob scheduleJob) throws ResultException{
        try {
            Long jobId = schedulerJobDao.insertScheduleJob(scheduleJob);
            schedulerJobDao.insertScheduleJobParams(jobId,scheduleJob.getParamsList());
            return jobId;
        }catch (Exception e){
            logger.error("新增定时器任务异常",e);
            throw new ResultException();
        }
    }

    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public Pager queryJobList(Map paramsMap) {
        Pager pager = new Pager();
        if(!StringUtils.isEmpty(paramsMap.get("pageSize"))){
            pager.setPageSize(Integer.parseInt(paramsMap.get("pageSize").toString()));
        }
        if(!StringUtils.isEmpty(paramsMap.get("curPage"))){
            pager.setCurPage(Integer.parseInt(paramsMap.get("curPage").toString()));
        }
        Integer counts = schedulerJobDao.countScheduleJob(paramsMap);
        List list = schedulerJobDao.queryScheduleJobList(pager.getCurPage(),pager.getPageSize(),paramsMap);
        pager.setTotalRow(counts);
        pager.setQueryList(list);
        return pager;
    }

    @Override
    public ScheduleJob getScheduleJobAndParams(long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        List<ScheduleJobParams> scheduleJobParamsList = schedulerJobDao.queryScheduleJobParams(scheduleJobId);
        scheduleJob.setParamsList(scheduleJobParamsList);
        return scheduleJob;
    }

    @Override
    public List<Project> getProjectList() {
        return schedulerJobDao.queryProjectList();
    }



    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public Long insert(ScheduleJob scheduleJob) {
        Long id = schedulerJobDao.insertScheduleJob(scheduleJob);
        return id;
    }

    @Override
    public void startJob(Long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        schedulerJobDao.updateJobStatus(scheduleJobId, ScheduleJob.DOING);
    }


    @Override
    public int update(ScheduleJob editScheduleJob) {
//        schedulerJobDao.updateJob(editScheduleJob);
//        schedulerJobDao.deleteScheduleJobParams(editScheduleJob.getScheduleJobId());
//        schedulerJobDao.insertScheduleJobParams(editScheduleJob.getScheduleJobId(),editScheduleJob.getParamsList());
//        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(editScheduleJob.getScheduleJobId());
//        if (scheduleJob.getStatus() == ScheduleJob.DOING) {
//            //运行中的任务
//            ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
//            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
//        }
//        int result = schedulerJobDao.updateJob(scheduleJob);
//        return result;
          return 0;
    }

    @Override
    public void modifyJobDetail(ScheduleJob editScheduleJob) {
        schedulerJobDao.updateJob(editScheduleJob);
        schedulerJobDao.deleteScheduleJobParams(editScheduleJob.getScheduleJobId());
        schedulerJobDao.insertScheduleJobParams(editScheduleJob.getScheduleJobId(),editScheduleJob.getParamsList());
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(editScheduleJob.getScheduleJobId());
        if (scheduleJob.getStatus() == ScheduleJob.DOING) {
            //运行中的任务
            ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        }
    }


    @Override
    public void delete(Long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        if (scheduleJob.getStatus() == ScheduleJob.DOING) {
            //删除运行的任务
            ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        }
        //逻辑删除数据
        schedulerJobDao.updateJobStatus(scheduleJobId, ScheduleJob.DELETE);
    }



    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public void runOnce(Long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        AsyncJobFactory.trigger(scheduleJob.getScheduleJobId(),UUID.randomUUID().toString(),ScheduleLog.CUSTOMER_TRIGGER);
    }

    @Override
    public void pauseJob(Long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        //删除任务
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //更新状态
        schedulerJobDao.updateJobStatus(scheduleJobId, ScheduleJob.PAUSED);
    }

    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public void resumeJob(Long scheduleJobId) {
        ScheduleJob scheduleJob = schedulerJobDao.queryScheduleJob(scheduleJobId);
        ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public ScheduleJob get(Long scheduleJobId) {
        return schedulerJobDao.queryScheduleJob(scheduleJobId);
    }

    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public int addScheduleLog(ScheduleLog scheduleLog) {
        return schedulerJobDao.insertScheduleLog(scheduleLog);
    }


    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public boolean existedScheduleLog(String nodeName, String fireInstanceId) {
        int counts = schedulerJobDao.existedScheduleLog(nodeName,fireInstanceId) ;
        if(counts > 0){
            return true;
        }
        return false;
    }
    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public boolean existedScheduleLog(String fireInstanceId) {
        int counts = schedulerJobDao.existedScheduleLog(fireInstanceId) ;
        if(counts > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<String> updateScheduleLog(List requestList) {
        Set<String> nodeSet = new HashSet();
        for (Object schLog : requestList) {
            Map log = (Map)schLog;
            String nodeName = log.get("nodeName") == null ? "":log.get("nodeName").toString();
            nodeSet.add(nodeName);
            String fireInstanceId = log.get("fireInstanceId")== null ? "":log.get("fireInstanceId").toString();
            boolean executed = log.get("executed") == null ? false:Boolean.parseBoolean(log.get("executed").toString());
            String exceptionMsg = log.get("rootExceptionClassName")== null ? "":log.get("rootExceptionClassName").toString();
            schedulerJobDao.updateLogStatus(fireInstanceId,executed,exceptionMsg);
        }
        List <String> unCompleteIds = schedulerJobDao.queryUnCompleteJob(nodeSet);
        return unCompleteIds;
    }



    @Override
    public int updateScheduleLog(String fireInstanceId, Map responseMap) {
        return 0;
    }



    @Override
    public List countTriggersList(int days) {
        return schedulerJobDao.countTriggersList(days);
    }

    @Override
    public Map countTriggers(int days) {
        return schedulerJobDao.countTriggers(days);
    }

    @Override
    public Map countAllTriggers() {
        Map map = new HashMap();
        List<Map> jobList = schedulerJobDao.countJobs();
        List<Map> triggersList = schedulerJobDao.countAllTriggers();
        for (Map tmap : jobList) {
            map.put("job" + tmap.get("STATUS"),tmap.get("NUMS"));
        }
        for (Map tmap : triggersList) {
            map.put("trigger" + tmap.get("SUCCESS"),tmap.get("SUCESS_NUMS"));
        }
        return map;
    }

    @Override
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public int updateScheduleLog(ScheduleLog scheduleLog) {
        return schedulerJobDao.updateScheduleLog(scheduleLog);
    }
}

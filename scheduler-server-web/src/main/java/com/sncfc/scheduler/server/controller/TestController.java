package com.sncfc.scheduler.server.controller;

import com.sncfc.scheduler.server.pojo.ScheduleJob;
import com.sncfc.scheduler.server.service.IScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 123 on 2017/1/19.
 */
@Controller
public class TestController {

    @Autowired
    private IScheduleJobService scheduleJobService;

    /**
     * 新增
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testAddJob")
    public String testAddJob(){
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setProjectId(100000L);
        scheduleJob.setJobAliasName("自己起的");
        scheduleJob.setSync("1");
        scheduleJob.setUrl("http://localhost:8080/quartzClient/jobDispatcher");
        scheduleJob.setJobType("1");
        scheduleJob.setCronExpression("0/20 * * * * ?");
        scheduleJob.setJobDesc("描述");
        scheduleJobService.insert(scheduleJob);
        return "add success";
    }

    /**
     * 暂停
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testPauseJob")
    public String testPauseJob(){
        Long scheduleJobId = 100031L;
        scheduleJobService.pauseJob(scheduleJobId);
        return "暂停success";
    }

    /**
     * 恢复--执行
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testResumeJob")
    public String testResumeJob(){
        Long scheduleJobId = 100031L;
        scheduleJobService.startJob(scheduleJobId);
        return "恢复success";
    }

    /**
     * 执行一次
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testTriggerJob")
    public String testTriggerJob(){
        Long scheduleJobId = 100031L;
        scheduleJobService.runOnce(scheduleJobId);
        return "";
    }

    /**
     * 删除任务
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testDeleteJob")
    public String testDeleteJob(){
        Long scheduleJobId = 100031L;
        scheduleJobService.delete(scheduleJobId);
        return "delete success";
    }
    /**
     * 更新任务
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testUpdateJob")
    public String testUpdateJob(){
        Long scheduleJobId = 100031L;
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setScheduleJobId(scheduleJobId);
        scheduleJobService.update(scheduleJob);
        return "updateSuccess";
    }

}

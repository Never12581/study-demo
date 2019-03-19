package com.hzx.sort;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobTest {

    public static void main(String[] args) throws Throwable {
        SchedulerFactory factory = new StdSchedulerFactory();
        // 从工厂里面拿到一个scheduler实例
        Scheduler scheduler = factory.getScheduler();
        // 计算任务的开始时间，DateBuilder.evenMinuteDate方法是取下一个整数分钟
        // 真正执行的任务并不是Job接口的实例，而是用反射的方式实例化的一个JobDetail实例
        JobDataMap jdm = new JobDataMap();
        jdm.putAsString("aaa", 1);
        JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").setJobData(jdm).build();
        // 定义一个触发器，startAt方法定义了任务应当开始的时间
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0/1 * * * * ? ")
                )
                .build();
        // 将任务和Trigger放入scheduler
        scheduler.scheduleJob(job, trigger);

        scheduler.start();

        JobDataMap jdm2 = new JobDataMap();
        jdm2.putAsString("aaa", 22222);
        JobDetail job1 = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group2").setJobData(jdm2).build();
        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger1", "group2")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0/2 * * * ? ")
                )
                .build();
        scheduler.scheduleJob(job1, trigger1);

        try {
            // 等待65秒，保证下一个整数分钟出现，这里注意，如果主线程停止，任务是不会执行的
            Thread.sleep(65L * 100L);
        } catch (Exception e) {

        }
        // scheduler结束
        scheduler.shutdown(true);
    }

    public static class HelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " hello world !");
            JobDataMap map = context.getMergedJobDataMap();
            Integer i = map.getIntegerFromString("aaa");
            System.out.println(threadName + " get from jobDataMap : " + i);

            // so I can call other logic to perfect schedule task.
            // get case id , and execute it .
        }
    }

}


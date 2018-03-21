package com.louisz.zflow.schedule;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.louisz.zflow.dao.ScheduleDao;
import com.louisz.zflow.entity.PlanEntity;
import com.louisz.zflow.service.StartProcessService;
import com.louisz.zflow.util.ELParseUtil;
import com.louisz.zflow.util.JsonUtil;
import com.louisz.zflow.util.SpringUtil;

/**
 * class for establishing timed tasks and run those tasks
 * 
 * @author zhang
 * @description
 * @time 2018年2月27日
 */
@Component
public class PlanScheduler implements ApplicationListener<ApplicationReadyEvent> {

	private static Logger logger = LoggerFactory.getLogger(PlanScheduler.class);

	private static SchedulerFactory sf = new StdSchedulerFactory();
	private static String JOB_GROUP = "DEFAULT";
	private static String TRIGGER_GROUP = "DEFAULT";

	/**
	 * add timed tasks into scheduler by quartz
	 * 
	 * @author zhang
	 * @time 2018年3月21日下午2:16:23
	 * @param plan
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	private void addPlan(PlanEntity plan) throws SchedulerException, ParseException {

		// establishing timed tasks by using quartz
		JobDetail jobDetail = JobBuilder.newJob(PlanJob.class).withIdentity(plan.getId(), JOB_GROUP).build();
		jobDetail.getJobDataMap().put(plan.getId(), plan);

		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(plan.getId(), TRIGGER_GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(plan.getCron())).build();

		Scheduler sch = sf.getScheduler();
		sch.scheduleJob(jobDetail, trigger);
		sch.start();

	}

	/**
	 * clear all timed tasks in scheduler
	 * 
	 * @author zhang
	 * @time 2018年3月21日下午2:17:40
	 * @throws Exception
	 */
	public void clearSchedule() throws Exception {
		logger.info("Clearing all timed tasks in schedule...");
		try {
			Scheduler scheduler = sf.getScheduler();
			scheduler.clear();
		} catch (Exception e) {
			logger.error("Exception happend while clearing schedule!");
			throw e;
		}

	}

	protected static class PlanJob implements Job {

		private static Logger logger = LoggerFactory.getLogger(PlanJob.class);

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {

			PlanEntity plan = (PlanEntity) context.getJobDetail().getJobDataMap()
					.get(context.getJobDetail().getKey().getName());

			// Variables is a JSON formed String which contains some parameters need to be
			// parsed
			String var = ELParseUtil.reassemble(plan.getVariables());

			if (!JsonUtil.validate(var)) {
				logger.error("variables is not a json string : {} ", var);
				return;
			}

			try {
				@SuppressWarnings("unchecked")
				Map<String, String> args = JsonUtil.fromJson(var, Map.class);
				StartProcessService service = (StartProcessService) SpringUtil.getBean("startFlowService");
				logger.info("Scheduler start plan : {}", plan);
				// start those timed process executions
				service.doService(args);
			} catch (Exception e) {
				logger.error("start timed plan failed :" + plan, e);
			}

		}

	}

	/**
	 * start to add timed tasks into scheduler
	 * 
	 * @author zhang
	 * @time 2018年3月21日下午2:21:00
	 */
	public void start() {
		logger.info("Start to initializing scheduled plans...");
		// 取了AF_SCHEDULE表的所有数据，一行记录代表某个省某次对账
		ScheduleDao scheduleDao = (ScheduleDao) SpringUtil.getBean(ScheduleDao.class);
		List<PlanEntity> list = scheduleDao.selectAll();
		if (null == list || list.isEmpty()) {
			logger.info("There is no timed plan for schedule.");
		}
		for (PlanEntity p : list) {
			try {
				logger.info("Add Plan : {}", p);
				// 把Plan放到quartz的一个定时任务中
				addPlan(p);
			} catch (SchedulerException | ParseException e) {
				logger.error("Exception happend whend add plan :" + p, e);
			}
		}
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		this.start();
	}
}

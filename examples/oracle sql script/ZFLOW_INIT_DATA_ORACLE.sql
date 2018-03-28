------------insert 2 examples of process for test-----
DECLARE
	Proc_reparamonly_test CLOB:='<?xml version="1.0" encoding="UTF-8"?>
<process id="Proc_reparamonly_test" name="Proc_reparamonly_test">
	<start name="start">
		<transition to="fork" />
	</start>
	<fork name="fork">
		<transition to="Test_Proc" />
		<transition to="test" />
	</fork>
	<process id="Test_Proc" name="Test_Proc">
		<transition to="join" />
	</process>
	<task name="test" refUri="http://ZFLOW/clientTest">
		<repeat interval="50" parameters="(testxx1=XXXXXX1)|((testxx2=XXXXXX2))" />
		<transition to="join" />
	</task>
	<join name="join">
		<transition to="end" />
	</join>
	<end name="end" />
</process>';
	Test_Proc CLOB:='<?xml version="1.0" encoding="UTF-8"?>
<process id="Test_Proc" name="Test_Proc">
	<start name="start">
		<transition to="test" />
	</start>
	<task name="test" refUri="http://ZFLOW/clientTest">
		<transition to="end" />
	</task>
	<end name="end" />
</process>';
BEGIN
Insert into ZF_PROCESS (ID,NAME,STATE,CONTENT,CREATETIME,UPDATETIME) values ('Proc_reparamonly_test','Proc_reparamonly_test',0,Proc_reparamonly_test,'20180319 17:18:15','20180319 17:18:15');
Insert into ZF_PROCESS (ID,NAME,STATE,CONTENT,CREATETIME,UPDATETIME) values ('Test_Proc','Test_Proc',0,Test_Proc,'20180319 17:22:26','20180319 17:22:26');
END;
/
COMMIT;



-------------------------insert a schedule for test-----------------
Insert into ZF_SCHEDULE (ID,PROCESSID,FLOWNAME,CRON,VARIABLES,STATE,CREATETIME,UPDATETIME) values ('SCH_TEST_01','Test_Proc','Test_Proc','* 1 * * * ?','{"processId":"Test_Proc","settleDate":#{com.louisz.zflow.util.DateUtil.getDate(-1)}}',1,'20180328-10:00',null);
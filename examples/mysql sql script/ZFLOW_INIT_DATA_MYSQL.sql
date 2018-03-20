------------insert 2 examples of process for test-----
BEGIN
	DECLARE Proc_reparamonly_test TEXT;
	SET Proc_reparamonly_test='<?xml version="1.0" encoding="UTF-8"?>
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
	DECLARE Test_Proc TEXT;
	SET Test_Proc='<?xml version="1.0" encoding="UTF-8"?>
<process id="Test_Proc" name="Test_Proc">
	<start name="start">
		<transition to="test" />
	</start>
	<task name="test" refUri="http://ZFLOW/clientTest">
		<transition to="end" />
	</task>
	<end name="end" />
</process>';
Insert into ZF_PROCESS (ID,NAME,STATE,CONTENT,CREATETIME,UPDATETIME) values ('Proc_reparamonly_test','Proc_reparamonly_test',0,Proc_reparamonly_test,'20180319 17:18:15','20180319 17:18:15');
Insert into ZF_PROCESS (ID,NAME,STATE,CONTENT,CREATETIME,UPDATETIME) values ('Test_Proc','Test_Proc',0,Test_Proc,'20180319 17:22:26','20180319 17:22:26');
END;
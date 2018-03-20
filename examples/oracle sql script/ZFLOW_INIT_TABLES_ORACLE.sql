
  CREATE TABLE "ZF_PROCESS" 
   (	"ID" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"NAME" VARCHAR2(64 BYTE), 
	"STATE" NUMBER(10,0), 
	"CONTENT" CLOB, 
	"CREATETIME" VARCHAR2(30 BYTE), 
	"UPDATETIME" VARCHAR2(30 BYTE), 
	 PRIMARY KEY ("ID")
	 );
	COMMENT ON TABLE "ZF_PROCESS"  IS 'table for storing process entities';commit;


  CREATE TABLE "ZF_FLOW" 
   (	"ID" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"NAME" VARCHAR2(100 BYTE), 
	"PROCESSID" VARCHAR2(64 BYTE), 
	"PROCESSNAME" VARCHAR2(100 BYTE), 
	"STATE" VARCHAR2(20 BYTE), 
	"VARIABLES" VARCHAR2(4000 BYTE), 
	"CREATETIME" VARCHAR2(30 BYTE), 
	"UPDATETIME" VARCHAR2(30 BYTE), 
	"FINISHTIME" VARCHAR2(30 BYTE), 
	"FATHER_FLOW_ID" VARCHAR2(64 BYTE),
	 PRIMARY KEY ("ID")
  ) ;
	COMMENT ON TABLE "ZF_FLOW"  IS 'table for process executions';commit;
   

  CREATE TABLE "ZF_SCHEDULE" 
   (	"ID" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"PROCESSID" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"FLOWNAME" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"CRON" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"VARIABLES" VARCHAR2(4000 BYTE), 
	"STATE" NUMBER(11,0) NOT NULL ENABLE, 
	"CREATETIME" VARCHAR2(30 BYTE), 
	"UPDATETIME" VARCHAR2(30 BYTE),  
	 PRIMARY KEY ("ID")
	 ) ;
  COMMENT ON TABLE "ZF_SCHEDULE"  IS 'table for timed plans';COMMIT;


  CREATE TABLE "ZF_TASK" 
   (	"ID" VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"NAME" VARCHAR2(64 BYTE), 
	"FLOWID" VARCHAR2(64 BYTE), 
	"FLOWNAME" VARCHAR2(64 BYTE), 
	"PROCESSID" VARCHAR2(64 BYTE), 
	"PROCESSNAME" VARCHAR2(64 BYTE), 
	"STATE" VARCHAR2(20 BYTE), 
	"VARIABLES" VARCHAR2(4000 BYTE), 
	"CREATETIME" VARCHAR2(30 BYTE), 
	"UPDATETIME" VARCHAR2(30 BYTE), 
	"FINISHTIME" VARCHAR2(30 BYTE), 
	"SETTLEDATE" VARCHAR2(30 BYTE), 
	"NODE" VARCHAR2(60 BYTE), 
	"MESSAGE" VARCHAR2(500 BYTE), 
	 PRIMARY KEY ("ID")
   );
 COMMENT ON TABLE "ZF_TASK"  IS 'table for task executions';COMMIT;

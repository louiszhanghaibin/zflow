ZFLOW--a compact and powerful workflow engine
Hi all! Here is the original developer's note for fellow developers of this project.
	1.ZFLOW is a simple JAVA WEB project combined with Spring features, like spring boot, spring cloud and etc;
	2.In the example folders, I provided some PROCESS examples, and give some SQL scripts for basic database deployment in ORACLE or MYSQL;
	3.Steps for first running:
		1).Deploy SQL scripts in data base like ORACLE or MYSQL, please note to choose different SQL script for different data base;
		2).If you clone the source code from Git repository, you can run the application by IDE. 
			Or, if you package the project into a runnable Jar file, then simply use the JAVA -JAR command to run this application;
		3).Open the index page of this application.If you did not change the sever port, then the index page URL must be "http://localhost:8088/zflow";
			Change the port part in the URL if you changed this application's sever port property before you request the index page;
		4).Run some test process as you want!
	
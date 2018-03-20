<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>
<title>zflow</title>	
</head>
<script type="text/javascript">
		function checkNull(id){
			var obj = document.getElementById(id);
			var value = obj.value;
			if(trim(value)==""){
				alert(id+"不能为空!");
				obj.focus();
				return false;
			}
			return true;
		}
		function trim(str){ //删除左右两端的空格
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function add() {  
	        var trObj = document.createElement("tr");  
	        trObj.id = new Date().getTime();  
	        trObj.innerHTML = "<td><input name='key'/></td><td><input name='value'/></td><td><input type='button' value='Del' onclick='del(this)'></td>";  	        
	        document.getElementById("spTbody").appendChild(trObj);  
	    }  
	  
	    function del(obj) {  
	        var trId = obj.parentNode.parentNode.id;  
	        var trObj = document.getElementById(trId);  
	        document.getElementById("spTbody").removeChild(trObj);  
	    } 
	    
	    
	function getFormToJson(tbId) {
		var mytable = document.getElementById(tbId);
		var map = {};
		var data = [];
		for (var i = 0, rows = mytable.rows.length; i < rows; i++) {
			for (var j = 0, cells = mytable.rows[i].cells.length; j < cells; j++) {
				if (!data[i]) {
					data[i] = new Array();
				}
				var temp = mytable.rows[i].cells[j]
						.getElementsByTagName("input")[0];
				if ("undefined" == typeof (temp)
						&& null != mytable.rows[i].cells[j].innerText) {
					data[i][j] = mytable.rows[i].cells[j].innerText.replace(
							/(^\s*)|(\s*$)|(\:\*)|(\*)/g, "");
					if (null == data[i][j] || 0 == data[i][j].length) {
						data[i][j] = mytable.rows[i].cells[j]
								.getElementsByTagName('textarea')[0].value;
					}
				} else {
					data[i][j] = mytable.rows[i].cells[j]
							.getElementsByTagName("input")[0].value;
				}
			}
			map[data[i][0]] = data[i][1];
		}
		var result = JSON.stringify(map);
		alert("Sending these parameters : " + result);
		return map;
	}

	function submitByAjax(addr, dt) {
		//post json by ajax
		$.ajax({
			url : addr,
			type : 'post',
			data : JSON.stringify(dt),
			contentType : 'application/json;charset=UTF-8',
			success : function(result) {
				alert(result);
			},
			error : function(jqXHR, status, err) {
				var msg = "ERROR[status=" + status
						+ "]!Exception happened while connecting to URL["
						+ addr + "]!" + err;
				alert(msg);
			}
		});
	}

	function doSubmit(id, addr, tbId) {
		if (!checkNull(id)) {
			return;
		}
		var data = getFormToJson(tbId);
		submitByAjax(addr, data);
	}

	function submitDeploy(id, url) {
		var value = document.getElementById(tbId)
		$.ajax({

		})
	}
</script>
<body>
	<h1>zflow</h1><hr>
	
	
	<h3>start process</h3>
	<h5>please input custom parameters below :</h5>
	<form action="startProcess" method="post" onsubmit="return checkNull('processId')" id="spForm">
		<table border="0" id="spTable">  
            <thead>  
                <tr>  
                    <td  align="left">key</td>
                    <td  align="left">: value</td>
                    <td>
                    	<input type="button" value="Add" onclick="add()">   
                    </td>
                </tr>  
            <thead>  
            <tbody id="spTbody">  
                <tr id="1st">
                    <td align="left">processId<span style="color: red">*</span></td>  
                    <td align="left"><input  id="processId" name="processId" value="(流程唯一ID,如:Proc_miGuTest)"  align="left"></td>
                </tr>
                <tr id="2nd">
					<td align="right"><input value="settleDate" align="left"></td>
					<td align="left"><input value="(账期日，如：20180101)" align="left"></td>
					<td>
                    	<input type="button" value="Del" onclick="del(this)">
                    </td>
				</tr>
				<tr id="3rd">
					<td align="right"><input value="province" align="left"></td>
					<td align="left"><input value="(省代码，如：100)" align="left"></td>
					<td>
                    	<input type="button" value="Del" onclick="del(this)">
                    </td>
				</tr>
				<tr id="4th">
					<td align="right"><input value="busiLine" align="left"></td>
					<td align="left"><input value="(业务线编码，如：0088)" align="left"></td>
					<td>
                    	<input type="button" value="Del" onclick="del(this)">
                    </td>
				</tr>
            </tbody>  
            <tr>
				<td align="right"></td><td align="left"><input type="button" value="submit" onclick="doSubmit('processId', '/startProcess', 'spTbody')">&nbsp;<input type="reset" value="reset"></td>
			</tr>
        </table>
	</form>
	
	
	<br/><br/><br/>
	<h3>deploy process</h3>
	<h5>please input the absolute file path of a process file below for deploying :</h5>
	<form action="/deployProcess" method="post" onsubmit="return checkNull('deployFilePath')">
		<table id="dpTable">
			<tbody id="dpTbody">
			<tr>
				<td align="right">processFilePath:<span style="color: red">*</span></td><td align="left"><input id="processFilePath" name="processFilePath"  value="(流程文件路径名，如:/opt/local/example_proc.xml)" style="width:300px"></td>
			</tr>
			<tr>
				<td ><input value="type" type="hidden"></td>
				<td ><input value="path" type="hidden"></td>
			</tr>
			</tbody>
			<tr>
				<td align="right"></td><td align="left"><input type="button" value="submit" onclick="doSubmit('processFilePath', '/deployProcess', 'dpTbody')">&nbsp;<input type="reset" value="reset"></td>
			</tr>
		</table>
	</form>	
	
	
	
	<h5>OR</h5>
	<h5>please input or copy the exact content of a process file below for deploying :</h5>
	<form action="/deployProcess" method="post" onsubmit="return checkNull('deployFilePath')" onkeydown="if(event.keyCode==13){return 9;}">
		<table id="dpcTable">
			<tbody id="dpcTbody">
			<tr>
				<td align="right">processFileContent:<span style="color: red">*</span></td>
				<td align="left">
					<textarea id="processFilePath" name="processFilePath" style="width:500px;height:300px;" >(流程文件内容，如:
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;process id=&quot;Test_Proc&quot; name=&quot;Test_Proc&quot;&gt;
	&lt;start name=&quot;start&quot;&gt;
		&lt;transition to=&quot;test&quot; /&gt;
	&lt;/start&gt;
	&lt;task name=&quot;test&quot; refUri=&quot;http&#58;//ZFLOW/clientTest&quot;&gt;
		&lt;transition to=&quot;end&quot; /&gt;
	&lt;/task&gt;
	&lt;end name=&quot;end&quot; /&gt;
&lt;/process&gt;)</textarea>
				</td>
			</tr>
			<tr>
				<td ><input value="type" type="hidden"></td>
				<td ><input value="content" type="hidden"></td>
			</tr>
			</tbody>
			<tr>
				<td align="right"></td><td align="left"><input type="button" value="submit" onclick="doSubmit('processFilePath', '/deployProcess', 'dpcTbody')">&nbsp;<input type="reset" value="reset"></td>
			</tr>
		</table>
	</form>
		
</body>
</html>
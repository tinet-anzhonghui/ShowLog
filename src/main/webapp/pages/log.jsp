<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE>
<html>
<head>
<base href="<%=basePath%>">

<title>tail log</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
<link href="css/jsTree/style.min.css" rel="stylesheet">
<link href="css/style.min.css?v=4.0.0" rel="stylesheet">
<base target="_blank">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jsTree/jstree.min.js"></script>
<!-- websocket连接的重连 -->
<script src="js/websocket/reconnecting-websocket.min.js"></script>


</head>
<body>
	<div class="wrapper wrapper-content  animated fadeInRight">
		<div class="row" style="margin-top:10px; line-height:35px;">
			<label class="col-sm-1 control-label" style="padding-left:35px; ">日志路径：</label>
			<div class="col-sm-2" style="padding-left: 0px;">
				<div class="form-group">
					<input id="filePath" type="text" class="form-control"
						name="filePath" placeholder="请输入日志路径（tab提醒）" />
				</div>
			</div>
			<label class="col-sm-1 control-label" style="padding-left:35px; ">显示行数：</label>
			<div class="col-sm-2" style="padding-left: 0px;">
				<div class="form-group">
					<input id="displayRowNum" type="text" class="form-control"
						name="displayRowNum" placeholder="默认全部显示" />
				</div>
			</div>
			<div class="col-sm-1">
				<button id="init" type="button" class="btn btn-success">初始化</button>
			</div>
			<div class="col-sm-1">
				<button id="empty" type="button" class="btn btn-danger">清空</button>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-3">
				<div class="ibox float-e-margins">
					<div class="ibox-title">读取路径下的文件</div>
					<div class="ibox-content">
						<div id="js_tree"></div>
					</div>
				</div>
			</div>
			<div class="col-sm-9">
				<div id="log-container"
					style="height: 600px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px;">
					<div></div>
				</div>
			</div>
		</div>
	</div>
</body>
<!-- websocket的初始化 -->
<script>
	$(document).ready(function() {
		
		// 获取本机的外网IP
		var contextPath;
		$.ajax({
			url : '${pageContext.request.contextPath}/getIPV4',
			type : 'GET',
			async : false, // 同步获取请求的地址
			success : function(result) {
				contextPath = result;
			}
		});
		// 指定websocket路径
		/* var websocket = new WebSocket('ws://${pageContext.request.contextPath}/log');
		websocket.onmessage = function(event) {
		    // 接收服务端的实时日志并添加到HTML页面中
		    $("#log-container div").append(event.data);
		    // 滚动条滚动到最低部
		    $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
		}; */

		/**
         *  功能概述：建立websocket连接 
         *  编写人： 安仲辉
         *  时间：2018.8.18
         */
		//全局变量们
		var websocket = null;
		//判断当前浏览器是否支持WebSocket 49.4.69.9
		if ('WebSocket' in window) {
			websocket = new ReconnectingWebSocket('ws://' + contextPath + '/websocket'); //${pageContext.request.contextPath}
		//websocket = new WebSocket("ws://localhost:8080/WEControlSystem/websocket/INQUIRE_NOW");
		} else {
			alert('对不起，您使用的浏览器版本较低，为了带来更好的体验，请升级你的浏览器或换成谷歌浏览器')
		}

		//连接发生错误的回调方法
		websocket.onerror = function() {
			console.log("error");
		};

		//连接成功建立的回调方法
		websocket.onopen = function(event) {
			console.log("open");
		}

		//接收到消息的回调方法
		websocket.onmessage = function(event) {
			console.log("收到的消息：" + event.data);
			// 接收服务端的实时日志并添加到HTML页面中
			$("#log-container div").append(event.data);
			// 滚动条滚动到最低部
			$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
		}

		//连接关闭的回调方法
		websocket.onclose = function() {
			console.log("close");
		}

		//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		window.onbeforeunload = function() {
			websocket.close();
		}

		//关闭连接
		function closeWebSocket() {
			websocket.close();
		}

		//发送消息
		function send(msg) {
			websocket.send(msg);
		}
	});
</script>

<!-- bootstrap-treeview 树状图、初始化按钮、显示行数输入框-->
<script type="text/javascript">
	var js_tree = $('#js_tree');
	$("#init").click(function() {
		var filePath = $("#filePath").val();
		$
			.ajax({
				url : '${pageContext.request.contextPath}/initLog',
				type : 'POST', //PUT DELETE POST
				data : "filePath=" + filePath,
				success : function(result) {
					js_tree.jstree(true).settings.core.data = result;
					js_tree.jstree(true).refresh();
				}
			});
	});

	$(function() {
		js_tree.jstree({
			'core' : {
				'data' : null
			}
		});

		js_tree.bind("activate_node.jstree", function(obj, e) {
			// 处理代码
			// 获取当前节点
			var currentNode = e.node;
			var text = currentNode.text;
			// 获取文件后缀
			var startIndex = text.lastIndexOf(".");
			if (startIndex == -1) {
				return;
			}
			var endIndex = text.length;
			var suffix = text.substring(startIndex + 1, endIndex); //后缀名

			// 获取显示的行数
			var displayRowNum = $("#displayRowNum").val();
			var regex = /^\+?[0-9][0-9]*$/; //判断是否为正整数 
			if (displayRowNum != null && displayRowNum != '') {
				if (!regex.test(displayRowNum)) {
					alert("请输入正整数或零")
					return;
				}
			}

			//alert(suffix);
			$.ajax({
				url : "${pageContext.request.contextPath}/startTail",
				type : "GET",
				data : "id=" + currentNode.id + "&displayRowNum=" + displayRowNum,
				success : function(result) {
					console.log("监控成功");
				}
			});
		});
	})
</script>

<!-- 文件路径，tab键的监听 -->
<script type="text/javascript">
	/*
	   tab键的监听
	*/
	document.onkeydown = function(e) {
		if (window.event.keyCode == 9) {
			// 禁用tab键
			e.which = 0;
			e.preventDefault();
			// 获取提醒和前半段的路径
			var filePath = $("#filePath").val();
			if (filePath == null || filePath == "") {
				return;
			}
			/* var startIndex = filePath.lastIndexOf("\\");
			if (startIndex == -1) {
			    return;
			}
			var endIndex = filePath.length;
			var remind = filePath.substring(startIndex+1, endIndex);
			var path = filePath.substring(0, startIndex);
			if (remind == null) {
			    return;
			} */
			$.ajax({
				url : "${pageContext.request.contextPath}/remind",
				type : "POST",
				data : "filePath=" + filePath,
				success : function(result) {
					$("#filePath").val(result);
				}
			});
		}
	}
</script>

<!-- 清除功能 -->
<script type="text/javascript">
	$("#empty").click(function() {
		$("#log-container div").empty();
	});
</script>

</body>
</html>
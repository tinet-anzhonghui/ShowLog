<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>tail log</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/treeview/bootstrap-treeview.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/treeview/bootstrap-treeview.js"></script>

</head>
<body>
	<div class="wrapper wrapper-content  animated fadeInRight">
		<div class="row" style="margin-top:20px; line-height:35px;">
			<label class="col-sm-1 control-label" style="padding-left:35px; ">日志路径：</label>
			<div class="col-sm-2" style="padding-left: 0px;">
				<div class="form-group">
					<input id="filePath" type="text" class="form-control"
						name="filePath" placeholder="请输入日志路径" />
				</div>
			</div>
			<div class="col-sm-1">
				<button id="init" type="button" class="btn btn-primary">初始化</button>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-3">
				<div class="ibox float-e-margins">
					<div class="ibox-title"></div>
					<div class="ibox-content">
						<div id="treeview1" class="test"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="log-container"
		style="height: 450px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px;">
		<div></div>
	</div>
</body>
<script>
	$(document).ready(function() {
		// 指定websocket路径
		var websocket = new WebSocket('ws://localhost:8080/log');
		websocket.onmessage = function(event) {
			// 接收服务端的实时日志并添加到HTML页面中
			$("#log-container div").append(event.data);
			// 滚动条滚动到最低部
			$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
		};
	});
</script>

<!-- bootstrap-treeview -->
<script type="text/javascript">
	$("#init").click(function() {
		var filePath = $("#filePath").val();
		alert(filePath);
		$
		.ajax({
			url : '${pageContext.request.contextPath}/logServlet',
			type : 'POST', //PUT DELETE POST
			data : "filePath =" + filePath,
			success : function(result) {
				$("#treeview1").treeview({
					color : "#428bca",
					//expandIcon : "glyphicon glyphicon-chevron-right",
					//collapseIcon : "glyphicon glyphicon-chevron-down",
					//nodeIcon : "glyphicon glyphicon-folder-close",
					expandIcon : "glyphicon glyphicon-folder-close",
					collapseIcon : "glyphicon glyphicon-folder-open",
					nodeIcon : "glyphicon",//设置不显示默认图标
					data : result
				})
			}
		});
	});
	$(function() {
//		var t = '[{"text": "父节点 1","nodes": [{"text": "子节点 1","nodes": [{"text": "孙子节点 1", "icon": "glyphicon glyphicon-file"},{"text": "孙子节点 2"}]},{"text": "子节点 2"}]},{"text": "父节点 2"},{"text": "父节点 3"},{"text": "父节点 4"},{"text": "父节点 5"}]';
		var t = '[{"text":"hxInvoicingSystemLogs","nodes":[{"text":"company","nodes":[{"text":"company.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"delivery","nodes":[{"text":"delivery.log","icon":"glyphicon glyphicon-cloud-download"},{"text":"delivery.log.2018-12-18","icon":"glyphicon glyphicon-cloud-download"},{"text":"delivery.log.2018-12-21","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"delivery.log","icon":"glyphicon glyphicon-cloud-download"},{"text":"exception","nodes":[{"text":"exception.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"expense","nodes":[{"text":"expense.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"reduction","nodes":[{"text":"reduction.log","icon":"glyphicon glyphicon-cloud-download"},{"text":"reduction.log.2018-12-18","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"role","nodes":[{"text":"role.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"salesman","nodes":[{"text":"salesman.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"summary","nodes":[{"text":"summary.log","icon":"glyphicon glyphicon-cloud-download"}]},{"text":"user","nodes":[{"text":"user.log","icon":"glyphicon glyphicon-cloud-download"},{"text":"user.log.2018-12-18","icon":"glyphicon glyphicon-cloud-download"},{"text":"user.log.2018-12-21","icon":"glyphicon glyphicon-cloud-download"}]}]}]';
		$("#treeview1").treeview({
			color : "#428bca",
			//expandIcon : "glyphicon glyphicon-chevron-right",
			//collapseIcon : "glyphicon glyphicon-chevron-down",
			//nodeIcon : "glyphicon glyphicon-folder-close",
			expandIcon : "glyphicon glyphicon-folder-close",
			collapseIcon : "glyphicon glyphicon-folder-open",
			nodeIcon : "glyphicon",//设置不显示默认图标
			data : t
		})
	});
</script>
</body>
</html>
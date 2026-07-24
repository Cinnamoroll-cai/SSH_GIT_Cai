<!DOCTYPE html>
<html>
<head>
	<title>供应商管理</title>
	<#include "../common.ftl">
</head>
<body class="childrenBody">

<form class="layui-form" >
	<blockquote class="layui-elem-quote quoteBox">
		<form class="layui-form">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input type="text" name="supplierName"
						   class="layui-input
					searchVal" placeholder="供应商名称" />
				</div>
				<a class="layui-btn search_btn" data-type="reload"><i
							class="layui-icon">&#xe615;</i> 搜索</a>
			</div>
		</form>
	</blockquote>
	<table id="supplierList" class="layui-table"  lay-filter="suppliers"></table>

	<script type="text/html" id="toolbarDemo">
		<div class="layui-btn-container">
			<a class="layui-btn layui-btn-normal addNews_btn" lay-event="add">
				<i class="layui-icon">&#xe608;</i>
				添加
			</a>
			<a class="layui-btn layui-btn-normal delNews_btn" lay-event="del">
				<i class="layui-icon">&#xe672;</i>
				删除
			</a>
		</div>
	</script>
	<!--操作-->
	<script id="roleListBar" type="text/html">
		<a class="layui-btn layui-btn-xs" id="edit" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
	</script>
</form>
<!--
	supplier.js 会初始化这个表格，自动向后台请求数据。
	它请求的接口是：/supplier/list
	请求参数：page（当前页）、limit（每页条数）、supplierName（搜索条件）
-->
<script type="text/javascript" src="${ctx.contextPath}/js/supplier/supplier.js"></script>

</body>
</html>
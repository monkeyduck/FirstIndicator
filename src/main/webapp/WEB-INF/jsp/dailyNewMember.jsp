<%--
  Created by IntelliJ IDEA.
  User: zoey
  Date: 16/10/29
  Time: 上午11:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.llc.model.FirstIndicator" %>
<%@ page import="com.llc.model.Member" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String url = request.getQueryString();
    String exportToExcel = request.getParameter("exportToExcel");
    if(exportToExcel != null && exportToExcel.toString().equalsIgnoreCase("YES")){
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition","inline; filename=" + "dailyNewMember.xls");
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>日新增用户详情</title>
    <link type="text/css" href='<c:url value="/resources/bootstrap-table/bootstrap-table.css"></c:url>' rel="stylesheet" >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Example of Fixed Layout with Bootstrap version 2.0 from ziqiangxuetang.com">
    <meta name="author" content="">
    <!-- Le styles -->
    <link type="text/css" href='<c:url value="/resources/bootstrap/css/bootstrap.css"></c:url>' rel="stylesheet"/>
    <link type="text/css" href='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/css/example-fluid-layout.css"></c:url>' rel="stylesheet"/>
    <%--<link href="../bootstrap/twitter-bootstrap-v2/docs/assets/css/example-fixed-layout.css" rel="stylesheet">--%>
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body>
<p id="chartData" style="display: none">${chartData}</p>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="text-center" style="margin-bottom: 8px">
        <h2>日新增用户详情</h2>
    </div>

    <!-- Example row of columns -->
    <div class="row" id="table_data">
        <table class="table table-striped" cellpadding="2px" cellspacing="2px">
            <thead>
            <tr>
                <th>ID</th>
                <th>日期</th>
                <th>Member ID</th>
                <th>Device ID</th>
                <th>昵称</th>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>注册时间</th>
                <th>用户类型</th>
                <th>版本</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${memberList}" var="member" varStatus="loop">
                <tr>
                    <td>${loop.count}</td>
                    <td>${member.date}</td>
                    <td>${member.member_id}</td>
                    <td>${member.device_id}</td>
                    <td>${member.nick_name}</td>
                    <td>${member.real_name}</td>
                    <td>${member.gender}</td>
                    <td>${member.age}</td>
                    <td>${member.signup_time}</td>
                    <td>${member.member_type}</td>
                    <td>${member.version}</td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>
    <div class="row">
        <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
        <div id="main" style="width: 960px;height:400px; text-align: center"></div>
        <div class="btn-success" style="float:right">
            <button class="btn-success" id="exportExcel" onclick="TableToExcel()">导出excel</button>
        </div>
    </div>

    <hr>

    <footer>
        <p>&copy; Zoey 2016</p>
    </footer>
</div> <!-- /container -->
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src='<c:url value="/resources/jquery-3.1.0.min.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap-table/bootstrap-table.min.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap-table/bootstrap-table.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap-table/bootstrap-table-zh-CN.js"></c:url>'></script>
<script src='<c:url value="/resources/echarts.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/jquery.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-transition.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-dropdown.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-scrollspy.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-tab.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-tooltip.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-popover.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-button.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-collapse.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-carousel.js"></c:url>'></script>
<script src='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/js/bootstrap-typeahead.js"></c:url>'></script>

</body>
<script type="text/javascript">
    function TableToExcel() {
        window.location.href = '<%=basePath%>stat/dailyNewMember?<%=url%>&exportToExcel=YES';
    }
</script>
</html>
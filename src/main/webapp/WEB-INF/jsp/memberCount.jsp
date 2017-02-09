<%--
  Created by IntelliJ IDEA.
  User: zoey
  Date: 16/10/29
  Time: 上午11:30
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Date" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String url = request.getQueryString();
    String exportToExcel = request.getParameter("exportToExcel");
    if(exportToExcel != null && exportToExcel.toString().equalsIgnoreCase("YES")){
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition","inline; filename=" + "memberCount.xls");
    }
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>各版本人数详情</title>
    <link type="text/css" href='<c:url value="/resources/bootstrap-table/bootstrap-table.css"></c:url>' rel="stylesheet" >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Example of Fixed Layout with Bootstrap version 2.0 from ziqiangxuetang.com">
    <meta name="author" content="">
    <link type="text/css" href='<c:url value="/resources/bootstrap/css/bootstrap.css"></c:url>' rel="stylesheet"/>
    <link type="text/css" href='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/css/example-fluid-layout.css"></c:url>' rel="stylesheet"/>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <link rel="stylesheet" href="jqueryui/style.css">
    <script src="//apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js" src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js">
        $(function() {
            $( "#start_datepicker" ).datepicker();
            $( "#end_datepicker" ).datepicker();
        });
    </script>
</head>
<body>
<p id="chartData" style="display: none">${chartData}</p>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="text-center" style="margin-bottom: 8px">
        <h2>各版本人数详情</h2>
    </div>

    <div class="row" style="margin-bottom: 10px">
        <select style="float:left" class="selectpicker" id="isValidSelect" style="width: 40px">
            <c:forEach items="${isValidList}" var="isValid">
                <option value="${isValid}" <c:if test="${isValid eq is}">selected="selected"</c:if> >${validTypeName[isValid]}</option>
            </c:forEach>
            <%--<option value="all">全部用户</option>--%>
            <%--<option value="true">活跃用户</option>--%>
        </select>

        <div style="float: left">
            <select style="float:left" class="selectpicker" id="userTypeSelect" style="width: 20px">
                <c:forEach items="${userTypeList}" var="userType">
                    <option value="${userType}" <c:if test="${userType eq utype}">selected="selected"</c:if> >${typeName[userType]}</option>
                </c:forEach>
                <%--<option value="real">真实用户</option>--%>
                <%--<option value="formal">正式用户</option>--%>
                <%--<option value="indoor ">入户用户</option>--%>
                <%--<option value="market">市场用户</option>--%>
                <%--<option value="innerTest">公司测试</option>--%>
                <%--<option value="gray">灰度用户</option>--%>
                <%--<option value="vip">VIP用户</option>--%>
            </select>
        </div>

        <div style="float: left">
            <input type="text" id="start_datepicker" value="${start}" style="height: 30px" />
        </div>
        <div style="float: left">
            <input type="text" id="end_datepicker" value="${end}" style="height: 30px" />
        </div>

        <div style="float: right">
            <label style="padding-right: 10px">
                <button class="btn btn-default" id="button_date" onclick="displayMemberCount()">查询</button>
            </label>
        </div>
    </div>

    <!-- Example row of columns -->
    <div class="row" id="table_data">
        <table class="table table-striped" cellpadding="2px" cellspacing="2px">
            <thead>
            <tbody>
            <tr>
                <th>日期</th>
                <th>版本</th>
                <th>使用人数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${memberList}" var="member">
                <tr>
                    <td>${member.date}</td>
                    <td>${member.version}</td>
                    <td>${member.member_count}</td>
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
        <p>&copy; zoey 2016</p>
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

    function displayMemberCount() {
        var member_type = document.getElementById("userTypeSelect").value;
        var isValid = document.getElementById("isValidSelect").value;
        var begin = document.getElementById("start_datepicker").value;
        var end = document.getElementById("end_datepicker").value;
        window.location.href = '<%=basePath%>stat/memberCount?isValid='+isValid+'&member_type='+member_type+'&start_date='+begin+'&end_date='+end;
    }

    function TableToExcel() {
        window.location.href = '<%=basePath%>stat/memberCount?<%=url%>&exportToExcel=YES';
    }

</script>
</html>
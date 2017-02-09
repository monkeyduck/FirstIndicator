<%--
  Created by IntelliJ IDEA.
  User: zoey
  Date: 16/11/18
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
        response.setHeader("Content-Disposition","inline; filename=" + "TTSLatency.xls");
    }
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>讯飞合成时延详情</title>
    <link type="text/css" href='<c:url value="/resources/bootstrap-table/bootstrap-table.css"></c:url>' rel="stylesheet" >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Example of Fixed Layout with Bootstrap version 2.0 from ziqiangxuetang.com">
    <meta name="author" content="">
    <link type="text/css" href='<c:url value="/resources/bootstrap/css/bootstrap.css"></c:url>' rel="stylesheet"/>
    <link type="text/css" href='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/css/example-fluid-layout.css"></c:url>' rel="stylesheet"/>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <link rel="stylesheet" href="jqueryui/style.css">
</head>
<body>
<p id="chartData" style="display: none">${chartData}</p>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="text-center" style="margin-bottom: 8px">
        <h2>讯飞合成时延详情</h2>
    </div>

    <div class="row">
        <div class="btn-success" style="float:right">
            <button class="btn-success" id="exportExcel" onclick="TableToExcel()">导出excel</button>
        </div>
    </div>

    <div class="row" id="table_data">
        <table class="table table-striped" cellpadding="2px" cellspacing="2px">
            <thead>
            <tbody>
        <tr>
            <th>ID</th>
            <th>日期</th>
            <th>Member ID</th>
            <th>均值</th>
            <th>最大值</th>
            <th>上四分位</th>
            <th>下四分位</th>
            <th>最小值</th>
        </tr>
            </thead>
            <tbody>
            <c:forEach items="${xfTTSLatencyList}" var="xfLatency" varStatus="loop">
                <tr>
                    <td>${loop.count}</td>
                    <td>${xfLatency.date}</td>
                    <td>${xfLatency.member_id}</td>
                    <td>${xfLatency.avg}</td>
                    <td>${xfLatency.max}</td>
                    <td>${xfLatency.min}</td>
                    <td>${xfLatency.first_quarter}</td>
                    <td>${xfLatency.third_quarter}</td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
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
    function TableToExcel() {
        window.location.href = '<%=basePath%>stat/xfTTSLatency?<%=url%>&exportToExcel=YES';
    }
</script>
</html>
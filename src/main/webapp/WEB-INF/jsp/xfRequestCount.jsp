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
        response.setHeader("Content-Disposition","inline; filename=" + "requestCount.xls");
    }
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>讯飞请求次数统计</title>
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

<body onload="chart(1)">
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <div class="nav-collapse">
                <ul class="nav">
                    <li class="active"><a href="<%=basePath%>stat/xfRequestCount">讯飞请求</a></li>
                    <li class="active"><a href="<%=basePath%>stat/xfDailyRecLatency">听写时延</a></li>
                    <li class="active"><a href="<%=basePath%>stat/xfDailyTTSLatency">合成时延</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<p id="chartData" style="display: none">${chartData}</p>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="text-center" style="margin-bottom: 8px">
        <h2>讯飞请求次数统计</h2>
    </div>

    <div class="row" style="margin-bottom: 10px">
        <div style="float: left">
            <select style="float:left" class="selectpicker" id="versionSelect" style="width: 40px">
                <option value="">全部版本</option>
                <c:forEach items="${versionList}" var="version">
                    <option value="${version}" <c:if test="${version eq ver}">selected="selected"</c:if> >${version}</option>
                </c:forEach>
            </select>
            <select style="float:left" class="selectpicker" id="userTypeSelect" style="width: 20px">
            <c:forEach items="${userTypeList}" var="userType">
                <option value="${userType}" <c:if test="${userType eq utype}">selected="selected"</c:if> >${typeName[userType]}</option>
            </c:forEach>
            </select>
        </div>

        <div style="float: left">
            <input type="text" id="start_datepicker" value="${start_date}" style="height: 30px" />
        </div>
        <div style="float: left">
            <input type="text" id="end_datepicker" value="${end_date}" style="height: 30px" />
        </div>

        <div style="float: right">
            <label style="padding-right: 10px">
                <button class="btn btn-default" id="button_date" onclick="displayRequestCount()">查询</button>
            </label>
        </div>
    </div>

        <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 960px;height:400px; text-align: center"></div>

    <div class="row" style="margin-bottom: 10px">
        <div class="btn-success" style="float:right"  style="margin-bottom: 10px">
            <button class="btn-success" id="exportExcel" onclick="TableToExcel()">导出excel</button>
        </div>
    </div>

    <!-- Example row of columns -->
    <div class="row" id="table_data" style="margin-top: 8px">
        <table class="table table-striped" cellpadding="2px" cellspacing="2px">
            <thead>
            <tbody>
            <tr>
                <th>明细数据</th><th></th><th></th><th></th>
            </tr>
            </tbody>
            <tbody>
            <tr>
                <th>ID</th>
                <th>时间</th>
                <th>用户数量</th>
                <th>请求总数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${xfRequestList}" var="xfRequest" varStatus="loop">
                <tr>
                    <td>${loop.count}</td>
                    <td>${xfRequest.date}</td>
                    <td><a onclick="displayRequestDetail('${xfRequest.date}');">${xfRequest.member_count}</a> </td>
                    <td>${xfRequest.total_request}</td>
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
    function displayRequestCount() {
        var member_type = document.getElementById("userTypeSelect").value;
        var begin = document.getElementById("start_datepicker").value;
        var end = document.getElementById("end_datepicker").value;
        var version = document.getElementById("versionSelect").value;
        window.location.href = '<%=basePath%>stat/xfRequestCount?member_type='+member_type+'&version='+version+'&start_date='+begin+'&end_date='+end;
    }

    function displayRequestDetail(date) {
        var member_type = document.getElementById("userTypeSelect").value;
        var version = document.getElementById("versionSelect").value;
        window.location.href = '<%=basePath%>stat/xfRequestDetail?member_type='+member_type+'&version='+version+'&date='+date;
    }

    function TableToExcel() {
        window.location.href = '<%=basePath%>stat/xfRequestCount?<%=url%>&exportToExcel=YES';
    }

    function chart(index) {
        // 基于准备好的dom，初始化echarts实例
        var title_list = ['用户数量', '请求总数'];
        var title_name = title_list[index];
        var label_list = ['人数', '数量'];
        var label = label_list[index];
        var myChart = echarts.init(document.getElementById('main'));
        var showData = eval($('#chartData').text())[index];
        var xaxis = eval($('#chartData').text())[2];
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: title_name
            },
            tooltip: {},
            legend: {
                data: [label]
            },
            xAxis: {
                type: "category",
                data: xaxis
            },
            yAxis: {},
            series: [{
                name: '',
                type: 'line',
                data: showData,

            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
</script>
</html>
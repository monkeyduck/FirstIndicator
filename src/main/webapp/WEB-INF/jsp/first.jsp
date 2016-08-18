<%--
  Created by IntelliJ IDEA.
  User: llc
  Date: 16/7/14
  Time: 下午5:39
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.llc.model.FirstIndicator" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <meta charset="utf-8">
    <%--<script type="text/javascript">--%>
        <%--$(document).ready(function(){--%>
            <%--$("button_date").click(function(){--%>
                <%--htmlobj=$.ajax({url:"<%=basePath%>stat/first?&type=date",async:false});--%>
                <%--$("#table_data").html(htmlobj);--%>
            <%--});--%>
            <%--$("button_hour").click(function(){--%>
                <%--htmlobj=$.ajax({url:"<%=basePath%>stat/first?&type=hour",async:false});--%>
                <%--$("#table_data").html(htmlobj);--%>
            <%--});--%>
        <%--});--%>
    <%--</script>--%>
    <title>一级指标</title>
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
                    <li class="active"><a href="<%=basePath%>log/index">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="text-center" style="margin-bottom: 8px">
        <h2>日志一级指标统计</h2>
    </div>

    <div class="row" style="margin-bottom: 10px">
        <select style="float:left" class="selectpicker" id="versionSelect" style="width: 40px">
            <option value="all">全部版本</option>
            <c:forEach items="${versionList}" var="version">
                <option value="${version}" <c:if test="${version eq ver}">selected="selected"</c:if> >${version}</option>
            </c:forEach>
        </select>

        <div style="float: left">
            <button class="btn" id="button_version" onclick="displayByVersion()">按版本筛选</button>
        </div>

        <div style="float: left">
            <select style="float:left" class="selectpicker" id="userTypeSelect" style="width: 20px">
                <option value="all">全部用户</option>
                <c:forEach items="${userTypeList}" var="userType">
                    <option value="${userType}" <c:if test="${userType eq utype}">selected="selected"</c:if> >${typeName[userType]}</option>
                </c:forEach>
            </select>
        </div>

        <div style="float: left">
            <button class="btn" id="button_usertype" onclick="displayByUserType()">按用户筛选</button>
        </div>

        <div style="float: right">
            <label style="padding-right: 10px">
                <button class="btn btn-default" id="button_date" onclick="displayByDate()">按日期</button>
            </label>
        </div>
        <div style="float:right">
            <label style="padding-right: 10px">
                <button class="btn btn-default" id="button_hour" onclick="displayByHour()">按时间</button>
            </label>
        </div>

    </div>


    <!-- Example row of columns -->
    <div class="row" id="table_data">
        <table class="table table-striped" cellpadding="2px" cellspacing="2px">
            <thead>
                <tr>
                    <th>时间</th>
                    <th>总用户数</th>
                    <th>日新增用户数</th>
                    <th>日活</th>
                    <th>日人均使用时长(分钟)</th>
                    <th>一日留存率</th>
                    <th>三日留存率</th>
                    <th>七日留存率</th>
                    <th>bug数</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>    </td>
                    <td height="80%"><button class="btn btn-default" onclick="chart(0)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(1)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(2)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(3)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(4)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(5)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(6)">画图</button> </td>
                    <td><button class="btn btn-default"  onclick="chart(7)">画图</button> </td>

                </tr>
            <c:forEach items="${firstStatList}" var="stat">
                <tr>
                    <td height="80%">${stat.displayTime}</td>
                    <td>${stat.totalUserNum}</td>
                    <td>${stat.dailyNewUser}</td>
                    <td>${stat.dailyActive}</td>
                    <td>${stat.avgUsedTimePerUser}</td>
                    <td>${stat.retention}</td>
                    <td>${stat.retention_3}</td>
                    <td>${stat.retention_7}</td>
                    <td>${stat.bugNum}</td>
                </tr>
            </c:forEach>
                <tr>
                    <c:forEach items="${compare}" var="val">
                        <td>${val}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>    </td>
                    <td><button class="btn btn-default" onclick="chart(0)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(1)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(2)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(3)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(4)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(5)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(6)">画图</button> </td>
                    <td><button class="btn btn-default" onclick="chart(7)">画图</button> </td>

                </tr>
            </tbody>

        </table>
    </div>
    <div class="row">
        <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
        <div id="main" style="width: 960px;height:400px; text-align: center"></div>
        <div class="btn-success" style="float:right">
            <button class="btn-success" id="exportExcel" >导出excel</button>
        </div>
    </div>

    <hr>

    <footer>
        <p>&copy; linchuan 2016</p>
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
    function displayByDate() {
        var userType = document.getElementById("userTypeSelect").value;
        window.location.href = '<%=basePath%>stat/userType?&type=date&userType='+userType;
    }
    function displayByHour() {
        var userType = document.getElementById("userTypeSelect").value;
        window.location.href = '<%=basePath%>stat/userType?type=hour&userType='+userType;
    }
    function displayByVersion() {
        var version = document.getElementById("versionSelect").value;
        window.location.href = '<%=basePath%>stat/version?version='+version+'&type=time';
    }

    function displayByUserType() {
        var userType = document.getElementById("userTypeSelect").value;
        window.location.href = '<%=basePath%>stat/userType?userType='+userType+'&type=time';
    }
    function chart(index){
        // 基于准备好的dom，初始化echarts实例
        var title_list = ['总用户数', '日新增用户数', '日活跃', '人均使用时长', '1日留存率', '3日留存率', '7日留存率','bug数'];
        var title_name = title_list[index];
        var label_list = ['人数', '人数', '人数', '分钟', '留存率', '留存率', '留存率', 'bug数'];
        var label = label_list[index];
        var myChart = echarts.init(document.getElementById('main'));
        var showData = eval($('#chartData').text())[index];
        var xaxis = eval($('#chartData').text())[8];
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: title_name
            },
            tooltip: {},
            legend: {
                data:[label]
            },
            xAxis: {
                type:"category",
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
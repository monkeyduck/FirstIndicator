<%--
  Created by IntelliJ IDEA.
  User: zoey
  Date: 16/11/16
  Time: 下午11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Log System</title>
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
                    <li class="active"><a href="#">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>
<div class="container">
    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="leaderboard">
        <h1>Welcome to Log System</h1>
        <p>You can analyse, download and search logs here</p>
        <%--<p><a class="btn btn-success btn-large">Sign Up for a 30 day free trial</a></p>--%>
    </div>
    <!-- Example row of columns -->
    <div class="row">
        <div class="span4">
            <h2>讯飞听写请求</h2>
            <p><a class="btn btn-success btn-large" href="<%=basePath%>stat/xfRequestCount">Enter</a></p>
        </div>
        <div class="span4">
            <h2>讯飞听写时延</h2>
            <p><a class="btn btn-success btn-large" href="<%=basePath%>stat/xfDailyRecLatency">Enter</a></p>
        </div>
        <div class="span4">
            <h2>讯飞合成时延</h2>
            <p><a class="btn btn-success btn-large" href="<%=basePath%>stat/xfDailyTTSLatency">Enter</a></p>
        </div>
    </div>
    <div class="row" style="margin-top: 100px">
        <div class="span4">
            <h2>应用评价标准</h2>
            <p><a class="btn btn-success btn-large" href="<%=serverPath%>websocket/websocket.jsp">Enter</a></p>
        </div>
        <div class="span4">
            <h2>用户历史轨迹</h2>
            <p><a class="btn btn-success btn-large" href="<%=basePath%>stat/xunfei">Enter</a></p>
        </div>
        <div class="span4">
            <h2>用户使用详情</h2>
            <p><a class="btn btn-success btn-large" href="<%=basePath%>log/wangmeng">Enter</a></p>
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
</html>

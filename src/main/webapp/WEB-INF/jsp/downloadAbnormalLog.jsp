<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Download abnormal logs</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>

    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

    <!-- DatePicker-->
    <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css"/>
    <style type="text/css">
        .vcenter {
            display: inline-block;
            vertical-align: middle;
            float: none;
        }

        .col-centered {
            float: none;
            margin: 0 auto;
        }
    </style>
</head>
<body>
<header>
    <div class="container" style="text-align: center">
        <h3>下载异常日志</h3>
    </div>
</header>
<div class="row">
    <div class="col-md-1 vcenter"></div>
    <select class="selectpicker" id="moduleSelect">
        <option value="">Module</option>
        <option value="GeneralGame">GeneralGame</option>
        <option value="dialog">dialog</option>
        <option value="WorldView">WorldView</option>
        <option value="handworkGuide">handworkGuide</option>
        <option value="cartoon">cartoon</option>
        <option value="music">music</option>
        <option value="story">story</option>
        <option value="FrontEnd">FrontEnd</option>
        <option value="preprocess">Preprocess</option>
    </select>
    <button id="download" onclick="download()" class="btn btn-default">下载日志</button>
</div>
</body>
<script type="text/javascript">
    function download() {
        var moduleName = document.getElementById("moduleSelect")
        window.location.href = '<%=basePath%>log/downloadAbnormal?&moduleName=' + moduleName;
    }
</script>
</html>
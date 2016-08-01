<%--
  Created by IntelliJ IDEA.
  User: llc
  Date: 16/7/7
  Time: 下午1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Log Analysis</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Example of Fixed Layout with Bootstrap version 2.0 from ziqiangxuetang.com">
    <meta name="author" content="">
    <!-- Le styles -->
    <link type="text/css" href='<c:url value="/resources/bootstrap/css/bootstrap.css"></c:url>' rel="stylesheet"/>
    <link type="text/css" href='<c:url value="/resources/bootstrap/twitter-bootstrap-v2/docs/assets/css/example-fluid-layout.css"></c:url>' rel="stylesheet"/>
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
                    <li class="active"><a href="<%=basePath%>log/index">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span4">
            <form method="POST" enctype="multipart/form-data" action="<%=basePath%>log/upload">
                <table class="table">
                    <tr>
                        <td>file:</td>
                        <td><input type="file" name="file" /></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" class="btn-success" value="Upload" /></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="span2">
            <h2>result</h2>
    <textarea class="text-left" rows="15" cols="60">
            ${message}
    </textarea>
        </div>
    </div>
    <div class="row">
        <div class="span2">
            <button class="btn-success" id="btn_download" onclick="download_result(${fileName})">下载结果</button>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <form method="POST" enctype="multipart/form-data" action="<%=basePath%>log/analyseAudio">
            <table class="table">
                <tr>
                    <td>file:</td>
                    <td><input type="file" name="file" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" class="btn-success" value="Upload" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>



<script type="text/javascript">
    function download_result(suffix) {

        window.location.href = '<%=basePath%>log/download_result?fileName='+suffix;
    }
    $('input[id=lefile]').change(function() {
        $('#input-1').val($(this).val());
    });

    function loading(isloading) {
        if (isloading) {
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        } else {
            $("#zhongxin").show();
            $("#zhongxin2").hide();
        }
    }

    function analyse() {
        loading(true)
        var formData = new FormData($('form#uploadform')[0]);
    }
    $('form#uploadform').submit(function () {
        var formData = new FormData($(this)[0]);
        $.ajax({
            url: '<%=basePath%>log/upload.do',
            type: 'POST',
            data: formData,
            success: function (data) {
                loading(false)
            },
            cache:false,
            contentType:false,
            processData:false})
        return false})


</script>
</body>
</html>

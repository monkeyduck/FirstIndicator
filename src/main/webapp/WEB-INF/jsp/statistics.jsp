<%--
  Created by IntelliJ IDEA.
  User: hxx
  Date: 3/9/16
  Time: 10:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>statistics</title>
    <script type="text/javascript" src="//cdn.jsdelivr.net/jquery/1/jquery.min.js"></script>
    <script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap/latest/css/bootstrap.css" />

    <%--<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.css" rel="stylesheet">--%>
    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" rel="stylesheet">
    <%--<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">--%>
    <%--<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.js"></script>--%>
    <script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <%--<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/npm.js"></script>--%>

    <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />

    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/1.10.7/css/jquery.dataTables.css">

    <!-- jQuery -->
    <%--<script type="text/javascript" charset="utf8" src="http://code.jquery.com/jquery-1.10.2.min.js"></script>--%>

    <!-- DataTables -->
    <script type="text/javascript" charset="utf8" src="http://cdn.datatables.net/1.10.7/js/jquery.dataTables.js"></script>

    <script src="http://echarts.baidu.com/dist/echarts.min.js"></script>
</head>
<body>
<p id="currentIndex" style="display: none">${currentIndex}</p>
<p id="maxPage" style="display: none">${totalPage}</p>
<p id="dateRangeBeginBack" style="display: none">${dateRangeBegin}</p>
<p id="dateRangeEndBack" style="display: none">${dateRangeEnd}</p>
<p id="optionRadiosBack" style="display: none">${optionRadios}</p>
<p id="chartXAxisData" style="display: none">${chartXAxisData}</p>
<p id="chartDataList" style="display: none">${chartDataList}</p>
<header>
    <div class="container" style="text-align: center">
        <h3>日志统计</h3>
    </div>
</header>
<div class="container" style="width: 90%">
<form class = "form-inline" role = "form" action="statistics.do" method="get" id ="queryForm">
    <div class="form-group">
        <input type="text" class="form-control" name="dateRangeBegin" value="" id="dateRangeBegin"/>
        <label>到</label>
        <input type="text" class="form-control" name="dateRangeEnd" value="" id="dateRangeEnd"/>
    </div>
    <div class="form-group radio" style="display: none;">
        <label>
            <input type="radio" name="optionRadios" id="optionRadioshour" value="perhour" checked>每小时
        </label>
        <label>
            <input type="radio" name="optionRadios" id="optionRadiosday" value="perday">每天
        </label>
    </div>
    <input type="button" onclick="firstGrade()" value="一级指标" class="btn btn-default" "/>
</form>
<div style="clear:both">

</div>
<div  align="center">
    <c:set var="shouldShowTable" scope="session" value="${tableContentList.size()}"/>
    <c:if test="${shouldShowTable > 0}">
    <table border="1" cellpadding="5" align="center" width="100%" >
        <tr>
            <c:forEach items="${tableHeadList}" var="tableHead" varStatus="status">
                <td style="text-align: center">
                <c:if test="${status.index == 2 || status.index == 4 || status.index == 8}">
                    <font color="#adff2f">
                </c:if>
                    ${tableHead}
                <c:if test="${status.index == 2 || status.index == 4 || status.index == 8}">
                    </font>
                    </c:if>
                </td>
            </c:forEach>
        </tr>
        <tr>
            <c:set var="tableHeadcount" scope="session" value="${tableHeadList.size()}"/>
            <c:if test="${tableHeadcount > 0}">
                <td></td>
            </c:if>
            <c:forEach items="${tableHeadList}" var="tableHead" begin="1" varStatus="status">
                <td style="text-align: center">
                    <button data-toggle="modal" data-target="#myModal" data-title="${tableHead}"
                            id="showChartId${status.index}" onclick="showModal(${status.index})">
                        显示图表
                    </button>
                </td>
            </c:forEach>
        </tr>
        <c:forEach items="${tableContentList}" var="tableContent">
            <tr>
                <c:forEach items="${tableContent}" var="content">
                    <td style="text-align: center">${content}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
    <ul class="pagination" id="paginationId">
        <li><a href="#" onclick="prevPage()">上一页</a></li>
        <li><a href="#" onclick="nextPage()">下一页</a></li>
    </ul>
    </c:if>
    <c:if test="${shouldShowTable <= 0}">
        <span style="text-align: center">暂时没有数据</span>
    </c:if>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
            </div>
            <div class="modal-body">
                <div id="main" style="width: 1000px;height:400px;"></div>
                <script type="text/javascript">
                    function showModal(i){
                        var index = i/1 - 1
                        var id = '#showChartId'+i;
                        var title = $(id).data('title');
                        var xAxis = eval($('#chartXAxisData').text());
                        var chart = echarts.getInstanceByDom(document.getElementById('main'));
                        var datatoShow = eval($('#chartDataList').text())[index];
                        var option = {
                            title: {
                                text: title
                            },
                            tooltip: {
                                trigger: 'axis',
                                formatter: '{b}:{c}'
                            },
                            legend: {
                                data:[title]
                            },
                            xAxis: {
                                data: xAxis,
                                splitLine: {
                                    show: false
                                },
                                boundaryGap: false
                            },
                            yAxis: {},
                            series: [{
                                name: '销量',
                                type: 'line',
                                data: datatoShow,
                                showAllSymbol: true
                            }]
                        };
                        chart.setOption(option)
                    }
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));
                </script>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</div>
<script>
    function firstGrade(){
        var begin = Date.parse($('#dateRangeBegin').val().replace(/-/g,"/"))
        var end = Date.parse($('#dateRangeEnd').val().replace(/-/g,"/"))
        if (begin>end){
            alert("开始时间必须晚于结束时间")
            return
        }
        var currentIndex = $('#currentIndex').text();
        var query = $('#queryForm').serialize()+"&page="+(currentIndex/1)
        window.location.href = "/statistics.do?"+query
    }
    function prevPage(){
        var currentIndex = $('#currentIndex').text();
        if (currentIndex == 0){
            alert("这是第一页了")
        }else{
            var query = $('#queryForm').serialize()+"&page="+(currentIndex/1 - 1)
            window.location.href = "/statistics.do?"+query
        }
    }
    function nextPage(){
        var maxPage = $('#maxPage').text()
        var currentIndex = $('#currentIndex').text()
        if ((maxPage/1) <= (currentIndex/1 + 1)) {
            alert("这是最后一页了")
        }else {
            var query = $('#queryForm').serialize()+"&page="+(currentIndex/1 + 1)
            window.location.href = "/statistics.do?"+query
        }
    }
    $(function() {
        var radioStatus = $('#optionRadiosBack').text()
        if (radioStatus != "perday"){
            $('#optionRadioshour').attr("checked","checked")
            $('#optionRadiosday').removeAttr("checked")
        }else{
            $('#optionRadiosday').attr("checked","checked")
            $('#optionRadioshour').removeAttr("checked")
        }
        var startDate =$('#dateRangeBeginBack').text()
        if (!startDate){
            startDate = new Date()
            startDate.setDate(startDate.getDate() - 1)
        }
        var endDate = $('#dateRangeEndBack').text()
        if (!endDate){
            endDate = new Date()
        }
        var dateRangeBegin = $('input[name="dateRangeBegin"]')
        dateRangeBegin.daterangepicker({
            startDate: startDate,
            singleDatePicker: true,
            "maxDate": endDate,
            showDropdowns: true,
            locale:{
                format: 'YYYY-MM-DD'
            }
        });
        $('input[name="dateRangeEnd"]').daterangepicker({
            startDate: endDate,
            singleDatePicker: true,
            "maxDate": new Date(),
            showDropdowns: true,
            locale:{
                format: 'YYYY-MM-DD'
            }
        });
    });
</script>
</body>
</html>

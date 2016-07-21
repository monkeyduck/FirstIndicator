<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset = "utf-8"/>
    <title>Chat by Web Sockets</title>
    <script src='<c:url value="/resources/recordWorker.js"></c:url>'></script>
    <script src='<c:url value="/resources/record.js"></c:url>'></script>
    <script src='<c:url value="/resources/jquery-3.1.0.min.js"></c:url>'></script>

    <style type='text/css'>

    </style>
</head>
<body>
<div id="message"></div>
<form action="http://api-web.emokit.com:802/wechatemo/WxVoiceWAV.do" method="post" enctype="multipart/form-data">
    <input type="hidden" name="appid" value="100001"/>

    <input type="hidden" name="key" value="98cd22f6f90141f8f1876dd2f5a27ea5"/>

    <input type="hidden" name="platid" value="h5_tag"/>

    <input type="hidden" name="uid" value="dupf"/>

    <input type="file" name="file" />

    <input type="submit" name="submit">

</form>

</body>

<script type='text/javascript'>
    var onFail = function(e) {
        console.log('Rejected!', e);
    };
    var rec;
    var onSuccess = function(s) {

        audioContext= window.AudioContext || window.webkitAudioContext;
        var context = new audioContext();
        var mediaStreamSource = context.createMediaStreamSource(s);
        rec = new Recorder(mediaStreamSource);
        //rec.record();

        // audio loopback
        // mediaStreamSource.connect(context.destination);
    }
    /*
     var onSuccess = function (stream) {
     //创建一个音频环境对像
     audioContext = window.AudioContext || window.webkitAudioContext;
     context = new audioContext();
     //将声音输入这个对像
     //rec = new Recorder(mediaStreamSource);


     var mediaStreamSource= context.createMediaStreamSources(stream);
     //设置音量节点

     volume = context.createGain();
     mediaStreamSource.connect(volume);
     rec = new Recorder(mediaStreamSource);
     //创建缓存，用来缓存声音
     var bufferSize = 2048;

     // 创建声音的缓存节点，createJavaScriptNode方法的
     // 第二个和第三个参数指的是输入和输出都是双声道。
     recorder = context.createJavaScriptNode(bufferSize, 2, 2);
     // 录音过程的回调函数，基本上是将左右两声道的声音
     // 分别放入缓存。
     recorder.onaudioprocess = function(e){
     console.log('recording');
     var left = e.inputBuffer.getChannelData(0);
     var right = e.inputBuffer.getChannelData(1);
     // we clone the samples
     leftchannel.push(new Float32Array(left));
     rightchannel.push(new Float32Array(right));
     recordingLength += bufferSize;
     }
     // 将音量节点连上缓存节点，换言之，音量节点是输入
     // 和输出的中间环节。
     volume.connect(recorder);
     // 将缓存节点连上输出的目的地，可以是扩音器，也可以
     // 是音频文件。
     recorder.connect(context.destination);

     }
     */

    //window.URL = URL || window.URL || window.webkitURL;
    navigator.getUserMedia  = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;


    //var audio = document.querySelector('#audio');
    var audio=$("#audiox");
    function startRecording() {
        if (navigator.getUserMedia) {
            navigator.getUserMedia({audio: true}, onSuccess, onFail);
        } else {
            console.log('navigator.getUserMedia not present');
        }
    }
    startRecording();
    //--------------------
    $('#record').click(function() {
        rec.record();
        //var dd = ws.send("start");
        $("#message").text("Click export to stop recording");
        // export a wav every second, so we can send it using websockets
        /* intervalKey = setInterval(function() {
         rec.exportWAV(function(blob) {
         rec.clear();
         //ws.send(blob);
         audio.src = URL.createObjectURL(blob);

         //alert(audio.src);
         });
         }, 3000);*/

    });

    $('#export').click(function() {
        // first send the stop command
        rec.stop();
        /*
         var fd = new FormData();
         fd.append('audioData', this.getBlob());
         */
        rec.exportWAV(function(blob) {
            rec.clear();
            //ws.send(blob);
            audio.src = URL.createObjectURL(blob);
            var fd = new FormData();


            fd.append('audioData', blob);
            var appid='100001';
            var key='98cd22f6f90141f8f1876dd2f5a27ea5';
            var platid='h5_tag';
            var uid='dupf';

            var resemo=(function loadEmo(){

                if (xhr.readyState == 4) {

                    var  jsonres = eval("(" + xhr.responseText + ")");
                    alert( JSON.stringify( jsonres.infovoice));
                    if(jsonres.status==0)
                    {

                        alert("result is---"+jsonres);
                    }
                }
            });


            var paraminit="?appid="+appid+"&key="+key+"&platid="+platid+"&uid="+uid;
            var xhr = new XMLHttpRequest();
            var url="https://api-web.emokit.com:1803/wechatemo/Voiceemo.do"+paraminit;
            var async=true;
            xhr.open('POST', url,async);
            xhr.onreadystatechange=resemo;
            res=xhr.send(fd);
        });
        /*
         clearInterval(intervalKey);

         ws.send("analyze");
         $("#message").text("");*/
    });
    //var ws = new WebSocket("ws://127.0.0.1:8088/websocket/servlet/record");
    /*var ws = new WebSocket("ws://localhost:8080/websocket");
     ws.onopen = function () {
     console.log("Openened connection to websocket");
     };
     ws.onclose = function (){
     console.log("Close connection to websocket");
     }
     ws.onmessage = function(e) {
     audio.src = URL.createObjectURL(e.data);
     }*/


</script>
</html>

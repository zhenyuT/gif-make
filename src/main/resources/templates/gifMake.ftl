<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>动图制作</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
    <#--<link href="http://www.bootcss.com/p/layoutit/css/bootstrap-combined.min.css" media="all" rel="stylesheet" type="text/css" />-->
    <#--<link href="http://www.bootcss.com/p/layoutit/css/layoutit.css" media="all" rel="stylesheet" type="text/css" />-->
    <script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/fileinput.min.js"></script>

</head>
<body>

<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <h3 class="text-center">
                动图制作
            </h3>
        </div>
    </div>

    <form id="form" method="post" enctype="multipart/form-data">
        <div class="row clearfix">
            <div class="col-md-6 column">
                    <div class="form-group">
                        <label for="gifName">名称</label>
                        <input id="gifId" name="gifId" value="0" class="hide" />
                        <input class="form-control" id="gifName"  name="gifName" type="text" placeholder="名称必须唯一,不要包含特殊字符" />
                    </div>
                    <div class="form-group">
                        <label for="video">视频上传</label>
                        <div dir=rtl class="file-loading">
                            <input id="video" name="video" type="file" accept = "video/*" capture="camcorder" />
                        </div>
                    </div>

            </div>
            <div class="col-md-6 column">
                <div class="form-group" style="visibility: hidden;">
                    <label for="sentenceNums">对话数</label><input class="form-control" id="sentenceNums"  name="sentenceNums" type="text" readonly />
                </div>
                <div class="form-group">
                    <label for="previewImg">预览图上传</label>
                    <div dir=rtl class="file-loading"> <!-- note the direction if you want to display file-loading indicator -->
                        <!-- note that your input must just set the `rtl` data property of the plugin or in your javascript code -->
                        <input id="previewImg" name="previewImg" type="file" accept="image/*" />
                    </div>
                </div>
            </div>
        </div>

        <div class="row clearfix">
            <div class="col-md-12 column">
                <h3>
                    对话配置
                </h3>
                <div style="color: gray;">(对话开始-结束时间必须配置,格式为"秒.毫秒"，默认对话可不配)</div>
                <table class="table">
                    <thead>
                    <tr>
                        <th> 对话</th>
                        <th> 开始时间</th>
                        <th> 结束时间 </th>
                        <th> 对话内容</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="line-height: 35px;">
                            第<span class="sentenceIndex">1</span>句
                        </td>
                        <td>
                            <input class="form-control" name="startTime" type="text" value="00.00" style="width: 200px;float: left;margin-right: 10px;"/>
                        </td>
                        <td>
                            <input class="form-control" name="endTime" type="text" value="01.00" style="width: 200px;float: left;" />
                        </td>
                        <td>
                            <input class="form-control" name="dialog" type="text" placeholder="填写默认对话"  style="width: 250px;float: left" />
                            <button onclick="delSentence(this);event.stopPropagation();"  class="btn glyphicon glyphicon-minus" style="margin-left: 10px;">删除</button>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </form>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <button onclick="addSentence()"  class="btn glyphicon glyphicon-plus">添加对话</button>
        </div>
    </div>

    <br>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <button id="submitButton" class="btn-primary btn-lg center-block">提交</button>
        </div>
    </div>
</div>

<script>
    $(document).on("ready", function() {
        $("#video").fileinput({
            rtl: true,
            dropZoneEnabled: false,
            allowedFileTypes: ["video"],
            allowedFileExtensions: ["mp4", "avi"],
            maxFileSize: 30*1024,
            maxFileCount: 1,
            showUpload: false,
            previewFileType: 'any'
        });

        $("#previewImg").fileinput({
            rtl: true,
            dropZoneEnabled: false,
            allowedFileTypes: ["image"],
            allowedFileExtensions: ["jpg", "png"],
            maxFileSize: 10*1024,
            maxFileCount: 1,
            showUpload: false,
            previewFileType: 'any'
        });

        $('#submitButton').on('click', submitForm);
    });

    function addSentence() {
        var sentenceIndex = $('table tbody tr:last-child').index()+1+1;
        var lastEndTime = $('table tbody tr:last-child input[name="endTime"]').val();
        if(!lastEndTime) lastEndTime = "00.00";
        var html = '<tr>'+
                   '     <td style="line-height: 35px;">'+
                   '     第<span class="sentenceIndex">'+sentenceIndex+'</span>句'+
                   '     </td>'+
                   '     <td>'+
                   '     <input class="form-control" name="startTime" type="text" value="'+lastEndTime+'" style="width: 200px;float: left;margin-right: 10px;"/>'+
                   '     </td>'+
                   '     <td>'+
                   '     <input class="form-control" name="endTime" type="text" value="'+lastEndTime+'" style="width: 200px;float: left;" />'+
                   '     </td>'+
                   '     <td>'+
                   '     <input class="form-control" name="dialog" type="text" placeholder="填写默认对话"  style="width: 250px;float: left" />'+
                   '     <button onclick="delSentence(this);event.stopPropagation();"  class="btn glyphicon glyphicon-minus" style="margin-left: 10px;">删除</button>'+
                   '     </td>'+
                   '</tr>';
       $('table tbody').append(html);
    }

    function delSentence(obj) {
        $(obj).parent().parent().remove();
        $('table tbody tr td span.sentenceIndex').each(function (i, e) {
            $(this).html(i+1);
        });
    }

    /**
     * 将填写的时间格式化为字幕文件需要的格式
     * @param time
     */
    function formatTime(time) {
        return "0:00:"+$.trim(time);
    }

    function submitForm() {
        var formData = new FormData(document.getElementById('form'));
        var defaultSentences = [];
        var sentenceInfos = [];
        $("input[name='dialog']").each(function (i, e) {
            defaultSentences.push(e.value);


            var startTime = $(this).parent().parent().find("input[name='startTime']").val();
            var endTime = $(this).parent().parent().find("input[name='endTime']").val();
            var sentenceInfo = {
                startTime: formatTime(startTime),
                endTime: formatTime(endTime),
                placeholder: "$\{mx.sentences"+i+"}"
            };
            sentenceInfos.push(sentenceInfo);

        });
        console.log(sentenceInfos)
        formData.append("defaultSentences", JSON.stringify(defaultSentences));
        formData.append("sentenceInfos", JSON.stringify(sentenceInfos));
        formData.delete("dialog");
        formData.delete("startTime");
        formData.delete("endTime");
        $.ajax({
            url: "${request.contextPath}/gif/makeGif",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (data) {
                try {
                    data = JSON.parse(data);
                    if (data.status == 200) {
                        var gifId = data.gifId;
                        $('#gifId').val(gifId);
                        $('#gifName').attr('readonly', true);
                        alert("制作成功");
                    } else {
                        alert("制作失败："+data.errorMsg);
                    }
                } catch (e) {
                    console.error("", e);
                    alert("制作异常")
                }
            },
            error: function (data) {
                alert('网络异常');
            }
        });
    }
</script>



</body>

</html>

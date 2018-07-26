<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>为所欲为</title>

    <meta name="author" content="lizhihao!">

  	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-md-12">
			<h1>
				动图制作Java版
                <a href="gif_make" target="_blank"  type="button" class="btn btn-primary btn-lg center-block">
                    自制动图
                </a>
			</h1>

		</div>
	</div>
		<p></p>
		<div class="col-md-12">
			<div class="tabbable" id="tabs1">
				<ul class="nav nav-pills">
					<#list gifList as gif>
                        <li class="<#if gif_index==0>active</#if> ">
                            <a onclick="clickTab(${gif.gifConfig.gifId},'${gif.gifConfig.gifName}')" href="#panel-${gif_index}" data-toggle="tab" id="${gif.gifConfig.gifName}">${gif.gifConfig.gifName}</a>
                        </li>
					</#list>
				</ul>
				<br>
				<div class="tab-content">
					<#list gifList as gif>
                        <div class="tab-pane <#if gif_index==0>active</#if> " id="panel-${gif_index}">
                            <div class="row">
                                <div class="col-md-6">
                                    <img class="center-block img-responsive" alt="${gif.gifConfig.gifName}" src="${gif.gifConfig.previewImg}">
                                </div>
                                <div class="col-md-6">
                                    <form class="form-horizontal" role="form">
									<#list gif.defaultSentences as sentence>
                                        <div class="form-group">
                                            <label  class="col-sm-2 control-label">
                                                第${sentence_index+1}句:
                                            </label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" name="${gif.gifConfig.gifName}_sen" placeholder="${sentence}">
                                            </div>
                                        </div>
									</#list>

                                    </form>
                                </div>
                            </div>
                        </div>
					</#list>

				</div>
			</div>
		</div>


	</div>
	<div class="row">
		<br><br>
		<div class="col-md-12 text-center">
			<label class="radio-inline">
				<input type="radio" name="inlineRadioOptions" id="inlineRadio1" value="simple" alt="小文件，质量低"> 小图(表情包)
			</label>
			<label class="radio-inline">
				<input type="radio" name="inlineRadioOptions" id="inlineRadio2" value="normal" checked alt="大文件，质量高"> 大图（论坛微博）
			</label>
		</div>
		<br><br>
	</div>
	<div class="row">
		<div class="col-md-12">
			<input type="hidden" id="gifId" value="${gifList[0].gifConfig.gifId}">
			<input type="hidden" id="template" value="${gifList[0].gifConfig.gifName}">
			<button id="render" type="button" class="btn btn-primary btn-lg center-block" data-loading-text="生成中..." >
				<!--data-target="#myModal"-->
				生成
			</button>
			<#if couldDel==true>
			    <button id="delBtn" type="button" class="btn btn-primary btn-lg center-block" style="margin-top: 10px;"  >
                    删除
                </button>
			</#if>

		</div>
	</div>
	<div class="row">
		<br><br>
		<div class="col-md-12">
			<p>
				<img id="result" class="center-block img-responsive" src="">
				<a id="download" href="#" target="_blank">
					<button id="downloadbtn" type="button" class="btn btn-default center-block hidden" aria-label="Left Align">
						猛击打开<span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
					</button>
				</a>
			</p>
		</div>
	</div>
</div>
</div>
<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!--<script src="js/jquery.min.js"></script>-->
<!--<script src="js/bootstrap.min.js"></script>-->
<!--<script src="js/scripts.js"></script>-->
<!--<script src="//cdn1.lncld.net/static/js/3.0.4/av-min.js"></script>-->
<!--<script src='//unpkg.com/valine/dist/Valine.min.js'></script>-->
</body>
<script>

	function clickTab(gifId, gifName) {
        $('#gifId').val(gifId);
        $('#template').val(gifName);
    }
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
//		e.target // newly activated tab
//		e.relatedTarget // previous active tab
		$('#template').val(e.target.id)
	});

	$('#render').bind('click', function () {
		var $btn = $(this).button('loading')
		$('#render').prop('disabled', true);
		var template = $('#template').val();
        var gifId = $('#gifId').val();
		var inputs = $("input[name='" + template + "_sen']")
		var all = [];
		inputs.each(function (i, e) {
			e.value = e.value === '' ? e.placeholder : e.value
			all.push(e.value)
		});

		var json = {
		    "gifId": gifId,
			"templateName": template,
			"sentence": all.join(),
			"mode":$("input:checked").val()
		};

		console.log(json);

		$.ajax({
			type: "POST",
			url: "/gif/filePath",
			data: JSON.stringify(json),
			contentType: "application/json",
			dataType: 'json'
		}).done(function (msg) {
			if (msg.code=='0'){
                alert("生成成功！快去看看吧！");
				$('#download').attr('href', msg.result);
			} else {
				alert("服务器爆掉了，请稍后再试！");
//				$('#result').attr('src', 'http://p5m79jjxo.bkt.clouddn.com/money.jpg');
			}
		}).error(function (jqxhr, textStatus, errorThrown) {
			alert("服务器爆掉了，请稍后再试！");
//			$('#result').attr('src', 'http://p5m79jjxo.bkt.clouddn.com/money.jpg');
		}).complete(function () {
			$btn.button('reset')
			$('#render').prop('disabled', false);
			$('#downloadbtn').removeClass('hidden')
		});
	});

	$('#delBtn').on('click', function () {
        var gifId = $('#gifId').val();
		$.ajax({
            type: "POST",
            url: "/gif/delGif",
            data: {
                gifId: gifId
			},
			success: function (data) {
				if(data=='ok'){
				    alert("删除成功");
				    location.reload(true);
				} else {
                    alert("删除失败");
				}
            },
			error: function () {
                alert("服务器爆掉了，请稍后再试！");
            }
		});
    });
</script>
</html>

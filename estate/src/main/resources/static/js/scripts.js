
function editReply(rid, reg_id, content) {
		var para = document.location.href.split("/"); 
		var questionId = para[4]
		var htmls = "";
		htmls += '<div class="media text-muted pt-3" id="rid ' + rid + '">';
		htmls += '<span class="d-block">';
		htmls += '<strong class="text-gray-dark" style="font-size:18px;"> ID &nbsp' + reg_id + '</strong>';
		htmls += '<span style="padding-left: 7px; font-size: 9pt">';
		htmls += '</span>';
		htmls += '</span>';	
		htmls += '<form class="reply-write" method="post" action="/api/questions/' + questionId + '/answers/edit/' + rid + ' "> ';
		htmls += 	'<div class="form-group" style="padding:14px; margin-bottom:0px; ">';
     	htmls += 		'<textarea name="contents" id="editContent" class="form-control" rows="3" style="margin-right:10px">';
		htmls += 			content;
		htmls += 		'</textarea>';
		htmls += 	'</div>';
		//htmls += 	'<input type="button" class="btn btn-primary pull-right" value="취소하기" style="margin-left:10px; margin-top:6px;" />' ;
		htmls += 	'<input type="submit" class="form-control btn btn-primary pull-right" value="수정하기" style="margin-top:6px; width:85px; float:right;"/>' ;
		htmls +=   '<div class="clearfix" />';
		htmls += '</form>';
		htmls += '</div>';
		var RID = document.getElementById('rid ' + rid);
		RID.insertAdjacentHTML('afterend', htmls);
		$('#editContent').focus();
}

$(".reply-write input[type=submit]").click(editReplySave);

function editReplySave(e) {
	e.preventDefault();
	console.log("editReplySave clicked");
	
	var queryStringReply = $(".reply-write").serialize();
	console.log("query : " + queryStringReply);
	
	var urlReply = $(".reply-write").attr("action");
	console.log("url : " + urlReply);
	
//	$.ajax({
//		type : 'post',
//		url : urlReply,
//		data : queryStringReply,
//		dataType : 'json' ,
//		error : onReplyError , 
//		success : onReplySuccess
//	});
}

function onReplySuccess(data, status) {
	console.log(data, status);
	window.location.reload();
}

//$(".reply-write input[type=button]").click(cancleEdit);

$(".answer-write input[type=submit]").click(addAnswer);
		
function addAnswer(e) {
	e.preventDefault();
	console.log("click me!");
	
	var queryString = $(".answer-write").serialize();
	console.log("query : " + queryString);
	
	var url = $(".answer-write").attr("action");
	console.log("url : " + url);
	
	$.ajax({
		type : 'post',
		url : url,
		data : queryString,
		dataType : 'json' ,
		error : onError , 
		success : onSuccess
	});
	
} 

function onError() {
}

function onSuccess(data, status) {
	console.log(data);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template);
	$("textarea[name=contents]").val("");
	window.location.reload();
}

$(".link-delete-article").click(deleteAnswer);

function deleteAnswer(e) {
	e.preventDefault();
	
	var deleteBtn = $(this);
	var url = deleteBtn.attr("href");
	console.log("url : " + url);
	
	$.ajax({
		type : 'delete',
		url  : url,
		dataType : 'json',
		error : function(request, status) {
			console.log("error");
		},
		success : function(data, status) {
			console.log(data);
			if(data.valid) {
				deleteBtn.closest("article").remove();
				window.location.reload();
			} else {
				alert(data.errorMessage);
			}
		}
	});
}

String.prototype.format = function() {
	  var args = arguments;
	  return this.replace(/{(\d+)}/g, function(match, number) {
	    return typeof args[number] != 'undefined'
	        ? args[number]
	        : match
	        ;
	  });
	};


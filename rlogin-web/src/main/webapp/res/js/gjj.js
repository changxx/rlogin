var gjj = {
	login : function() {
		var account = $('#account').val();
		var pass = $('#password').val();
		var vericode = $('#vericode').val();

		$.ajax({
			type : "post",
			url : context + "/gjj/loginin",
			data : {
				"account" : account,
				"pass" : pass,
				"vericode" : vericode
			},
			beforeSend : function() {
				$('#login').html('登录中，请稍候');
			},
			success : function(json) {
				if (json.code == 0) {
					$('#login').html('登录成功，数据加载中');
					gjj.fetch(account, pass);
				} else {
					alert(json.tip);
				}
			}
		})
	},

	fetch : function(account, pass) {
		$.ajax({
			type : "post",
			url : context + "/gjj/fetch",
			data : {
				"account" : account,
				"pass" : pass
			},
			success : function(json) {
				if (json.code == 0) {
					location.href = context + "/gjj/index"
					console.log(json.tip);
				} else {
					alert(json.tip);
				}
			}
		})
	}

}
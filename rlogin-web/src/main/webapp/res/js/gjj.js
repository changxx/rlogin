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
			success : function(json) {
				if (json.code == 0) {
					console.log(json.tip);
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
					console.log(json.tip);
				} else {
					alert(json.tip);
				}
			}
		})
	}

}
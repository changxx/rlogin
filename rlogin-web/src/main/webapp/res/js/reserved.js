var reserved = {
	login : function() {
		var account = $('#account').val();
		var pass = $('#password').val();
		var vericode = $('#vericode').val();

		$.ajax({
			type : "post",
			url : context + "/reserved/loginin",
			data : {
				"account" : account,
				"pass" : pass,
				"vericode" : vericode
			},
			success : function(msg) {
				alert("Data Saved: " + msg.tip);
			}

		})
	}
}
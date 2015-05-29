var ipcrs = {
    login: function () {
        var account = $('#account').val();
        var pass = $('#password').val();
        var vericode = $('#vericode').val();

        $.ajax({
            type: "post",
            url: context + "/ipcrs/loginin",
            data: {
                "account": account,
                "pass": pass,
                "vericode": vericode
            },
            beforeSend: function () {
                $('#login').html('登录中，请稍候');
            },
            success: function (json) {
                if (json.code == 0) {
                    $('#login').html('登录成功，数据加载中');
                } else {
                    alert(json.tip);
                }
            }
        })
    }
}
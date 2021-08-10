layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    
    //提交表单!!
    form.on("submit(login)",function (data){
        var dataField=data.field;
        //alert(data.field.username+"-->"+data.field.password);
        //验证用户名
        if (dataField.username == "undefinded" || dataField.username.trim == "") {
            layer.msg("用户名不能为空");
            return;
        }
        if (dataField.password == "undefinded" || dataField.password.trim == "") {
            layer.msg("密码不能为空");
            return;
        }
        //发送ajax请求
        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            data: {
                "username": dataField.username,
                "userpwd": dataField.password
            },
            dataType:"json",
            success: function (data) {
                if (data.code == 200) {
                    layer.msg("登录成功了", function () {
                        //存储数据到cookie
                        $.cookie("userIdStr", data.result.userIdStr);
                        $.cookie("userName", data.result.userName);
                        $.cookie("trueName", data.result.trueName);
                        //判断 记住我
                        if($("#rememberMe").is(":checked")){
                            //存储数据到cookie
                            $.cookie("userIdStr", data.result.userIdStr,{expires:7});
                            $.cookie("userName", data.result.userName,{expires: 7});
                            $.cookie("trueName", data.result.trueName,{expires:7 });
                        }
                        //跳转页面
                        window.location.href = ctx + "/main";
                    });
                } else {
                    //登录失败展示错误信息
                    layer.msg(data.msg);
                }
            }
        });

        //阻止表单跳转
        return false;
    });




});


layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //提交表单!!
    form.on("submit(saveBtn)",function (data){
        var dataField=data.field;
        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                "oldPassword":dataField.old_password,
                "newPassword":dataField.new_password,
                "confirmPwd":dataField.again_password
            },
            dataType:"json",
            success:function (data){
                if(data.code==200){
                    layer.msg("修改密码成功了，三秒后消失",function(){
                        //清空Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
        });
        //阻止表单跳转
        return false;
    });
});
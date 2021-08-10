layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects =layui.formSelects;
    /**
     * 添加或更新⽤户
     * 添加模块
     */
    form.on("submit(addOrUpdateUser)",function(obj){
        //加载层 特效
        var index=top.layer.msg("数据加载中...",{icon:16,time:false,shade:0.8});
        //发送地址
        var url=ctx+"/user/save";
        //判断是新增还是修改
        //有id修改 不存在添加
        if($("input[name='id']").val()){
            url=ctx+"/user/update"
        }
        //添加 发送ajax
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (data){    //回调函数
                if(data.code==200){
                    //关闭中间层 //根据索引关
                    top.layer.close(index);
                    //关闭iframe //关闭所有弹窗
                    top.layer.closeAll("iframe");
                    top.layer.msg("添加OK",{icon:6});
                    //刷新父路径 //刷新当前页面
                    parent.location.reload()
                }else{
                    //失败提示信息
                    layer.msg(data.msg,{icon:5});
                }
            }
        });
        //阻止表单提交
        return false;
    });


    /*取消*/
    $("#closeBtn").click(function (){
       var index = parent.layer.getFrameIndex(window.name);
       parent.layer.close(index);
    });


    /*加载下拉框*/
    var userId=$("input[name='id']").val();
    formSelects.config('selectId', {
        type: 'post',                //请求方式: post, get, put, delete...
        searchUrl: ctx+'/role/roles?userId='+userId,              //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
        keyName: 'roleName',            //自定义返回数据中name的key, 默认 name
        keyVal: 'id',            //自定义返回数据中value的key, 默认 value
        //当有搜索内容时, 点击选项是否清空搜索内容, 默认不清空
    }, true);
});
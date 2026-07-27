layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    $.ajax({
        type:"post",
        url:ctx+"/goodsType/queryAllGoodsTypes",
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view:{
                    showLine: false
                }/*,
                callback: {
                    onClick: zTreeOnClick
                }*/
            };
            $.fn.zTree.init($("#treeDemo"), setting, data);
        }
    })


    /**
     * 商品单位下拉框展示
     */
    $.ajax({
        type:"post",
        url:ctx+"/goodsUnit/allGoodsUnits",
        success:function (data){
            if (data!== null) {
            	var selectedUnit = $("input[name='goodsUnit']").val(); // 单位名称
            	 $.each(data, function(index, item) {
                     var selected = (selectedUnit == item.name) ? "selected='selected'" : "";
                     $("#unit").append("<option value='"+item.name+"' "+selected+">"+item.name+"</option>");
                 });
            }
            //重新渲染
            form.render("select")
        }
    })




    form.on("submit(addOrUpdateGoods)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/goods/save";
        if($("input[name='id']").val()){
            url=ctx + "/goods/update";
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                        res.message, {
                            icon: 5
                        }
                    );
            }
        });
        return false;
    });
    
    // 保存并新增下一商品
    form.on("submit(next)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        // 始终是新增，用 /goods/save
        $.post(ctx + "/goods/save", data.field, function (res) {
            if (res.code == 200) {
                top.layer.close(index);
                top.layer.msg("操作成功，继续新增！");
                // 刷新父页面列表
                parent.location.reload();
                // 获取当前选中的分类ID，以便刷新后继续使用
                var typeId = $("input[name='typeId']").val();
                // 刷新当前 iframe，重置表单，继续新增
                var url = ctx + "/goods/addOrUpdateGoodsPage";
                if (typeId && typeId != 0) {
                    url += "?typeId=" + typeId;
                }
                window.location.href = url;
            } else {
                layer.msg(res.message, {icon: 5});
            }
        });
        return false;
    });


    $("#reloadGoodsType").click(function (){
        var url  =  ctx+"/goods/toGoodsTypePage?typeId="+$("input[name='typeId']").val();
        var title="商品管理-商品类别";
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","400px"],
            maxmin:true,
            content : url
        });
    })



    $("#closeDlg").click(function (){
        // iframe 页面关闭 添加parent
        parent.layer.closeAll();
    })


});

/**
 * 子窗口调用方法  显示选中的商品类别
 * @param typeName
 * @param typeId
 */
function getVal(typeName,typeId){
    $("input[name='typeName']").val(typeName);
    $("input[name='typeId']").val(typeId);
}
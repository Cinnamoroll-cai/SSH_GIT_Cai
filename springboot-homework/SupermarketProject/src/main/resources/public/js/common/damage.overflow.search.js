layui.use(['laydate', 'table', 'layer', 'form'], function() {
    var $ = layui.jquery;
    var laydate = layui.laydate;
    var table = layui.table;
    var layer = layui.layer;

    laydate.render({ elem: '#startDate' });
    laydate.render({ elem: '#endDate' });

    // 全局表格实例
    var leftTableInst;

    // 初始化左侧表格
    function initDamageTable(){
        leftTableInst = table.render({
            elem: '#leftList',
            id: 'leftListTable',
            url: ctx + "/damage/list",
            method: 'post',
            page: { limit: 10, limits: [10,15,20,25] },
            where: { // 默认查询条件
                startDate:'',
                endDate:''
            },
            // 适配后端返回格式
            parseData: function(res){
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.count,
                    "data": res.data
                }
            },
            cols: [[
                { field: 'damageNumber', title: '单号', minWidth: 50, align: 'center' },
                { field: 'damageDate', title: '日期', minWidth: 50, align: 'center' },
                { field: 'type', title: '类型', minWidth: 80, align: 'center', templet:function(){return "报损单";}},
                { field: 'userName', title: '操作员', minWidth: 80, align: 'center' },
                { field: 'remarks', title: '备注', minWidth: 100, align: 'center' },
                { templet: '#leftListBar', minWidth: 120, fixed: 'right', align: 'center' }
            ]]
        });
    }

    function initOverflowTable(){
        leftTableInst = table.render({
            elem: '#leftList',
            id: 'leftListTable',
            url: ctx + "/overflow/list",
            method: 'post',
            page: { limit: 10, limits: [10,15,20,25] },
            where: {
                startDate:'',
                endDate:''
            },
            parseData: function(res){
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.count,
                    "data": res.data
                }
            },
            cols: [[
                { field: 'overflowNumber', title: '单号', minWidth: 50, align: 'center' },
                { field: 'overflowDate', title: '日期', minWidth: 50, align: 'center' },
                { field: 'type', title: '类型', minWidth: 80, align: 'center', templet:function(){return "报溢单";}},
                { field: 'userName', title: '操作员', minWidth: 80, align: 'center' },
                { field: 'remarks', title: '备注', minWidth: 100, align: 'center' },
                { templet: '#leftListBar', minWidth: 120, fixed: 'right', align: 'center' }
            ]]
        });
    }

    // 默认加载报损单
    initDamageTable();

    // 搜索按钮点击
    $(".search_btn").on("click", function() {
        var type = $("#type").val();
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        if(type == "1"){
            initDamageTable();
            table.reload('leftListTable', {
                where: {startDate:startDate, endDate:endDate},
                page: {curr:1}
            })
        }else{
            initOverflowTable();
            table.reload('leftListTable', {
                where: {startDate:startDate, endDate:endDate},
                page: {curr:1}
            })
        }
    });

    // 右侧商品表格
    table.render({
        elem: '#rightList',
        id: 'rightListTable',
        page: { limit: 10, limits: [10,15,20,25] },
        cols: [[
            { field: 'code', title: '商品编码', align: 'center' },
            { field: 'name', title: '商品名称', align: 'center' },
            { field: 'model', title: '型号', align: 'center' },
            { field: 'price', title: '单价', align: 'center' },
            { field: 'num', title: '数量', align: 'center' },
            { field: 'unit', title: '单位', align: 'center' },
            { field: 'total', title: '总金额', align: 'center' }
        ]],
        data: []
    });

    // 行工具事件
    table.on('tool(leftList)', function(obj){
        var type = $("#type").val();
        if(obj.event === "search"){
            if(type == "1"){
                $("#number_").val(obj.data.damageNumber);
                $("#date_").val(obj.data.damageDate);
                $("#typeName_").val("报损单");
                $("#userName_").val(obj.data.userName);
                table.reload('rightListTable',{
                    url:ctx+'/damageListGoods/list',
                    where:{damageListId:obj.data.id},
                    page:{curr:1}
                })
            }else{
                $("#number_").val(obj.data.overflowNumber);
                $("#date_").val(obj.data.overflowDate);
                $("#typeName_").val("报溢单");
                $("#userName_").val(obj.data.userName);
                table.reload('rightListTable',{
                    url:ctx+'/overflowListGoods/list',
                    where:{overflowListId:obj.data.id},
                    page:{curr:1}
                })
            }
        }else if(obj.event === "del"){
            var url = type=="1" ? ctx+"/damage/delete" : ctx+"/overflow/delete";
            layer.confirm("确定删除？",function(){
                $.post(url,{id:obj.data.id},function(r){
                    if(r.code==200){
                        layer.msg("删除成功");
                        $(".search_btn").click();
                    }else{
                        layer.msg(r.message);
                    }
                },"json")
            })
        }
    })

    // 重置按钮
    $(".search_btn02").on("click",function(){
        $("#number_,#date_,#typeName_,#userName_").val("");
        table.reload("rightListTable",{data:[]})
    })
});
var renderTableData;

$(function() {
    layui.use(['laydate', 'table', 'layer', 'form'], function() {
        var layer = layui.layer,
            $ = layui.jquery,
            laydate = layui.laydate,
            table = layui.table,
            form = layui.form;

        laydate.render({ elem: '#startDate' });
        laydate.render({ elem: '#endDate' });

        var zTreeObj;
        $.ajax({
            type: "post",
            url: ctx + "/goodsType/queryAllGoodsTypes",
            dataType: "json",
            success: function(data) {
                var setting = {
                    data: { simpleData: { enable: true } },
                    view: { showLine: false },
                    callback: { onClick: zTreeOnClick }
                };
                zTreeObj = $.fn.zTree.init($("#goodsTypeTree"), setting, data);
            }
        });

        function zTreeOnClick(event, treeId, treeNode) {
            $("input[name='typeId']").val(treeNode.id);
            renderTableData();
        }

        var tableIns = table.render({
            elem: '#purchaseList',
            url: ctx + "/purchase/countPurchase",
            page: true,
            limits: [10, 15, 20, 25],
            limit: 20,
            id: "purchaseListTable",
            // 重点！！！强制把后台数据转为标准JS对象，解决Map解析异常
            parseData: function(res) {
                // 序列化再反序列化，净化数据
                var rawList = JSON.parse(JSON.stringify(res.data));
                return {
                    code: res.code,
                    msg: res.msg,
                    count: res.count,
                    data: rawList
                }
            },
            cols: [[
                { field: 'purchasenumber', title: '单号', align: 'center' },
                { field: 'type', title: '类型', align: 'center' },
                { field: 'purchasedate', title: '日期', align: 'center' },
                { field: 'suppliername', title: '供应商', align: 'center' },
                { field: 'goodscode', title: '商品编码', align: 'center' },
                { field: 'goodsname', title: '商品名称', align: 'center' },
                { field: 'goodsmodel', title: '商品型号', align: 'center' },
                { field: 'typename', title: '类别', align: 'center' },
                { field: 'goodsprice', title: '单价', align: 'center' },
                { field: 'goodsnum', title: '数量', align: 'center' },
                { field: 'unitname', title: '单位', align: 'center' },
                { field: 'totalprice', title: '总金额', align: 'center' }
            ]],
            done: function(res) {
                console.log("净化后数据：", res.data);
            }
        });

        $(".search_btn").on("click", function() {
            renderTableData();
        });

        renderTableData = function () {
            table.reload("purchaseListTable", {
                page: { curr: 1 },
                where: {
                    goodsName: $("input[name='goodsName']").val(),
                    startDate: $("input[name='startDate']").val(),
                    endDate: $("input[name='endDate']").val(),
                    typeId: $("input[name='typeId']").val()
                }
            });
        };
    });
});

/**
 * Created by Administrator on 2016/11/19.
 */
$(function(){
    //声明省
    var onemenu = ["wap", "pc", "back", "common"]; //直接声明Array
//声明市
    var twomenu = [
        ["login", "register", "common", "index","purse","center","taskrecord","invite","daysign"],
        ["index", "common","login","bussiness","paycash","publishArticle"],
        ["login", "withdraw"],
        ["pcwap","pcback"]
    ];
    var imgtype = [
        [
            ["png", "jpg"],
        ]
    ]
    //设置一个省的公共下标
    var pIndex = -1;
//先设置一级地址的值
    onemenu.forEach(function(){
        var option=document.createElement("option");
        option.setAttribute("value",++pIndex);
        option.innerHTML=onemenu[pIndex];
        document.getElementById("onemenu").appendChild(option);
    });
    $("#onemenu").change(function(){
        if ($("#onemenu option:selected").val()>-1){
            var val=$("#onemenu option:selected").val();
            console.log(val);
            var pIndex=-1;
            console.log("pIndex"+pIndex);
            var tm = twomenu[val];
            $("#twomenu").empty();
            tm.forEach(function(){
                var option=document.createElement("option");
                option.setAttribute("value",pIndex++);
                option.innerHTML=tm[pIndex];
                document.getElementById("twomenu").appendChild(option);
            });
        }
    })
})
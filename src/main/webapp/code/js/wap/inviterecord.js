/**
 * Created by Administrator on 2016/10/14 0014.
 */
var total=0;
var pageSize=16;
var count=1;
$(function() {
    loadinviterecord();

})
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();               //滚动条距离顶部的高度
    var scrollHeight = $(document).height();                   //当前页面的总高度
    var windowHeight = $(this).height();                   //当前可视的页面高度
    if(scrollTop + windowHeight >= scrollHeight){        //距离顶部+当前高度 >=文档总高度 即代表滑动到底部
        console.log(Math.ceil(total/pageSize)+1+"shi");
        if(count<=Math.ceil(total/pageSize)+1){
            $(".loadmore").css("visibility","visible");
            $(".loadmore").click(function(){
                count++;
                $(this).css("visibility","hidden");
                loadinviterecord();
            })
        }
        if(count>Math.ceil(total/pageSize)){
            $(".loadmore").css("display","none");
        }
    }else if(scrollTop<=0){         //滚动条距离顶部的高度小于等于0
//                    location.reload();
    }
});
function loadinviterecord(){
    $.ajax({
        type:'post',
        dataType:'json',
        url:'/pc/admin/memberInvited/getInvitedRecordList',
        data:{pageIndex:count,pageSize:pageSize,type:1},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params) {
            var json = eval(params);
            if (json.data != null && json.errorCode == 0) {
                total=json.data.total;
                json.data.list.forEach(function (invitelist) {
                    var li = document.createElement("li");
                    var img = document.createElement('img');
                    img.setAttribute("src",  invitelist.imageUrl);
                    var h5 = document.createElement('h5');
                    h5.innerHTML = invitelist.memberName;
                    var p = document.createElement('p');
                    var timestamp = invitelist.invitedTime;
                    p.innerHTML = format(timestamp);
                    li.appendChild(img);
                    li.appendChild(h5);
                    li.appendChild(p);
                    document.body.appendChild(li);
                    document.getElementsByTagName('ul')[0].appendChild(li);
                    $(".nullp").css("display","none");
                })
            } else {
                $(".nullp").css("display","block");

                //alert("获取数据失败")
            }
        },
        error:function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function format(timestamp) {
    function add0(y) {
        return y < 10 ? '0' + y : y
    }
    var time = new Date(timestamp * 1000);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return add0(y) + '/' +add0(m) + '/' + add0(d) + ' ' + add0(h) + ':' + add0(mm)+ ':' + add0(s);
}//时间戳变换格式

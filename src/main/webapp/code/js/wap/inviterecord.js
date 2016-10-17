/**
 * Created by Administrator on 2016/10/14 0014.
 */
$(function() {
    $.ajax({
        type:'post',
        dataType:'json',
        url:'/pc/admin/memberInvited/getInvitedRecordList',
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params) {
            var json = eval(params);
            if (json.data != null && json.errorCode == 0) {
                json.data.forEach(function (invitelist) {
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
})
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

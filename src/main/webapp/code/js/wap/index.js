/**
 * Created by Administrator on 2016/9/28.
 */
var getLocation = function () {
    //首先设置默认城市
    var defCity = {
        id: '000001',
        name: '北京市',
        date: new Date().getTime()//获取当前时间方法
    };
    //默认城市
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;
                //var map = new BMap.Map("container");   // 创建Map实例
                var point = new BMap.Point(lon, lat); // 创建点坐标
                var gc = new BMap.Geocoder();
                gc.getLocation(point, function (rs) {
                    var addComp = rs.addressComponents;
                    var curCity = {
                        id: '',
                        name: addComp.province,
                        date: new Date().getTime()
                    };
                    //当前定位城市
                    $.cookie('VPIAO_MOBILE_CURRENTCITY', JSON.stringify(curCity), { expires: 7, path: '/' });
                    //alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street);
                    $("#addressname").text(addComp.city);
                    modifyMemberAddress(addComp.province+","+addComp.city+","+addComp.district+","+addComp.street)
                });
            },
            function (error) {
                switch (error.code) {
                    case 1:
                        alert("位置服务被拒绝。");
                        jQuery.cookie('VPIAO_MOBILE_DEFAULTCITY', JSON.stringify(defCity), { expires: 1, path: '/' })
                        break;
                    case 2:
                        alert("暂时获取不到位置信息。");
                        jQuery.cookie('VPIAO_MOBILE_DEFAULTCITY', JSON.stringify(defCity), { expires: 1, path: '/' })
                        break;
                    case 3:
                        alert("获取位置信息超时。");
                        jQuery.cookie('VPIAO_MOBILE_DEFAULTCITY', JSON.stringify(defCity), { expires: 1, path: '/' })
                        break;
                    default:
                        alert("未知错误。");
                        jQuery.cookie('VPIAO_MOBILE_DEFAULTCITY', JSON.stringify(defCity), { expires: 1, path: '/' })
                        break;
                }
                var curCity = {
                    id: '000001',
                    name: '北京市',
                    date: new Date().getTime()
                };
                //默认城市
                $.cookie('VPIAO_MOBILE_DEFAULTCITY', JSON.stringify(curCity), { expires: 3, path: '/' });
            }, { timeout: 5000, enableHighAccuracy: true });
    } else {
    }
};


function  modifyMemberAddress(address){
    $.ajax({
        url:"/pc/admin/member/modifyMemberAddress",
        type:"post",
        data:{memberAddress:address},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (data) {

        }
    })
}
function updateMyscore(){
    $.ajax({
        url:"/pc/admin/member/updateMemberScore",
        type:"post",
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (data) {
            var json=eval(data)
            if(json.errorCode==0){
                var obj=json.data
                $(".money>p>span").text(obj.memberScore.toFixed(2))
            }
        }
    })
}
var count=0;
function getMemberRequestHeaderMsg(XMLHttpRequest){
    var memberId= $.cookie("memberId");
    if (memberId==null||memberId == undefined){
        if (count<1){
        alert("登录已过期请重新登录");
            count++;
            location.href="/frontPage/wap/login.html"
        }
    }
    var password= $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId",memberId);
    XMLHttpRequest.setRequestHeader("from",2);
}


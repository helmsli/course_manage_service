/**
 * 
 */

function getRootPath()
{
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    let curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    let pathName = window.document.location.pathname;
    let pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    let localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    let projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    //return (localhostPaht + projectName);
    return (localhostPaht );
}
function ajaxPost(serverUrl,postData,funSuccess,funError)
{
	$.ajax({
	    url:serverUrl,
	    type:'POST', //GET
	    async:true,    //或false,是否异步
	    data:JSON.stringify(postData),
	    contentType:'application/json;charset=UTF-8',
	    timeout:100000,    //超时时间
	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
	    beforeSend:function(xhr){
	        console.log(xhr)
	        
	        var transid = sessionStorage.getItem("transid");
			if(transid)
			{
				xhr.setRequestHeader('userTransid', transid);
			}
			let token = sessionStorage.getItem("token");
			if(token)
			{
				xhr.setRequestHeader('token', token);
			}
			console.log('发送前：'+  transid + ":" + token);
	        //xhr.setRequestHeader('Accept', 'text/plain; charset=utf-8');
	       // xhr.setRequestHeader('Content-Type', 'text/plain; charset=utf-8');
	    },
	    /*success:function(data,textStatus,jqXHR){
	        console.log(data)
	        console.log(textStatus)
	        console.log(jqXHR)
	    },
	    */
	    success:function(data,textStatus,jqXHR){
	    	funSuccess(data,textStatus);
	    },
	    /*error:function(xhr,textStatus){
	        console.log('错误')
	        console.log(xhr)
	        console.log(textStatus)
	    },
	    */
	    error:function(xhr,textStatus){
	    	console.log(xhr);
	    	console.log(textStatus);
	    	funError(xhr,textStatus);
	    },
	    complete:function(){
	        console.log('结束')
	    }
	});
}

function ajaxGet(serverUrl,postData,funSuccess,funError)
{
	$.ajax({
	    url:serverUrl,
	    type:'GET', //GET
	    async:true,    //或false,是否异步
	    data:postData,
	    timeout:10000,    //超时时间
	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
	    beforeSend:function(xhr){
	        console.log(xhr)
	        console.log('发送前')
	    },
	    /*success:function(data,textStatus,jqXHR){
	        console.log(data)
	        console.log(textStatus)
	        console.log(jqXHR)
	    },
	    */
	    success:function(data,textStatus,jqXHR){
	    	funSuccess(data,textStatus);
	    },
	    /*error:function(xhr,textStatus){
	        console.log('错误')
	        console.log(xhr)
	        console.log(textStatus)
	    },
	    */
	    error:function(xhr,textStatus){
	    	funError(xhr,textStatus);
	    },
	    complete:function(){
	        console.log('结束')
	    }
	});
}

$(".gettrendingresource").click(function(){
	//alert($(this).attr("tags"));
    //Cookie.set('contentFilter', $(this).attr("tags")) 
     $.ajax({
     type:'GET',
     url:'/bin/filterContent',
         data:{'tag':$(this).attr("tags")},
     	 success: function(status){
         if(status!='')
             console.log('No. of Pages are :'+ status);
         else 
            console.log('Invalid tag');
     }
    });
});

$(document).ready(function(){
setCookie("contentFilter", $('.gettrendingresource').attr("tags"));
});

$(".gettrendingresource").click(function(){

	alert($(this).attr("tags"));
    setCookie("contentFilter", $(this).attr("tags"));
    //Cookie.set('contentFilter', $(this).attr("tags")) 
     $.ajax({
     type:'GET',
     url:'/bin/filterContent',
         data:{'tag':$(this).attr("tags")},
     	 success: function(status){
         console.log(status);
         if(status=='true')
            console.log('valid age');
         else 
            console.log('Invalid age');
     }
    });

});

console.log(getCookie('contentFilter'));

	
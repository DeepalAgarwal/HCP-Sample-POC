
$(".registerbtn").click(function(){
var emailValue, fnameValue, lnameValue, pswValue, pswrepeatValue, countryValue, ageValue;
emailValue =$("#email").val();
fnameValue =$("#fname").val();
lnameValue =$("#lname").val();
pswValue =$("#psw").val();
pswrepeatValue =$("#pswrepeat").val();
countryValue =$("#country").val();
ageValue =$("#age").val();



 $.ajax({
 type:'GET',
 url:'/bin/registerdata',
     data:{'email':emailValue, 
           'fname':fnameValue, 
           'lname':lnameValue, 
           'password':pswValue,
           'country':countryValue,
           'dob':ageValue,},
 success: function(status){
     console.log(status);
     if(status=='true')
     	console.log('valid age');
     else 
		console.log('Invalid age');
 }
});

});
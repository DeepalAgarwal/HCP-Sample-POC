"use strict";
use(function() {
    var info = {};
    info.filter = request.getCookie("contentFilter").value;
    return info;
});




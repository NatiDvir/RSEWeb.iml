
let USER_LIST_URL = buildUrlWithContextPath("home-servlet");
let LOGOUT_URL = buildUrlWithContextPath("logout");
function checkLoggedIn() {
    $.ajax({
        url: USER_LIST_URL,
        method: "POST",
        success: function(myJSON) {
            let myUser = JSON.parse(myJSON);
            if(myUser === null)
                window.location.replace('../../');
        }
    });
}

function logoutClicked(){
    $.ajax({
        url: LOGOUT_URL,
        method:"POST",
        success:function () {
            window.location.replace('../../');
        }
    });
}
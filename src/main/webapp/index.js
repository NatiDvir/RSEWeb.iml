$(function() { // onload...do
    //add a function to the submit event
    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            method:this.method,
            timeout: 2000,
            error: function(errorObject) {
                alert(errorObject.responseText);
                console.error("Failed to login !");
            },
            success: function(nextPageUrl) {
                window.location.href =  nextPageUrl;
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
    getCurrUser();

});


function getCurrUser() {
    let myUser;
    $.ajax({
        url: "home-servlet",
        method: "POST",
        success: function(myJSON) {
            myUser = JSON.parse(myJSON);
            if(myUser !== null)
                window.location.replace('pages/userHome/userHome.html');
        }
    });
}

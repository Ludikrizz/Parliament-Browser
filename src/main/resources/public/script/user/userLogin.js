/**
 * Description placeholder
 *
 * @type {boolean}
 */
let isLoginVisible = false;

/**
 * Description placeholder
 *
 * @returns 
 */
function login() {

    var usernameElement = document.getElementById('username');
    var passwordElement = document.getElementById('password');
    var username = usernameElement.value;
    var password = passwordElement.value;

    usernameElement.style.border = "0.5px solid #161815";
    passwordElement.style.border = "0.5px solid #161815";


    if (username === "" && password === "") {
        usernameElement.style.borderColor = "red";
        passwordElement.style.borderColor = "red";
        return;
    } else if (password === "") {
        passwordElement.style.borderColor = "red";
        return;
    } else if (username === "") {
        usernameElement.style.borderColor = "red";
        return;
    }

    $.ajax({
        method: "POST",
        url: "/parliamentbrowser/user/login",
        contentType: "application/json",
        headers: {
            "username": username,
            "password": password
        }
    })
        .done(function (data) {
            console.log("Success");
            document.cookie = "sessionID=" + data.sessionID + "; path=/";
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            passwordElement.value = "";
        })
}


/**
 * Description placeholder
 *
 * @returns 
 */
function loginButton() {
    isLoginVisible = !isLoginVisible;
    if (isLoginVisible) {
        $(".loginForm").show();
    } else {
        $(".loginForm").hide();
        document.getElementById('username') != null ? document.getElementById('username').value = "" : null;
        document.getElementById('password') != null ? document.getElementById('password').value = "" : null;
    }
}

/**
 * Description placeholder
 *
 * @returns 
 */
function logout() {
    $.ajax({
        method: "POST",
        url: "/parliamentbrowser/user/logout",
        contentType: "application/json",
        headers: {
            "sessionID": document.cookie
        }
    })
        .done(function () {
            console.log("Success");
            document.cookie = "sessionID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
        })
}

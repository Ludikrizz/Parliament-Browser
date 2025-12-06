/**
 * Description placeholder
 *
 * @param {*} event 
 * @returns 
 */
function changePassword(event) {
    event.preventDefault();
    let oldpwd = document.getElementById("oldPassword");
    let newpwd = document.getElementById("newPassword");
    let newpwd2 = document.getElementById("newPasswordRepeat");
    if (newpwd.value !== newpwd2.value) {
        newpwd2.style.border = "0.5px solid red";
        newpwd2.value = "";
        return;
    }
    newpwd2.style.border = "0.5px solid #161815";
    oldpwd.style.border = "0.5px solid #161815";
    $.ajax ({
        method: "POST",
        url: "/parliamentbrowser/user/changePwd",
        contentType: "application/json",
        headers: {
            "oldPwd": oldpwd.value,
            "newPwd": newpwd.value
        }
    })
        .done(function () {
            console.log("Success");
            document.getElementById("oldPassword").value = "";
            document.getElementById("newPassword").value = "";
            document.getElementById("newPasswordRepeat").value = "";
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            oldpwd.value = "";
            oldpwd.style.borderColor = "red";
            newpwd.value = "";
            newpwd2.value = "";
        })
}

/**
 * Description placeholder
 *
 * @param {*} name 
 * @returns 
 */
function deleteUser(name) {
    let element = document.getElementById(name);

    $.ajax ({
        method: "DELETE",
        url: "/parliamentbrowser/user/delete-user",
        contentType: "application/json",
        headers: {
            "username": name
        }
    })
        .done(function () {
            console.log("Success");
            element.remove();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
        })
}

/**
 * Description placeholder
 *
 * @param {*} oldName 
 * @returns 
 */
function saveUserChanges(oldName) {
    let element = document.getElementById(oldName);
    let newName = element.querySelector("#usernameEditForm").value;
    let newPassword = element.querySelector("#newPassword") != null ? element.querySelector("#newPassword").value : "";
    let newGroup = element.querySelector("#groups").value;
    $.ajax ({
        method: "POST",
        url: "/parliamentbrowser/user/save-user",
        contentType: "application/json",
        headers: {
            "oldName": oldName,
            "newName": newName,
            "password": newPassword,
            "group": newGroup
        }
    })
        .done(function () {
            console.log("Success");
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            if (jqXHR.status === 400) {
                element.querySelector("#usernameEditForm").style.borderColor = "red";
                console.log("Username already exists");
            }
        })
}

/**
 * Description placeholder
 *
 * @returns 
 */
function addUser() {
    let username = document.getElementById("usernameEditFormAdd");
    let password = document.getElementById("newPasswordAdd");
    let group = document.getElementById("groupsAdd").value;

    if (username.value === "" ) {
        username.style.borderColor = "red";
        return;
    }
    if (password.value === "" ) {
        password.style.borderColor = "red";
        return;
    }
    if (username.value === "" && password.value === "") {
        username.style.borderColor = "red";
        password.style.borderColor = "red";
        return;
    }

    $.ajax ({
        method: "POST",
        url: "/parliamentbrowser/user/add-user",
        contentType: "application/json",
        headers: {
            "username": username.value,
            "password": password.value,
            "group": group
        }
    })
        .done(function () {
            console.log("Success");
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            if (jqXHR.status === 400) {
                username.style.borderColor = "red";
                console.log("Username already exists");
            }
        })
}


/**
 * Description placeholder
 *
 * @param {*} oldName 
 * @returns 
 */
function saveGroupChanges(oldName) {
    let element = document.getElementById(oldName);
    let newName = element.querySelector("#groupname").value;
    let rights = [];
    let multiselect = $('#' + oldName).find('select');
    let selected = multiselect.find("option:selected");
    selected.each(function () {
        rights.push($(this).val());
    });

    $.ajax ({
        method: "POST",
        url: "/parliamentbrowser/user/save-group",
        contentType: "application/json",
        headers: {
            "oldName": oldName,
            "newName": newName,
            "rights": rights
        }
    })
        .done(function () {
            console.log("Success");
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            if (jqXHR.status === 400) {
                element.querySelector("#groupname").style.borderColor = "red";
                console.log("Group already exists");
            }
        })
}

/**
 * Description placeholder
 *
 * @param {*} name 
 * @returns 
 */
function deleteGroup(name) {

    $.ajax ({
        method: "DELETE",
        url: "/parliamentbrowser/user/delete-group",
        contentType: "application/json",
        headers: {
            "groupName": name
        }
    })
        .done(function () {
            console.log("Success");
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
        })
}

/**
 * Description placeholder
 *
 * @returns 
 */
function addGroup() {
    let addGroup = document.getElementById("addGroup");
    let groupname = addGroup.querySelector("#groupnameAdd");
    let rights = [];
    let multiselect = $('#addGroup').find('select');
    let selected = multiselect.find("option:selected");
    selected.each(function () {
        rights.push($(this).val());
    });

    if (groupname.value === "" ) {
        groupname.style.borderColor = "red";
        return;
    }

    $.ajax ({
        method: "POST",
        url: "/parliamentbrowser/user/add-group",
        contentType: "application/json",
        headers: {
            "groupName": groupname.value,
            "rights": rights
        }
    })
        .done(function () {
            console.log("Success");
            location.reload();
        })
        .fail(function (jqXHR, textStatus){
            console.log("Error: " + textStatus);
            if (jqXHR.status === 400) {
                groupname.style.borderColor = "red";
                console.log("Group already exists");
            }
        })
}
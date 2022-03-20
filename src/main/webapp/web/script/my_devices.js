let account = sessionStorage.getItem("account");
let password = sessionStorage.getItem("password");
let username = sessionStorage.getItem("username");
let usertype = Number.parseInt(sessionStorage.getItem("usertype"));
if (username == null) {
    location.href = "index.html";
}

/**
 * 用户身份 id 转 string.
 *
 * @param type {number}
 * @return {string}
 */
function userTypeName(type) {
    switch (type) {
        case 0:
            return "同学";
        default:
            return "[未知用户]";
    }
}

onload = function onload(event) {
    let usernameDiv = document.getElementById("username");
    usernameDiv.innerText = username;
    let usertypeDiv = document.getElementById("usertype");
    usertypeDiv.innerText = userTypeName(usertype);
};

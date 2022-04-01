let account = sessionStorage.getItem("account");
let password = sessionStorage.getItem("password");
let username = sessionStorage.getItem("username");
let usertype = Number.parseInt(sessionStorage.getItem("usertype"));
if (username == null) {
    location.href = "index.html";
}
let bridge = new ServerBridge(account, password);

addEventListener("load", (event) => {
    let usernameDiv = document.getElementById("username");
    usernameDiv.innerText = username;
    let usertypeDiv = document.getElementById("usertype");
    usertypeDiv.innerText = userTypeName(usertype);
});

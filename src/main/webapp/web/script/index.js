/**
 * 登录按钮.
 *
 * @param event {MouseEvent}
 */
async function login(event) {
    let account = document.getElementById("account").value;
    let password = document.getElementById("password").value;
    let bridge = new ServerBridge(account, password);
    let result = await bridge.userLogin();
    if (result.code === 0) {
        sessionStorage.setItem("account", account);
        sessionStorage.setItem("password", password);
        location.href = "user.html";
        return;
    }
    alert("登录失败");
}

window.onload = function onload(event) {
    let loginButton = document.getElementById("login");
    loginButton.addEventListener("click", login);
}

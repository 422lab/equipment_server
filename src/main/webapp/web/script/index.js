/**
 * 登录按钮.
 *
 * @param event {MouseEvent}
 */
async function login(event) {
    let account = document.getElementById("account").value;
    let password = document.getElementById("password").value;
    let bridge = new ServerBridge(account, password);
    let hit = document.getElementById("loginHit");
    hit.style.display = "inline-block";
    let failHit = document.getElementById("failHit");
    failHit.style.visibility = "hidden";
    let result = await bridge.userLogin();
    hit.style.display = "none";
    if (result.code === 0) {
        sessionStorage.setItem("account", account);
        sessionStorage.setItem("password", password);
        sessionStorage.setItem("username", result.name);
        sessionStorage.setItem("usertype", result.type.toString());
        let next = sessionStorage.getItem("nextURL");
        if (next == null) {
            next = "my_devices.html";
        } else {
            sessionStorage.removeItem("nextURL");
        }
        location.href = next;
        return;
    }
    failHit.style.visibility = "visible";
    if (result.code === 2) {
    }
    switch (result.code) {
        case 2:
            failHit.innerText = "没有此用户";
            break;
        case 3:
            failHit.innerText = "密码错误";
            break;
        default:
            failHit.innerText = "登录失败";
    }
}

addEventListener("load", (event) => {
    let loginButton = document.getElementById("login");
    loginButton.addEventListener("click", login);
});


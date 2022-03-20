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
        location.href = "my_devices.html";
        return;
    }
    failHit.style.visibility = "visible";
}

window.onload = function onload(event) {
    let loginButton = document.getElementById("login");
    loginButton.addEventListener("click", login);
};


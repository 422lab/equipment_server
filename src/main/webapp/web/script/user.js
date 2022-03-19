let username = sessionStorage.getItem("username");

onload = function onload(event) {
    document.body.innerText += "欢迎 " + username;
};

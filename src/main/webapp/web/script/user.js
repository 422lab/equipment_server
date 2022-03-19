let username = sessionStorage.getItem("username");
if (username == null) {
    location.href = "index.html";
}

onload = function onload(event) {
    let usernameDiv = document.getElementById("username");
    usernameDiv.innerText = username;
};

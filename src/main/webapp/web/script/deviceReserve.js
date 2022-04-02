let hash = location.hash.match(/\d+/);
if (hash == null) {
    location.href = "devices.html";
}
let device = Number.parseInt(hash[0]);

addEventListener("load", async (event) => {
    let result = await ServerBridge.getDeviceDescription(device);
    if (result.code === 0) {
        document.getElementById("device").innerText = result.description;
    }
});

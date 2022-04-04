let hash = location.hash.match(/\d+/);
if (hash == null) {
    location.href = "devices.html";
} else {
    let device = Number.parseInt(hash[0]);
}

/**
 * @type {Timeline}
 */
let timeline = null;

let reserveBegin;
let reserveEnd;

function checkReserve(reserves, myself) {
    return true;
}

addEventListener("load", async (event) => {
    let now = new Date().getTime();
    let right = now + 60 * 60 * 1000;
    timeline = new Timeline(document.querySelector(".timeline"), now, right);
    timeline.onChange = (begin, end) => {
        begin = Math.min(Math.max(0, begin), 1);
        end = Math.min(Math.max(0, end), 1);
        reserveBegin = Math.floor(now + begin * (right - now));
        reserveEnd = Math.floor(now + end * (right - now));
        document.getElementById("begin").innerText = new Date(reserveBegin).toLocaleString();
        document.getElementById("end").innerText = new Date(reserveEnd).toLocaleString();
    };
    timeline.onChange(0, 0);
    let result = await ServerBridge.getDeviceDescription(device);
    if (result.code === 0) {
        document.getElementById("device").innerText = result.description;
        timeline.load(result.reserves);
    }
    document.getElementById("reserve").addEventListener("click", async (event) => {
        if (checkReserve(result.reserves, {start: reserveBegin, end: reserveEnd})) {
            let r = await bridge.userReserveDevice({
                type: result.type,
                start: reserveBegin,
                end: reserveEnd,
                devices: [device]
            });
            switch (r.code) {
                case 0:
                    alert("预约成功" + r.success.length + "个设备");
                    break;
                default:
                    alert("预约失败");
            }
            location.reload();
        }
    });
});

let hash = location.hash.match(/\d+/);
let device;
if (hash == null) {
    location.href = "devices.html";
} else {
    device = Number.parseInt(hash[0]);
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
            console.log(r);
            switch (r.code) {
                case 0:
                    await new Toast().text("预约成功" + r.success.length + "个设备").time(3000).showAndSleep();
                    break;
                case 4:
                    await new Toast().text("预约失败 (注意: 每个学习同一时间同种设备只能预约一个)").time(3000).showAndSleep();
                    break;
                default:
                    await new Toast().text("预约失败").time(3000).showAndSleep();
                    break;
            }
            location.reload();
        }
    });
});

/**
 * @param event {PointerEvent}
 */
function clickDevice(event) {
    /**
     * @type {HTMLTableRowElement}
     */
    let line = event.currentTarget;
    /**
     * @type {HTMLTableCellElement}
     */
    let id = line.firstElementChild;
    let device = Number.parseInt(id.innerText);
    location.href = "deviceReserve.html#" + device;
}

async function loadDevices(type, localLike) {
    let result = await ServerBridge.selectDevice(type, localLike);
    let header = document.getElementById("table_header");
    while (header.nextElementSibling) {
        header.nextElementSibling.remove();
    }

    let now = new Date().getTime();
    let right = now + 60 * 60 * 1000;

    let table = document.getElementById("device_table");
    if (result.code === 0) {
        let table = document.getElementById("device_table");
        let devices = result.devices;
        for (let i in devices) {
            let device = devices[i];
            let line = document.createElement("tr");
            line.addEventListener("click", clickDevice);
            {
                let item = document.createElement("td");
                item.innerText = device.uuid + "";
                line.append(item);
            }
            {
                let item = document.createElement("td");
                item.innerText = device.description;
                line.append(item);
            }
            {
                let item = document.createElement("td");
                item.innerText = device.local;
                line.append(item);
            }
            {
                let item = document.createElement("td");
                item.innerText = device.last + "";
                line.append(item);
            }
            {
                let item = document.createElement("td");
                {
                    let timeline = document.createElement("div");
                    timeline.classList.add("timeline");
                    let list = document.createElement("div");
                    list.classList.add("timeline_list");
                    timeline.append(list);
                    new Timeline(timeline, now, right).load(device.reserves);
                    item.append(timeline);
                }
                line.append(item);
            }
            table.append(line);
        }
    }
}

addEventListener("load", async (event) => {
    await loadDevices(1, "%");
});

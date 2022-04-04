addEventListener("load", async (event) => {
    let reserves = await bridge.userGetReserve();
    let countDiv = document.getElementById("count");
    let itemCountDiv = document.getElementById("item_count");
    let myDeviceList = document.getElementById("my_device_list");
    let noDevice = document.getElementById("no_device");
    let itemCount = reserves.reserved.set.length;
    if (itemCount !== 0) {
        itemCountDiv.innerText = itemCount + "";
        myDeviceList.style.display = "table";
        let header = document.getElementById("table_header");
        while (header.nextElementSibling) {
            header.nextElementSibling.remove();
        }
        noDevice.style.display = "none";
        let count = 0;
        for (let item of reserves.reserved.set) {
            count += item.devices.length;
            let line = document.createElement("tr");
            {
                let node = document.createElement("td");
                node.innerText = deviceTypeName(item.type);
                line.append(node);
            }
            {
                let node = document.createElement("td");
                node.innerText = item.devices.length + "";
                line.append(node);
            }
            {
                let node = document.createElement("td");
                node.innerText = new Date(item.start).toLocaleString();
                line.append(node);
            }
            {
                let node = document.createElement("td");
                node.innerText = new Date(item.end).toLocaleString();
                line.append(node);
            }
            myDeviceList.append(line);
        }
        countDiv.innerText = count;
    } else {
        myDeviceList.style.display = "none";
        noDevice.style.display = "block";
    }
});
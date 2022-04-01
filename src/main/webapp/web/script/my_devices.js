addEventListener("load", async (event) => {
    let reserves = await bridge.userGetReserve();
    let countDiv = document.getElementById("count");
    let itemCountDiv = document.getElementById("item_count");
    let myDeviceList = document.getElementById("my_device_list");
    let itemCount = reserves.reserved.set.length;
    if (itemCount !== 0) {
        itemCountDiv.innerText = itemCount + "";
        myDeviceList.innerHTML = "";
        let count = 0;
        for (let item of reserves.reserved.set) {
            count += item.devices.length;
            console.log(item);
            // todo
        }
        countDiv.innerText = count;
    }
});
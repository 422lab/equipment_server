addEventListener("load", async (event) => {
    let d = await bridge.userGetReserve();
    console.log(d);
});
/**
 * 一个对访问服务器接口的简单工具.
 * 仅对服务器接口进行渐层封装.
 */
class ServerBridge {
    /**
     * 一个向服务器执行post的简单封装.
     *
     * @param name {string} 方法名称
     * @param body 参数
     * @return {Promise<any>}
     */
    static async post(name, body) {
        let response = await fetch("https://ztuser.ltd/equipment_server/" + name, {
            method: "post",
            body: JSON.stringify(body),
        });
        return await response.json();
    }

    /**
     * 构造一个使用指定用户的服务器登录连接.
     *
     * 注意: 此方法并不会进行登录验证
     *
     * @param account {string}
     * @param password {string}
     */
    constructor(account, password) {
        this.account = account;
        this.password = password;
    }

    /**
     * 用户账号.
     *
     * @type {string}
     */
    account;

    /**
     * 用户密码.
     *
     * @type {string}
     */
    password;

    /**
     * 执行用户登录验证.
     *
     * @return {Promise<{code:number, name?:string, type?: number}>}
     */
    async userLogin() {
        return await ServerBridge.post("userLogin", {
            account: this.account,
            password: this.password,
        });
    }

    /**
     * 用户获取已预约设备.
     *
     * @return {Promise<{code:number, reserved?:{set:[{devices:[number], type:number, start:number, end:number}]}}>}
     */
    async userGetReserve() {
        return await ServerBridge.post("userGetReserve", {
            account: this.account,
            password: this.password,
        });
    }

    /**
     * 用户预约设备.
     *
     * @param reserves {{type:number, start:number, end:number, devices:[number]}}
     * @return {Promise<{code:number, fails?:[number], success?:[number], start:number, end:number}>}
     */
    async userReserveDevice(reserves) {
        return await ServerBridge.post("userReserveDevice", {
            account: this.account,
            password: this.password,
            reserves: reserves,
        });
    }

    /**
     * 用户取消预约设备.
     *
     * @param device {number}
     * @param start {number}
     * @param end {number}
     * @return {Promise<{code:number}>}
     */
    async userStopReserve(device, start, end) {
        return await ServerBridge.post("userStopReserve", {
            account: this.account,
            password: this.password,
            device: device,
            start: start,
            end: end,
        });
    }

    /**
     * 获取设备列表.
     *
     * @param type {number}
     * @param localLike {string}
     * @return {Promise<{code:number, devices:[{uuid:number, description:string, local:string, last:number, reserves:[{start:number, end:number}]}]}>}
     */
    static async selectDevice(type, localLike) {
        return await ServerBridge.post("selectDevice", {
            type: type,
            localLike: localLike,
        });
    }

    /**
     * 获取设备描述.
     *
     * @param device {number}
     * @return {Promise<{code:number, type?:number, description?:string, local?:string, last?:number, reserves?:[{start:number, end:number}]}>}
     */
    static async getDeviceDescription(device) {
        return await ServerBridge.post("getDeviceDescription", {
            device: device,
        });
    }

}

/**
 * 用户身份 id 转 string.
 *
 * @param type {number}
 * @return {string}
 */
function userTypeName(type) {
    switch (type) {
        case 0:
            return "同学";
        default:
            return "[未知用户]";
    }
}

/**
 * 设备类型转 string.
 *
 * @param type {number}
 * @return {string}
 */
function deviceTypeName(type) {
    switch (type) {
        case 1:
            return "通用电源插口";
        default:
            return "未知设备类型";
    }
}

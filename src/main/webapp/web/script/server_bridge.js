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
     * @return {Promise<{code:number, name:string}>}
     */
    async userLogin() {
        return await ServerBridge.post("userLogin", {
            account: this.account,
            password: this.password,
        });
    }

}
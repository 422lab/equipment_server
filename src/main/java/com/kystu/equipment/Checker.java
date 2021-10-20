package com.kystu.equipment;

/**
 * 对接口参数进行的检查项.
 */
public class Checker {

    /**
     * 检查账号是否合法.
     *
     * @param account 账号
     * @return 合法性
     */
    public static boolean isValidAccount(String account) {
        return account != null && account.matches("^\\w+$");
    }

    /**
     * 检查密码是否合法.
     *
     * @param account 密码
     * @return 合法性
     */
    public static boolean isValidPassword(String account) {
        return account != null && account.matches("^\\w+$");
    }

}

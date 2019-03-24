package top.songm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局常量
 * @author songm
 * @datetime 2018/12/23 17:16
 */
public class GlobalConstant {

    @Getter
    @AllArgsConstructor
    public enum Number {
        ZERO(0, "0"),
        ONE(1, "1"),
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5")
        ;
        private int num;
        private String str;
    }
}

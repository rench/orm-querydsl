package com.lowang.ormquerydsl;

import java.util.Scanner;
/**
 * Ai智能对话核心代码,估值上亿
 * @author wang.ch
 * @date 2018-12-13 16:59:37
 */
public class AiChat {
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in); ) {
      String input;
      while (true) {
        input = sc.next();
        input = input
            .replace("你能", "我能")
            .replace("了吗", "")
            .replace("吗", "")
            .replace("?", "!")
            .replace("？", "!")
            .replace("? ", "!");
        System.out.println(input);
      }
    }
  }
}

package com.alarm.wechat.common;

import java.security.MessageDigest;
import java.util.Arrays;


/**
 * 计算公众平台的消息签名接口.
 *
 * @author 酷酷的诚
 */
public class Sha1 {

  /**
   * 用SHA1算法生成安全签名
   *
   * @param token 票据
   * @param timestamp 时间戳
   * @param nonce 随机字符串
   * @param encrypt 密文
   * @return 安全签名
   * @throws AesException 微信异常
   */
  public static String getSha1(String token, String timestamp, String nonce, String encrypt) throws AesException {
    try {
      int f = 4;
      String[] array = new String[]{token, timestamp, nonce, encrypt};
      StringBuilder sb = new StringBuilder();
      // 字符串排序
      Arrays.sort(array);
      for (int i = 0; i < f; i++) {
        sb.append(array[i]);
      }
      String str = sb.toString();
      // SHA1签名生成
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.update(str.getBytes());
      byte[] digest = md.digest();

      StringBuilder hexStr = new StringBuilder();
      String shaHex;
      for (byte aDigest : digest) {
        shaHex = Integer.toHexString(aDigest & 0xFF);
        if (shaHex.length() < 2) {
          hexStr.append(0);
        }
        hexStr.append(shaHex);
      }
      return hexStr.toString();
    } catch (Exception e) {
      throw new AesException(AesException.COMPUTE_SIGNATURE_ERROR);
    }
  }
}
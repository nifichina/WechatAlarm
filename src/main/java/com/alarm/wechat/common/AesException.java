package com.alarm.wechat.common;


import lombok.Getter;

/**
 * 异常
 *
 * @author 酷酷的诚
 */
public class AesException extends Exception {

  private final static int VALIDATE_SIGNATURE_ERROR = -40001;
  private final static int PARSE_XML_ERROR = -40002;
  final static int COMPUTE_SIGNATURE_ERROR = -40003;
  private final static int ILLEGAL_AES_KEY = -40004;
  private final static int VALIDATE_APPID_ERROR = -40005;
  private final static int ENCRYPTAES_ERROR = -40006;
  private final static int DECRYPTAES_ERROR = -40007;
  private final static int ILLEGAL_BUFFER = -40008;

  @Getter
  private int code;

  private static String getMessage(int code) {
    switch (code) {
      case VALIDATE_SIGNATURE_ERROR:
        return "签名验证错误";
      case PARSE_XML_ERROR:
        return "xml解析失败";
      case COMPUTE_SIGNATURE_ERROR:
        return "sha加密生成签名失败";
      case ILLEGAL_AES_KEY:
        return "SymmetricKey非法";
      case VALIDATE_APPID_ERROR:
        return "appid校验失败";
      case ENCRYPTAES_ERROR:
        return "aes加密失败";
      case DECRYPTAES_ERROR:
        return "aes解密失败";
      case ILLEGAL_BUFFER:
        return "解密后得到的buffer非法";
      default:
        return null;
    }
  }

  AesException(int code) {
    super(getMessage(code));
    this.code = code;
  }

}

package com.baidu.mapframework.app.fpstack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 页面注册 Annotation
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-29 下午5:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterPage {

  /**
   * Page需要注册到的Task
   *
   * @return Task 类名
   */
  String taskName();

}
